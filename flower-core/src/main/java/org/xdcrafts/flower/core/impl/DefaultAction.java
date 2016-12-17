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

import java.util.Map;
import java.util.function.Function;

/**
 * Basic implementation of action.
 */
public class DefaultAction implements Action {

    private final String name;
    private final Function<Map, Map> body;

    public DefaultAction(String name, Function<Map, Map> body) {
        this.name = name;
        this.body = body;
    }

    @Override
    public Map apply(Map context) {
        return this.body.apply(context);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "[" + getName() + "].[" + super.toString() + "]";
    }
}
