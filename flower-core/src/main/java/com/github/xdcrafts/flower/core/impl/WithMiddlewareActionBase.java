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

import com.github.xdcrafts.flower.core.Action;
import com.github.xdcrafts.flower.core.Middleware;
import com.github.xdcrafts.flower.tools.WithMetaBase;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstract class as a base for any Action implementation.
 */
public abstract class WithMiddlewareActionBase extends WithMetaBase implements Action {

    private final Function<Map, Map> applyBody;

    public WithMiddlewareActionBase(List<Middleware> middleware) {
        if (middleware == null || middleware.isEmpty()) {
            this.applyBody = this::act;
        } else {
            final Function<Function<Map, Map>, Function<Map, Map>> reduced = middleware
                .stream()
                .map(mw -> (Function<Function<Map, Map>, Function<Map, Map>>) f -> mw.apply(meta(), f))
                .reduce(Function.identity(), Function::andThen);
            this.applyBody = reduced.apply(this::act);
        }
    }

    @Override
    public Map apply(Map map) {
        return applyBody.apply(map);
    }

    /**
     * Implement your normal 'apply' logic here.
     * This method will be wrapped with list of
     * middleware and transformed into 'apply' function.
     */
    public abstract Map act(Map map);
}
