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

package org.xdcrafts.flower.core.impl;

import org.xdcrafts.flower.core.Action;
import org.xdcrafts.flower.core.Middleware;
import org.xdcrafts.flower.tools.WithMetaBase;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstract class as a base for any Action implementation.
 */
public abstract class MiddlewaredActionBase extends WithMetaBase implements Action {

    private final Function<Map, Map> applyBody;

    public MiddlewaredActionBase(List<Middleware> middlewares) {
        if (middlewares == null || middlewares.isEmpty()) {
            this.applyBody = this::act;
        } else {
            final Function<Function<Map, Map>, Function<Map, Map>> middleware = middlewares
                .stream()
                .map(mw -> (Function<Function<Map, Map>, Function<Map, Map>>) f -> mw.apply(meta(), f))
                .reduce(Function.identity(), Function::andThen);
            this.applyBody = middleware.apply(this::act);
        }
    }

    @Override
    public Map apply(Map map) {
        return applyBody.apply(map);
    }

    /**
     * Implement your normal 'apply' logic here.
     * This method will be wrapped with list of
     * middlewares and transformed into 'apply' function.
     */
    public abstract Map act(Map map);
}
