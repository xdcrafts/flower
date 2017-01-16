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

import com.github.xdcrafts.flower.core.Extension;
import com.github.xdcrafts.flower.core.Feature;
import com.github.xdcrafts.flower.core.Selector;

import java.util.Map;

/**
 * Default implementation of Feature.
 */
public class DefaultFeature implements Feature {

    private final String name;
    private final Map<Extension, Selector> extensions;

    public DefaultFeature(String name, Map<Extension, Selector> extensions) {
        this.name = name;
        this.extensions = extensions;
        extensions.entrySet().forEach(e -> e.getValue().register(e.getKey()));
    }

    @Override
    public Map<Extension, Selector> extensions() {
        return this.extensions;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "DefaultFeature{"
                + "name='" + name + '\''
                + ", extensions=" + extensions
                + '}';
    }
}
