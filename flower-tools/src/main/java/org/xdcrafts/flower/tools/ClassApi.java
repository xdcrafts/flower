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

import java.util.Optional;

/**
 * Utility methods for Class.
 */
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
