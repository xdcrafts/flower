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

package com.github.xdcrafts.flower.spring;

import com.github.xdcrafts.flower.tools.ClassApi;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

/**
 * Converts specific method of object of some type to Function<Map, Map>.
 * @param <T> type
 */
public interface MethodConverter<T> {

    /**
     * Convertible class.
     */
    Class<T> convertibleClass();

    /**
     * Convertible method name;
     */
    String methodName();

    /**
     * Calculates priority of this converter based on classDistance between declarator and convertible class.
     * @return -1 if this converter can not handle method value greater or equals then 0 otherwise
     * (0 - highest priority)
     */
    default int priority(Method method) {
        return method.getName().equals(methodName())
            && convertibleClass().isAssignableFrom(method.getDeclaringClass())
            ? ClassApi.classDistance(method.getDeclaringClass(), convertibleClass())
            : -1;
    }

    Function<Map, Map> convert(T object);
}
