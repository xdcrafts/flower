/*
 * Copyright (c) 2017 Vadim Dubs https://github.com/xdcrafts
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.github.xdcrafts.flower.spring.impl;

import com.github.xdcrafts.flower.core.impl.DefaultAction;
import com.github.xdcrafts.flower.tools.ClassApi;
import org.springframework.core.convert.converter.Converter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Spring factory bean for default action that uses bean name as action name.
 */
@SuppressWarnings("unchecked")
public class DefaultActionFactory
    extends AbstractActionFactoryBean<DefaultAction> {

    private static final String SPLITTER = "::";

    private final Object subject;

    public DefaultActionFactory(Object method) {
        this.subject = method;
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultAction.class;
    }

    @Override
    protected DefaultAction createInstance() throws Exception {
        return new DefaultAction(
            getBeanName(),
            buildActionFunction(this.subject),
            getMiddleware(getBeanName())
        );
    }

    private static int distance(Class from, Class to, int current) {
        return to.isAssignableFrom(from)
            ? to.equals(from) ? current : distance(from.getSuperclass(), to, current + 1)
            : -1;
    }

    private static Map safeVirtualInvoke(MethodHandle methodHandle, Object bean, Map context) {
        try {
            return (Map) methodHandle.invoke(bean, context);
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RuntimeException(throwable);
        }
    }

    private static Map safeStaticInvoke(MethodHandle methodHandle, Map context) {
        try {
            return (Map) methodHandle.invoke(context);
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RuntimeException(throwable);
        }
    }

    private Function<Map, Map> buildActionFunction(Object object) {
        final Function<Map, Map> actionFunction;
        if (object instanceof String) {
            final String[] subjectAndMethod = object.toString().split(SPLITTER);
            if (subjectAndMethod.length != 2) {
                throw new IllegalArgumentException(
                    "Invalid action declaration. Either "
                    + "<qualified-class-name>::<method-name> or <bean-name>::<method-name> expected,"
                );
            }
            actionFunction = resolveFunction(subjectAndMethod[0], subjectAndMethod[1]);
        } else {
            actionFunction = convertToFunction(object);
        }
        return actionFunction;
    }

    private Function<Map, Map> resolveFunction(String classOrBeanName, String methodName) {
        try {
            final Object bean = this.getApplicationContext().containsBean(classOrBeanName)
                ? this.getApplicationContext().getBean(classOrBeanName)
                : null;
            final boolean isVirtual = bean != null;
            final Class clazz = isVirtual ? bean.getClass() : Class.forName(classOrBeanName);
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final MethodType methodType = MethodType.methodType(Map.class, Map.class);
            final MethodHandle methodHandle = isVirtual
                ? lookup.findVirtual(clazz, methodName, methodType)
                : lookup.findStatic(clazz, methodName, methodType);
            return isVirtual
                ? ctx -> safeVirtualInvoke(methodHandle, bean, ctx)
                : ctx -> safeStaticInvoke(methodHandle, ctx);
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RuntimeException(throwable);
        }
    }

    private Function<Map, Map> convertToFunction(Object object) {
        final Map<String, Converter> converterBeans = this.getApplicationContext()
            .getBeansOfType(Converter.class, true, false);
        final Map<Converter, Integer> converterToDistance = converterBeans
            .values()
            .stream()
            .map(c -> new AbstractMap.SimpleEntry<>(
                c,
                Arrays.asList(ClassApi.resolveActualTypeArgs(c.getClass(), Converter.class))
            ))
            .filter(entry -> {
                final Type first = entry.getValue().get(0);
                if (!(first instanceof Class)) {
                    return false;
                }
                final Type second = entry.getValue().get(1);
                if (!(second instanceof ParameterizedType)) {
                    return false;
                }
                final Class firstClass = (Class) first;
                final ParameterizedType secondParametrized = (ParameterizedType) second;
                return firstClass.isAssignableFrom(object.getClass())
                    && secondParametrized.getRawType().equals(Function.class)
                    && Arrays.equals(secondParametrized.getActualTypeArguments(), new Type[]{Map.class, Map.class});
            })
            .map(entry -> new AbstractMap.SimpleEntry<>(
                entry.getKey(),
                distance(object.getClass(), (Class) entry.getValue().get(0), 0)
            ))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (converterToDistance.isEmpty()) {
            throw new IllegalStateException(
                "Converter<" + object.getClass() + ", Function<Map, Map>> not found, unable to construct action."
            );
        }
        final int minimalDistance = converterToDistance
            .values()
            .stream()
            .mapToInt(Integer::intValue)
            .reduce(Integer.MAX_VALUE, Integer::min);
        final List<Converter> converters = converterToDistance
            .entrySet()
            .stream()
            .filter(e -> e.getValue() == minimalDistance)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        if (converters.size() > 1) {
            final Map<String, Converter> ambiguousConverters = converterBeans
                .entrySet()
                .stream()
                .filter(e -> converters.contains(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            throw new IllegalStateException(
                "Ambiguous Converter<" + object.getClass() + ", Function<Map, Map>>. " + ambiguousConverters
            );
        }
        return (Function<Map, Map>) converters.get(0).convert(object);
    }
}
