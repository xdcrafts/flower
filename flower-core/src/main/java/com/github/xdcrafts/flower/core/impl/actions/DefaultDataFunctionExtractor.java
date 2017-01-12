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

package com.github.xdcrafts.flower.core.impl.actions;

import com.github.xdcrafts.flower.core.DataFunctionExtractor;
import com.github.xdcrafts.flower.core.MethodConverter;
import com.github.xdcrafts.flower.tools.ClassApi;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Default implementation of DataFunctionExtractor.
 */
@SuppressWarnings("unchecked")
public class DefaultDataFunctionExtractor implements DataFunctionExtractor {

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

    private final Collection<MethodConverter> methodConverters;

    public DefaultDataFunctionExtractor(Collection<MethodConverter> methodConverters) {
        this.methodConverters = methodConverters;
    }

    @Override
    public Function<Map, Map> apply(Object object, String methodName) {
        final boolean isVirtual = !(object instanceof Class);
        final Class clazz = isVirtual ? object.getClass() : (Class) object;
        final Method declaredMethod = ClassApi.findMethod(clazz, methodName);
        final Class returnType = declaredMethod.getReturnType();
        final Class[] parameterTypes = declaredMethod.getParameterTypes();
        if (Map.class.isAssignableFrom(returnType)
            && parameterTypes.length == 1
            && Map.class.isAssignableFrom(parameterTypes[0])) {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final MethodType methodType = MethodType.methodType(Map.class, Map.class);
            try {
                final MethodHandle methodHandle = isVirtual
                    ? lookup.findVirtual(clazz, methodName, methodType)
                    : lookup.findStatic(clazz, methodName, methodType);
                return isVirtual
                    ? ctx -> safeVirtualInvoke(methodHandle, object, ctx)
                    : ctx -> safeStaticInvoke(methodHandle, ctx);
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            final Map<MethodConverter, Integer> converterToDistance = methodConverters
                .stream()
                .map(c -> new AbstractMap.SimpleEntry<>(
                    c,
                    ClassApi.methodDistance(declaredMethod, c.method())
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
            final List<MethodConverter> matchedConverters = converterToDistance
                .entrySet()
                .stream()
                .filter(e -> e.getValue() == minimalDistance)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
            if (matchedConverters.size() > 1) {
                throw new IllegalStateException(
                    "Ambiguous MethodConverters for " + clazz + ". " + matchedConverters
                );
            }
            return matchedConverters.get(0).convert(object);
        }
    }
}
