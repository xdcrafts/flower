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

import com.github.xdcrafts.flower.core.Action;
import com.github.xdcrafts.flower.core.Core;
import com.github.xdcrafts.flower.core.Middleware;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of Extension.
 */
public class DefaultExtension extends WithMiddlewareExtensionBase {

    private final String name;
    private final Action action;
    private final Map configuration;

    public DefaultExtension(String name, Action action, Map configuration) {
        this(name, action, configuration, Collections.emptyList());
    }

    public DefaultExtension(String name, Action action, Map configuration, List<Middleware> middleware) {
        super(middleware);
        this.name = name;
        this.action = action;
        this.configuration = configuration;
        this.meta.put(Core.ActionMeta.NAME, name);
        this.meta.put(Core.ActionMeta.TYPE, getClass().getName());
        this.meta.put(Core.ActionMeta.MIDDLEWARE, middleware);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Action action() {
        return this.action;
    }

    @Override
    public Map configuration() {
        return this.configuration;
    }

    @Override
    public String toString() {
        return "DefaultExtension{"
                + "name='" + this.name + '\''
                + ", action=" + this.action
                + ", configuration=" + this.configuration
                + '}';
    }
}
