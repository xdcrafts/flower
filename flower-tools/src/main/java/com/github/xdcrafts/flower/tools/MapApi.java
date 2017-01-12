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

package com.github.xdcrafts.flower.tools;

import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility methods for Map.
 */
@SuppressWarnings("unchecked")
public final class MapApi {

    private static final String PATH_MUST_BE_SPECIFIED = "Path must be specified!";

    /**
     * Private constructor.
     */
    private MapApi() {
        // Nothing
    }

    /**
     * Walks by map's nodes and extracts optional value of type T.
     * @param <T> value type
     * @param clazz type of value
     * @param map subject
     * @param path nodes to walk in map
     * @return optional value of type T
     */
    public static <T> Optional<T> get(final Map map, final Class<T> clazz, final Object... path) {
        if (path == null || path.length == 0) {
            throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
        }
        if (path.length == 1) {
            return Optional.ofNullable((T) map.get(path[0]));
        }
        final Object[] pathToLastNode = Arrays.copyOfRange(path, 0, path.length - 1);
        final Object lastKey = path[path.length - 1];
        Map<Object, Object> intermediateMap = map;
        for (Object key : pathToLastNode) {
            final Object node = intermediateMap.get(key);
            if (node != null) {
                final Optional<Map> nodeMapOption = ClassApi.cast(node, Map.class);
                if (nodeMapOption.isPresent()) {
                    intermediateMap = nodeMapOption.get();
                } else {
                    throw new IllegalAccessError("Node with key '" + key + "' is not a map!");
                }
            } else {
                return Optional.empty();
            }
        }
        return Optional.ofNullable((T) intermediateMap.get(lastKey));
    }

    /**
     * Walks by map's nodes and extracts optional value of type T.
     * @param <T> value type
     * @param clazz type of value
     * @param map subject
     * @param path nodes to walk in map
     * @return value of type T
     */
    public static <T> T getNullable(final Map map, final Class<T> clazz, final Object... path) {
        if (path == null || path.length == 0) {
            throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
        }
        if (path.length == 1) {
            return (T) map.get(path[0]);
        }
        final Object[] pathToLastNode = Arrays.copyOfRange(path, 0, path.length - 1);
        final Object lastKey = path[path.length - 1];
        Map<Object, Object> intermediateMap = map;
        for (Object key : pathToLastNode) {
            final Object node = intermediateMap.get(key);
            if (node != null) {
                final Optional<Map> nodeMapOption = ClassApi.cast(node, Map.class);
                if (nodeMapOption.isPresent()) {
                    intermediateMap = nodeMapOption.get();
                } else {
                    throw new IllegalAccessError("Node with key '" + key + "' is not a map!");
                }
            } else {
                return null;
            }
        }
        return (T) intermediateMap.get(lastKey);
    }

    /**
     * Walks by map's nodes and extracts optional value of type T.
     * @param <T> value type
     * @param clazz type of value
     * @param map subject
     * @param path nodes to walk in map
     * @return optional value of type T
     */
    public static <T> T getUnsafe(final Map map, final Class<T> clazz, final Object... path) {
        return get(map, clazz, path).orElseThrow(() -> new IllegalAccessError(
            "Map "
            + map
            + " does not have value of type "
            + clazz.getName()
            + " by "
            + Arrays.stream(path).map(Object::toString).collect(Collectors.joining(".")))
        );
    }

