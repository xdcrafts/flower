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

package com.github.xdcrafts.flower.core.impl.selectors;

import com.github.xdcrafts.flower.core.Middleware;
import com.github.xdcrafts.flower.core.Selector;
import com.github.xdcrafts.flower.core.impl.actions.WithMiddlewareActionBase;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstract class as a base for any Selector implementation.
 */
public abstract class WithMiddlewareSelectorBase extends WithMiddlewareActionBase implements Selector {

    public WithMiddlewareSelectorBase(List<Middleware> middleware) {
        super(middleware);
    }

    @Override
    public Map act(Map ctx) {
        return selectAction(ctx)
            .stream()
            .map(a -> (Function<Map, Map>) a)
            .reduce(Function.identity(), Function::andThen)
            .apply(ctx);
    }
}
