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

package org.xdcrafts.flower.core.impl.selectors;

import org.xdcrafts.flower.core.Middleware;
import org.xdcrafts.flower.core.Selector;
import org.xdcrafts.flower.core.impl.WithMiddlewareActionBase;

import java.util.List;
import java.util.Map;

/**
 * Abstract class as a base for any Selector implementation.
 */
public abstract class WithMiddlewareSelectorBase extends WithMiddlewareActionBase implements Selector {

    public WithMiddlewareSelectorBase(List<Middleware> middleware) {
        super(middleware);
    }

    @Override
    public Map act(Map ctx) {
        return selectAction(ctx).apply(ctx);
    }
}
