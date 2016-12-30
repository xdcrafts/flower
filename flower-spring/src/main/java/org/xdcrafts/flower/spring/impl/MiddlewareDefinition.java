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

import java.util.Map;

/**
 * Definition of action to middleware mapping.
 */
public class MiddlewareDefinition {

    private final String namespace;
    private final Map<String, String> injections;

    public MiddlewareDefinition(String namespace, Map<String, String> injections) {
        this.namespace = namespace;
        this.injections = injections;
    }

    public String getNamespace() {
        return namespace;
    }

    public Map<String, String> getInjections() {
        return injections;
    }

    @Override
    public String toString() {
        return "MiddlewareDefinition{"
                + "namespace='" + namespace + '\''
                + ", injections=" + injections
                + '}';
    }
}
