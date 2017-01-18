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

package com.github.xdcrafts.flower.sage;

import com.github.xdcrafts.flower.tools.Named;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Spec interface.
 */
public interface Spec extends Named, Predicate {

    String CONFORMED = "conformed";
    String INVALID = "invalid";

    /**
     * Returns this spec as data.
     */
    Map describe();

    /**
     * Generate sample data, matching this spec.
     */
    Object generate();

    /**
     * Returns data map, describing how the object conforms/or not to spec.
     * Response will contain CONFORMED, INVALID keys.
     */
    Map conform(Object o);

    /**
     * Returns either map describing validation errors or null.
     */
    default Map validate(Object o) {
        return (Map) conform(o).get(INVALID);
    }

    @Override
    default boolean test(Object o) {
        return validate(o) == null;
    }
}
