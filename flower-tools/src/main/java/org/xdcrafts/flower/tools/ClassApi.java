/*
 * Copyright (c) 2016 Vadim Dubs https://github.com/xdcrafts
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

package org.xdcrafts.flower.tools;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Utility methods for Class.
 */
@SuppressWarnings("unchecked")
public final class ClassApi {

    private ClassApi() {
        // Nothing
    }

    /**
     * Creates new instance of class, rethrowing all exceptions in runtime.
     * @param clazz type
     * @param <T> class generic
     * @return instance object
     */
    public static <T> T newInstance(final Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ClassApiException(e);
        }
    }

    /**
     * Cast object to clazz and returns Optional. If it can not be cast then the Optional is empty.
     * @param <T> type to cast to
     * @param o object to cast
     * @param clazz type of value class to cast to
     * @return optional value of type T
     */
    public static <T> Optional<T> cast(final Object o, final Class<T> clazz) {
        T result = null;
        /*
         * There is no null pointer checking for clazz to detect logic errors.
         */
        if (o != null && clazz.isInstance(o)) {
            result = (T) o;
        }
        return Optional.ofNullable(result);
    }

    /**
     * Resolves the actual generic type arguments for a base class, as viewed from a subclass or implementation.
     *
     * @param <T> base type
     * @param offspring class or interface subclassing or extending the base type
     * @param base base class
     * @param actualArgs the actual type arguments passed to the offspring class
     * @return actual generic type arguments, must match the type parameters of the offspring class. If omitted, the
     * type parameters will be used instead.
     */
    public static <T> Type[] resolveActualTypeArgs(Class<? extends T> offspring, Class<T> base, Type... actualArgs) {
        if (offspring == null
            || base == null
            || (actualArgs.length != 0 && actualArgs.length != offspring.getTypeParameters().length)) {
            throw new IllegalArgumentException(
                "offspring and base should not be null, number ot actualArgs should match number of offspring types"
            );
        }
        if (actualArgs.length == 0) {
            actualArgs = offspring.getTypeParameters();
        }
        Map<String, Type> typeVariables = new HashMap<>();
        for (int i = 0; i < actualArgs.length; i++) {
            TypeVariable<?> typeVariable = offspring.getTypeParameters()[i];
            typeVariables.put(typeVariable.getName(), actualArgs[i]);
        }
        List<Type> ancestors = new LinkedList<>();
        if (offspring.getGenericSuperclass() != null) {
            ancestors.add(offspring.getGenericSuperclass());
        }
        Collections.addAll(ancestors, offspring.getGenericInterfaces());
        for (Type type : ancestors) {
            if (type instanceof Class<?>) {
                Class<?> ancestorClass = (Class<?>) type;
                if (base.isAssignableFrom(ancestorClass)) {
                    Type[] result = resolveActualTypeArgs((Class<? extends T>) ancestorClass, base);
                    if (result != null) {
                        return result;
                    }
                }
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type rawType = parameterizedType.getRawType();
                if (rawType instanceof Class<?>) {
                    Class<?> rawTypeClass = (Class<?>) rawType;
                    if (base.isAssignableFrom(rawTypeClass)) {
                        List<Type> resolvedTypes = new LinkedList<>();
                        for (Type t : parameterizedType.getActualTypeArguments()) {
                            if (t instanceof TypeVariable<?>) {
                                Type resolvedType = typeVariables.get(((TypeVariable<?>) t).getName());
                                resolvedTypes.add(resolvedType != null ? resolvedType : t);
                            } else {
                                resolvedTypes.add(t);
                            }
                        }

                        Type[] result = resolveActualTypeArgs(
                            (Class<? extends T>) rawTypeClass,
                            base,
                            resolvedTypes.toArray(new Type[] {})
                        );
                        if (result != null) {
                            return result;
                        }
                    }
                }
            }
        }
        return offspring.equals(base) ? actualArgs : new Type[]{};
    }

    /**
     * Custom exception.
     */
    public static final class ClassApiException extends RuntimeException {
        /**
         * Default constructor for MapApi exception.
         * @param cause throwable
         */
        public ClassApiException(final Throwable cause) {
            super(cause);
        }
    }
}
