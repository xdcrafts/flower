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

package org.xdcrafts.flower.spring.impl;

import org.xdcrafts.flower.spring.Feature;

import java.util.Map;

/**
 * Default implementation of Feature interface.
 */
public class DefaultFeature implements Feature {

    private final boolean enabled;
    private final Map<String, String> extensions;

    public DefaultFeature(boolean enabled, Map<String, String> extensions) {
        this.enabled = enabled;
        this.extensions = extensions;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Map<String, String> extensions() {
        return this.extensions;
    }

    @Override
    public String toString() {
        return "DefaultFeature{"
                + "enabled=" + enabled
                + ", extensions=" + extensions
                + '}';
    }
}
