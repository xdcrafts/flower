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

import java.util.Map;

/**
 * Dsl for MapApi methods.
 */
public final class MapDsl {

    private MapDsl() {
        // Nothing
    }

    /**
     * Dsl for mutable map api.
     */
    public static final class Mutable {
        /**
         * Entry point for mutable map dsl.
         * @param map subject
         * @param nodeClass map class
         * @return operator
         */
        public static MapOperator with(final Map map, final Class<? extends Map> nodeClass) {
            return new MapOperator(map, nodeClass);
        }
        /**
         * Entry point for mutable map dsl.
         * @param map subject
         * @return map operator
         */
        public static MapOperator with(final Map map) {
            return new MapOperator(map, map.getClass());
        }
        /**
         * Wrapper over map that performs mutable api operations.
         */
        public static final class MapOperator {
            private final Class<? extends Map> nodeClass;
            private final Map map;
            private MapOperator(final Map map, final Class<? extends Map> nodeClass) {
                this.map = map;
                this.nodeClass = nodeClass;
            }
            /**
             * Associates new value in map placed at path. New nodes are created if needed.
             * @param pathAndValue path to place new value and value at the end of this sequence
             * @return operator
             */
            public MapOperator assoc(final Object... pathAndValue) {
                MapApi.Mutable.assoc(this.map, this.nodeClass, pathAndValue);
                return this;
            }
            /**
             * Associates new value in map placed at path. New nodes are created if needed.
             * @param value value to assoc
             * @param pathString path to place new value and value at the end of this sequence
             * @return operator
             */
            public MapOperator dotAssoc(final String pathString, Object value) {
                MapApi.DotNotation.Mutable.dotAssoc(this.map, this.nodeClass, pathString, value);
                return this;
            }
            /**
             * Dissociates value by specified path.
             * @param path nodes to walk in map path of value
             * @return operator
             */
            public MapOperator dissoc(final Object... path) {
                MapApi.Mutable.dissoc(this.map, path);
                return this;
            }
            /**
             * Dissociates value by specified path.
             * @param pathString nodes to walk in map path of value
             * @return operator
             */
            public MapOperator dotDissoc(final String pathString) {
                MapApi.DotNotation.Mutable.dotDissoc(this.map, pathString);
                return this;
            }
            /**
             * Returns map value.
             * @return map value
             */
            public Map value() {
                return this.map;
            }
        }
    }
}
