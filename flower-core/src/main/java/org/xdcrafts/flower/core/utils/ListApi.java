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

package org.xdcrafts.flower.core.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility methods for List.
 */
@SuppressWarnings("unchecked")
public final class ListApi {

    private static final String PATH_MUST_BE_SPECIFIED = "Path must be specified!";

    private ListApi() {
        // Nothing
    }

    /**
     * Walks by list's nodes and extracts optional value of type T.
     * @param list subject
     * @param clazz value type
     * @param path nodes to walk in list
     * @param <T> value type
     * @return optional value of type T
     */
    public static <T> Optional<T> get(final List list, final Class<T> clazz, final Integer... path) {
        if (path == null || path.length == 0) {
            throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
        }
        if (path.length == 1) {
            return Optional.ofNullable((T) list.get(path[0]));
        }
        final Integer[] pathToLastNode = Arrays.copyOfRange(path, 0, path.length - 1);
        final Integer lastKey = path[path.length - 1];
        List intermediateList = list;
        for (Integer index : pathToLastNode) {
            final Object node = intermediateList.get(index);
            if (node != null) {
                final Optional<List> nodeListOption = ClassApi.cast(node, List.class);
                if (nodeListOption.isPresent()) {
                    intermediateList = nodeListOption.get();
                } else {
                    throw new IllegalAccessError("Node with key '" + index + "' is not a list!");
                }
            } else {
                return Optional.empty();
            }
        }
        return Optional.ofNullable((T) intermediateList.get(lastKey));
    }

    /**
     * Walks by list's nodes and extracts optional value of type T.
     * @param <T> value type
     * @param clazz type of value
     * @param list subject
     * @param path nodes to walk in map
     * @return value of type T
     */
    public static <T> T getNullable(final List list, final Class<T> clazz, final Integer... path) {
        if (path == null || path.length == 0) {
            throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
        }
        if (path.length == 1) {
            return (T) list.get(path[0]);
        }
        final Integer[] pathToLastNode = Arrays.copyOfRange(path, 0, path.length - 1);
        final Integer lastKey = path[path.length - 1];
        List intermediateList = list;
        for (Integer index : pathToLastNode) {
            final Object node = intermediateList.get(index);
            if (node != null) {
                final Optional<List> nodeListOption = ClassApi.cast(node, List.class);
                if (nodeListOption.isPresent()) {
                    intermediateList = nodeListOption.get();
                } else {
                    throw new IllegalAccessError("Node with key '" + index + "' is not a list!");
                }
            } else {
                return null;
            }
        }
        return (T) intermediateList.get(lastKey);
    }

    /**
     * Walks by list's nodes and extracts optional value of type T.
     * @param <T> value type
     * @param clazz type of value
     * @param list subject
     * @param path nodes to walk in map
     * @return optional value of type T
     */
    public static <T> T getUnsafe(final List list, final Class<T> clazz, final Integer... path) {
        return get(list, clazz, path).orElseThrow(() -> new IllegalAccessError(
            "List "
            + list
            + " does not have value of type "
            + clazz.getName()
            + " by "
            + Arrays.stream(path).map(Object::toString).collect(Collectors.joining(".")))
        );
    }

    /**
     * Get object value by path.
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static Optional<Object> get(final List list, final Integer... path) {
        return get(list, Object.class, path);
    }

    /**
     * Get object value by path.
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static Object getNullable(final List list, final Integer... path) {
        return getNullable(list, Object.class, path);
    }

    /**
     * Get string value by path.
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static Optional<String> getString(final List list, final Integer... path) {
        return get(list, String.class, path);
    }

    /**
     * Get string value by path.
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static String getNullableString(final List list, final Integer... path) {
        return getNullable(list, String.class, path);
    }

    /**
     * Get map value by path.
     * @param <A> map key type
     * @param <B> map value type
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <A, B> Optional<Map<A, B>> getMap(final List list, final Integer... path) {
        return get(list, Map.class, path).map(m -> (Map<A, B>) m);
    }

    /**
     * Get map value by path.
     * @param <A> map key type
     * @param <B> map value type
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <A, B> Map<A, B> getNullableMap(final List list, final Integer... path) {
        return getNullable(list, Map.class, path);
    }

    /**
     * Get map value by path.
     * @param <A> map key type
     * @param <B> map value type
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <A, B> Map<A, B> getMapUnsafe(final List list, final Integer... path) {
        return getUnsafe(list, Map.class, path);
    }

    /**
     * Get list value by path.
     * @param <T> list value type
     * @param clazz type of value
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <T> Optional<List<T>> getList(final List list, final Class<T> clazz, final Integer... path) {
        return get(list, List.class, path).map(c -> (List<T>) c);
    }

    /**
     * Get list value by path.
     * @param <T> list value type
     * @param clazz type of value
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <T> List<T> getNullableList(final List list, final Class<T> clazz, final Integer... path) {
        return (List<T>) getNullable(list, List.class, path);
    }

    /**
     * Get list value by path.
     * @param <T> list value type
     * @param clazz type of value
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <T> List<T> getListUnsafe(final List list, final Class<T> clazz, final Integer... path) {
        return (List<T>) getUnsafe(list, List.class, path);
    }

    /**
     * Get optional value by path.
     * @param <T> optional value type
     * @param clazz type of value
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <T> Optional<Optional<T>> getOptional(final List list, final Class<T> clazz, final Integer... path) {
        return get(list, Optional.class, path).map(opt -> (Optional<T>) opt);
    }

    /**
     * Get optional value by path.
     * @param <T> optional value type
     * @param clazz type of value
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <T> Optional<T> getNullableOptional(final List list, final Class<T> clazz, final Integer... path) {
        return getNullable(list, Optional.class, path);
    }

    /**
     * Get optional value by path.
     * @param <T> optional value type
     * @param clazz type of value
     * @param list subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <T> Optional<T> getOptionalUnsafe(final List list, final Class<T> clazz, final Integer... path) {
        return getUnsafe(list, Optional.class, path);
    }
}
