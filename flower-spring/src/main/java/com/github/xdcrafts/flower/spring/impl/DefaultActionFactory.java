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
import com.github.xdcrafts.flower.spring.MethodConverter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
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

    private final String subject;
    private final String method;

    public DefaultActionFactory(String method) {
        final String[] subjectAndMethod = method.split(SPLITTER);
        if (subjectAndMethod.length != 2) {
            throw new IllegalArgumentException(
                "Invalid action declaration. Either "
                + "<qualified-class-name>::<method-name> or <bean-name>::<method-name> expected,"
            );
        }
        this.subject = subjectAndMethod[0];
        this.method = subjectAndMethod[1];
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultAction.class;
    }

    @Override
    protected DefaultAction createInstance() throws Exception {
        return new DefaultAction(
            getBeanName(),
            buildActionFunction(this.subject, this.method),
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

    private Function<Map, Map> buildActionFunction(String classOrBeanName, String methodName) {
        try {
            final Object bean = this.getApplicationContext().containsBean(classOrBeanName)
                ? this.getApplicationContext().getBean(classOrBeanName)
                : null;
            final boolean isVirtual = bean != null;
            final Class clazz = isVirtual ? bean.getClass() : Class.forName(classOrBeanName);
            final List<Method> methods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .collect(Collectors.toList());
            if (methods.isEmpty()) {
                throw new IllegalArgumentException(classOrBeanName + "::" + methodName + " not found");
            }
            if (methods.size() > 1) {
                throw new IllegalArgumentException(
                    classOrBeanName + "::" + methodName
                    + " more then one method found, can not decide which one to use. "
                    + methods
                );
            }
            final Method declaredMethod = methods.get(0);
            final Class returnType = declaredMethod.getReturnType();
            final Class[] parameterTypes = declaredMethod.getParameterTypes();
            if (Map.class.isAssignableFrom(returnType)
                && parameterTypes.length == 1
                && Map.class.isAssignableFrom(parameterTypes[0])) {
                final MethodHandles.Lookup lookup = MethodHandles.lookup();
                final MethodType methodType = MethodType.methodType(Map.class, Map.class);
                final MethodHandle methodHandle = isVirtual
                    ? lookup.findVirtual(clazz, methodName, methodType)
                    : lookup.findStatic(clazz, methodName, methodType);
                return isVirtual
                    ? ctx -> safeVirtualInvoke(methodHandle, bean, ctx)
                    : ctx -> safeStaticInvoke(methodHandle, ctx);
            } else {
                final Map<String, MethodConverter> converterBeans = this.getApplicationContext()
                    .getBeansOfType(MethodConverter.class, true, false);
                final Map<MethodConverter, Integer> converterToDistance = converterBeans
                    .values()
                    .stream()
                    .map(c -> new AbstractMap.SimpleEntry<>(
                        c,
                        c.priority(declaredMethod)
                    ))
                    .filter(e -> e.getValue() >= 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                if (converterToDistance.isEmpty()) {
                    throw new IllegalStateException(
                        "MethodConverter<" + clazz + "> not found, unable to construct action."
                    );
                }
                final int minimalDistance = converterToDistance
                    .values()
                    .stream()
                    .mapToInt(Integer::intValue)
                    .reduce(Integer.MAX_VALUE, Integer::min);
                final List<MethodConverter> converters = converterToDistance
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue() == minimalDistance)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
                if (converters.size() > 1) {
                    final Map<String, MethodConverter> ambiguousConverters = converterBeans
                        .entrySet()
                        .stream()
                        .filter(e -> converters.contains(e.getValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    throw new IllegalStateException(
                        "Ambiguous MethodConverters for " + clazz + ". " + ambiguousConverters
                    );
                }
                return converters.get(0).convert(bean);
            }
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RuntimeException(throwable);
        }
    }
}
