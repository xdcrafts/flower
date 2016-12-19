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

package org.xdcrafts.flower.core.impl.extensions;

import org.xdcrafts.flower.core.Action;
import org.xdcrafts.flower.core.Extension;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static org.xdcrafts.flower.tools.MapDsl.Mutable.with;

/**
 * Predicate-based implementation of Extension.
 */
public class PredicateExtension implements Extension {

    /**
     * Class with configuration keys.
     */
    public static final class ConfigurationKeys {
        public static final String PREDICATE = "predicate";
    }

    private final String name;
    private final Action action;
    private final Map configuration;

    public PredicateExtension(String name, Action action, Predicate<Map> predicate) {
        this.name = name;
        this.action = action;
        this.configuration = with(new HashMap())
            .assoc(ConfigurationKeys.PREDICATE, predicate)
            .value();
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
        return "PredicateExtension{"
                + "name='" + this.name + '\''
                + ", action=" + this.action
                + ", configuration=" + this.configuration
                + '}';
    }
}
