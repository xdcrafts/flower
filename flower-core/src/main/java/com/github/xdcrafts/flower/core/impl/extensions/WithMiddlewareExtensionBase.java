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

package com.github.xdcrafts.flower.core.impl.extensions;

import com.github.xdcrafts.flower.core.Extension;
import com.github.xdcrafts.flower.core.Middleware;
import com.github.xdcrafts.flower.core.impl.WithMiddlewareActionBase;

import java.util.List;
import java.util.Map;

/**
 * Abstract class as a base for any Extension implementation.
 */
public abstract class WithMiddlewareExtensionBase extends WithMiddlewareActionBase implements Extension {

    public WithMiddlewareExtensionBase(List<Middleware> middleware) {
        super(middleware);
    }

    @Override
    public Map act(Map map) {
        return action().apply(map);
    }
}
