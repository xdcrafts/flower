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

package com.github.xdcrafts.flower.core.impl.middleware;

import com.github.xdcrafts.flower.core.Middleware;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Toggle middleware.
 */
public class ToggleMiddleware implements Middleware, Supplier<Boolean> {

    private final String name;
    private volatile boolean isEnabled;

    public ToggleMiddleware(String name) {
        this.name = name;
    }

    public ToggleMiddleware(String name, boolean isEnabled) {
        this.name = name;
        this.isEnabled = isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Boolean get() {
        return this.isEnabled;
    }

    @Override
    public Function<Map, Map> apply(Map<String, Object> stringObjectMap, Function<Map, Map> mapMapFunction) {
        return ctx -> this.isEnabled ? mapMapFunction.apply(ctx) : ctx;
    }

    @Override
    public String toString() {
        return "ToggleMiddleware{"
                + "name='" + name + '\''
                + ", isEnabled=" + isEnabled
                + '}';
    }
}
