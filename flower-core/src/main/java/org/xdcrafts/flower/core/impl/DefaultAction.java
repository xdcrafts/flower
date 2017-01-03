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

import org.xdcrafts.flower.core.Middleware;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Basic implementation of action.
 */
public class DefaultAction extends WithMiddlewareActionBase {

    private final String name;
    private final Function<Map, Map> actionBody;

    public DefaultAction(String name, Function<Map, Map> body) {
        this(name, body, Collections.emptyList());
    }

    public DefaultAction(String name, Function<Map, Map> body, List<Middleware> middleware) {
        super(middleware);
        this.actionBody = body;
        this.name = name;
        this.meta.put("name", name);
        this.meta.put("class", getClass().getName());
        this.meta.put("middleware", middleware);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Map act(Map map) {
        return this.actionBody.apply(map);
    }

    @Override
    public String toString() {
        return "[" + getName() + "].[" + super.toString() + "]";
    }
}