    /**
     * Get object value by path.
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static Optional<Object> get(final Map map, final Object... path) {
        return get(map, Object.class, path);
    }

    /**
     * Get object value by path.
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static Object getNullable(final Map map, final Object... path) {
        return getNullable(map, Object.class, path);
    }

    /**
     * Get object value by path.
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static Object getUnsafe(final Map map, final Object... path) {
        return getUnsafe(map, Object.class, path);
    }

    /**
     * Get string value by path.
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static Optional<String> getString(final Map map, final Object... path) {
        return get(map, String.class, path);
    }

    /**
     * Get string value by path.
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static String getNullableString(final Map map, final Object... path) {
        return getNullable(map, String.class, path);
    }

    /**
     * Get string value by path.
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static String getStringUnsafe(final Map map, final Object... path) {
        return getUnsafe(map, String.class, path);
    }

    /**
     * Get map value by path.
     * @param <A> map key type
     * @param <B> map value type
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <A, B> Optional<Map<A, B>> getMap(final Map map, final Object... path) {
        return get(map, Map.class, path).map(m -> (Map<A, B>) m);
    }

    /**
     * Get map value by path.
     * @param <A> map key type
     * @param <B> map value type
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <A, B> Map<A, B> getNullableMap(final Map map, final Object... path) {
        return getNullable(map, Map.class, path);
    }

    /**
     * Get map value by path.
     * @param <A> map key type
     * @param <B> map value type
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <A, B> Map<A, B> getMapUnsafe(final Map map, final Object... path) {
        return getUnsafe(map, Map.class, path);
    }

    /**
     * Get list value by path.
     * @param <T> list value type
     * @param clazz type of value
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <T> Optional<List<T>> getList(final Map map, final Class<T> clazz, final Object... path) {
        return get(map, List.class, path).map(c -> (List<T>) c);
    }

    /**
     * Get list value by path.
     * @param <T> list value type
     * @param clazz type of value
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <T> List<T> getNullableList(final Map map, final Class<T> clazz, final Object... path) {
        return (List<T>) getNullable(map, List.class, path);
    }

    /**
     * Get list value by path.
     * @param <T> list value type
     * @param clazz type of value
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <T> List<T> getListUnsafe(final Map map, final Class<T> clazz, final Object... path) {
        return (List<T>) getUnsafe(map, List.class, path);
    }

    /**
     * Get optional value by path.
     * @param <T> optional value type
     * @param clazz type of value
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <T> Optional<Optional<T>> getOptional(final Map map, final Class<T> clazz, final Object... path) {
        return get(map, Optional.class, path).map(opt -> (Optional<T>) opt);
    }

    /**
     * Get optional value by path.
     * @param <T> optional value type
     * @param clazz type of value
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <T> Optional<T> getNullableOptional(final Map map, final Class<T> clazz, final Object... path) {
        return getNullable(map, Optional.class, path);
    }

    /**
     * Get optional value by path.
     * @param <T> optional value type
     * @param clazz type of value
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static <T> Optional<T> getOptionalUnsafe(final Map map, final Class<T> clazz, final Object... path) {
        return getUnsafe(map, Optional.class, path);
    }

    /**
     * Get set of string keys by path.
     * @param map subject
     * @param path nodes to walk in map
     * @return value
     */
    public static Set<String> getKeysIn(final Map map, final Object... path) {
        return getMap(map, path)
            .map(m -> m
                .keySet()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toSet())
            ).orElse(new HashSet<>());
    }

    /**
     * Checks if specified path exists.
     * @param map subject
     * @param path nodes to walk in map
     * @return boolean value
     */
    public static boolean contains(final Map map, final Object... path) {
        return get(map, path).isPresent();
    }

    /**
     * Methods for mutable maps.
     */
    public static final class Mutable {

        /**
         * Private constructor.
         */
        private Mutable() {
            // Nothing
        }

        /**
         * Associates new value in map placed at path. New nodes are created with nodeClass if needed.
         * @param map subject original map
         * @param nodeClass class for intermediate nodes
         * @param path nodes to walk in map path to place new value
         * @param value new value
         * @return original map
         */
        public static Map assoc(
            final Map map, final Class<? extends Map> nodeClass, final Object[] path, final Object value
        ) {
            if (path == null || path.length == 0) {
                throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
            }
            if (value == null) {
                return map;
            }
            if (path.length == 1) {
                map.put(path[0], value);
                return map;
            }
            final Object[] pathToLastNode = Arrays.copyOfRange(path, 0, path.length - 1);
            final Object lastKey = path[path.length - 1];
            Map<Object, Object> intermediateMap = map;
            for (Object key : pathToLastNode) {
                final Object node = intermediateMap.computeIfAbsent(key, k -> ClassApi.newInstance(nodeClass));
                final Optional<Map> nodeMapOption = ClassApi.cast(node, Map.class);
                if (nodeMapOption.isPresent()) {
                    intermediateMap = nodeMapOption.get();
                } else {
                    throw new IllegalAccessError("Node with key '" + key + "' is not a map!");
                }
            }
            intermediateMap.put(lastKey, value);
            return map;
        }

        /**
         * Associates new value in map placed at path. New nodes are created with same class as map if needed.
         * @param map subject original map
         * @param path nodes to walk in map path to place new value
         * @param value new value
         * @return original map
         */
        public static Map assoc(final Map map, final Object[] path, final Object value) {
            return assoc(map, map.getClass(), path, value);
        }


        /**
         * Associates new value in map placed at path. New nodes are created with nodeClass if needed.
         * @param map subject original map
         * @param nodeClass class for intermediate nodes
         * @param pathAndValue path to place new value and value at the end of this sequence
         * @return original map
         */
        public static Map assoc(final Map map, final Class<? extends Map> nodeClass, final Object... pathAndValue) {
            if (pathAndValue == null || pathAndValue.length < 1) {
                throw new IllegalArgumentException(
                    "Path and value are required! Call it like: assocIn(Object[] path, Object value)");
            }
            return assoc(
                map,
                nodeClass,
                Arrays.copyOfRange(pathAndValue, 0, pathAndValue.length - 1),
                pathAndValue[pathAndValue.length - 1]
            );
        }

        /**
         * Associates new value in map placed at path. New nodes are created with same class as map if needed.
         * @param map subject original map
         * @param pathAndValue path to place new value and value at the end of this sequence
         * @return original map
         */
        public static Map assoc(final Map map, final Object... pathAndValue) {
            if (pathAndValue == null || pathAndValue.length < 1) {
                throw new IllegalArgumentException(
                    "Path and value are required! Call it like: assocIn(Object[] path, Object value)");
            }
            return assoc(
                map,
                Arrays.copyOfRange(pathAndValue, 0, pathAndValue.length - 1),
                pathAndValue[pathAndValue.length - 1]
            );
        }

        /**
         * Dissociates value by specified path.
         * @param map subject original map
         * @param path nodes to walk in map path of value
         * @return original map
         */
        public static Map dissoc(final Map map, final Object... path) {
            if (path == null || path.length == 0) {
                throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
            }
            if (path.length == 1) {
                map.remove(path[0]);
                return map;
            }
            final Object[] pathToLastNode = Arrays.copyOfRange(path, 0, path.length - 1);
            final Object lastKey = path[path.length - 1];
            Map<Object, Object> intermediateMap = map;
            for (Object key : pathToLastNode) {
                final Object node = intermediateMap.get(key);
                if (node != null) {
                    final Optional<Map> nodeMapOption = ClassApi.cast(node, Map.class);
                    if (nodeMapOption.isPresent()) {
                        intermediateMap = nodeMapOption.get();
                    } else {
                        throw new IllegalAccessError("Node with key '" + key + "' is not a map!");
                    }
                } else {
                    return map;
                }
            }
            intermediateMap.remove(lastKey);
            return map;
        }
    }

    /**
     * DotNotation version of api.
     */
    public static final class DotNotation {

        private static final String SEPARATOR = ".";
        private static final String SEPARATOR_REGEX = "\\.";

        /**
         * Private constructor.
         */
        private DotNotation() {
            // nothing
        }

        /**
         * Walks by map's nodes and extracts optional value of type T.
         * @param <T> value type
         * @param clazz type of value
         * @param map subject
         * @param pathString dot separated string with nodes of nested maps
         * @return optional value of type T
         */
        public static <T> Optional<T> dotGet(final Map map, final Class<T> clazz, final String pathString) {
            if (pathString == null || pathString.isEmpty()) {
                throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
            }
            if (!pathString.contains(SEPARATOR)) {
                return Optional.ofNullable((T) map.get(pathString));
            }
            final Object[] path = pathString.split(SEPARATOR_REGEX);
            return MapApi.get(map, clazz, path);
        }

        /**
         * Walks by map's nodes and extracts optional value of type T.
         * @param <T> value type
         * @param clazz type of value
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value of type T
         */
        public static <T> T dotGetNullable(final Map map, final Class<T> clazz, final String pathString) {
            if (pathString == null || pathString.isEmpty()) {
                throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
            }
            if (!pathString.contains(SEPARATOR)) {
                return (T) map.get(pathString);
            }
            final Object[] path = pathString.split(SEPARATOR_REGEX);
            return MapApi.getUnsafe(map, clazz, path);
        }

        /**
         * Walks by map's nodes and extracts optional value of type T.
         * @param <T> value type
         * @param clazz type of value
         * @param map subject
         * @param pathString nodes to walk in map
         * @return optional value of type T
         */
        public static <T> T dotGetUnsafe(final Map map, final Class<T> clazz, final String pathString) {
            return dotGet(map, clazz, pathString).orElseThrow(() -> new IllegalAccessError(
                "Map "
                + map
                + " does not have value of type "
                + clazz.getName()
                + " by "
                + pathString)
            );
        }

        /**
         * Get object value by path.
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static Optional<Object> dotGet(final Map map, final String pathString) {
            return dotGet(map, Object.class, pathString);
        }

        /**
         * Get object value by path.
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static Object dotGetNullable(final Map map, final String pathString) {
            return dotGetNullable(map, Object.class, pathString);
        }

        /**
         * Get object value by path.
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static Object dotGetUnsafe(final Map map, final String pathString) {
            return dotGetUnsafe(map, Object.class, pathString);
        }

        /**
         * Get string value by path.
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static Optional<String> dotGetString(final Map map, final String pathString) {
            return dotGet(map, String.class, pathString);
        }

        /**
         * Get string value by path.
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static String dotGetNullableString(final Map map, final String pathString) {
            return dotGetNullable(map, String.class, pathString);
        }

        /**
         * Get string value by path.
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static String dotGetStringUnsafe(final Map map, final String pathString) {
            return dotGetUnsafe(map, String.class, pathString);
        }

        /**
         * Get map value by path.
         * @param <A> map key type
         * @param <B> map value type
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static <A, B> Optional<Map<A, B>> dotGetMap(final Map map, final String pathString) {
            return dotGet(map, Map.class, pathString).map(m -> (Map<A, B>) m);
        }

        /**
         * Get map value by path.
         * @param <A> map key type
         * @param <B> map value type
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static <A, B> Map<A, B> dotGetNullableMap(final Map map, final String pathString) {
            return dotGetNullable(map, Map.class, pathString);
        }

        /**
         * Get map value by path.
         * @param <A> map key type
         * @param <B> map value type
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static <A, B> Map<A, B> dotGetMapUnsafe(final Map map, final String pathString) {
            return dotGetUnsafe(map, Map.class, pathString);
        }

        /**
         * Get list value by path.
         * @param <T> list value type
         * @param clazz type of value
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static <T> Optional<List<T>> dotGetList(final Map map, final String pathString, final Class<T> clazz) {
            return dotGet(map, List.class, pathString).map(c -> (List<T>) c);
        }

        /**
         * Get list value by path.
         * @param <T> list value type
         * @param clazz type of value
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static <T> List<T> dotGetNullableList(final Map map, final String pathString, final Class<T> clazz) {
            return (List<T>) dotGetNullable(map, List.class, pathString);
        }

        /**
         * Get list value by path.
         * @param <T> list value type
         * @param clazz type of value
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static <T> List<T> dotGetListUnsafe(final Map map, final String pathString, final Class<T> clazz) {
            return (List<T>) dotGetUnsafe(map, List.class, pathString);
        }

        /**
         * Get optional value by path.
         * @param <T> optional value type
         * @param clazz type of value
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static <T> Optional<Optional<T>> dotGetOptional(
            final Map map, final String pathString, final Class<T> clazz
        ) {
            return dotGet(map, Optional.class, pathString).map(opt -> (Optional<T>) opt);
        }

        /**
         * Get optional value by path.
         * @param <T> optional value type
         * @param clazz type of value
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static <T> Optional<T> dotGetNullableOptional(
            final Map map, final String pathString, final Class<T> clazz
        ) {
            return dotGetNullable(map, Optional.class, pathString);
        }

        /**
         * Get optional value by path.
         * @param <T> optional value type
         * @param clazz type of value
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static <T> Optional<T> dotGetOptionalUnsafe(
            final Map map, final String pathString, final Class<T> clazz
        ) {
            return dotGetUnsafe(map, Optional.class, pathString);
        }

        /**
         * Get set of string keys by path.
         * @param map subject
         * @param pathString nodes to walk in map
         * @return value
         */
        public static Set<String> dotGetKeysIn(final Map map, final String pathString) {
            return dotGetMap(map, pathString)
                .map(m -> m
                    .keySet()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet())
                ).orElse(new HashSet<>());
        }

        /**
         * Checks if specified path exists.
         * @param map subject
         * @param pathString nodes to walk in map
         * @return boolean value
         */
        public static boolean dotContains(final Map map, final String pathString) {
            return dotGet(map, pathString).isPresent();
        }

        /**
         * Methods for mutable maps.
         */
        public static final class Mutable {

            /**
             * Private constructor.
             */
            private Mutable() {
                // Nothing
            }

            /**
             * Associates new value in map placed at path. New nodes are created with nodeClass if needed.
             * @param map subject original map
             * @param nodeClass class for intermediate nodes
             * @param pathString nodes to walk in map path to place new value
             * @param value new value
             * @return original map
             */
            public static Map dotAssoc(
                final Map map, final Class<? extends Map> nodeClass, final String pathString, final Object value
            ) {
                if (pathString == null || pathString.isEmpty()) {
                    throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
                }
                if (value == null) {
                    return map;
                }
                if (!pathString.contains(SEPARATOR)) {
                    map.put(pathString, value);
                    return map;
                }
                return MapApi.Mutable.assoc(map, nodeClass, pathString.split(SEPARATOR_REGEX), value);
            }

            /**
             * Associates new value in map placed at path. New nodes are created with same class as map if needed.
             * @param map subject original map
             * @param pathString nodes to walk in map path to place new value
             * @param value new value
             * @return original map
             */
            public static Map dotAssoc(final Map map, final String pathString, final Object value) {
                return dotAssoc(map, map.getClass(), pathString, value);
            }


            /**
             * Dissociates value by specified path.
             * @param map subject original map
             * @param pathString nodes to walk in map path of value
             * @return original map
             */
            public static Map dotDissoc(final Map map, final String pathString) {
                if (pathString == null || pathString.isEmpty()) {
                    throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
                }
                if (!pathString.contains(SEPARATOR)) {
                    map.remove(pathString);
                    return map;
                }
                final Object[] path = pathString.split(SEPARATOR_REGEX);
                return MapApi.Mutable.dissoc(map, path);
            }
        }
    }
}
