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

package com.github.xdcrafts.flower.core.impl;

import com.github.xdcrafts.flower.core.Middleware;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Default implementation of Middleware.
 */
public class DefaultMiddleware implements Middleware {

    private final String name;
    private final BiFunction<Map<String, Object>, Function<Map, Map>, Function<Map, Map>> body;

    public DefaultMiddleware(
        String name,
        BiFunction<Map<String, Object>, Function<Map, Map>, Function<Map, Map>> body
    ) {
        this.name = name;
        this.body = body;
    }

    @Override
    public Function<Map, Map> apply(Map<String, Object> stringObjectMap, Function<Map, Map> mapMapFunction) {
        return this.body.apply(stringObjectMap, mapMapFunction);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "DefaultMiddleware{"
                + "name='" + name + '\''
                + ", body=" + body
                + '}';
    }
}
