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
import org.xdcrafts.flower.core.Middleware;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.xdcrafts.flower.tools.MapDsl.Mutable.with;

/**
 * Keyword-based implementation of selectAction.
 */
public class KeywordExtension extends MiddlewaredExtensionBase {

    /**
     * Class with configuration keys.
     */
    public static final class ConfigurationKeys {
        public static final String KEYWORD_VALUE = "keyword-value";
    }

    private final String name;
    private final Map configuration;
    private final Action action;

    public KeywordExtension(String name, String keywordValue, Action action) {
        this(name, keywordValue, action, Collections.emptyList());
    }

    public KeywordExtension(String name, String keywordValue, Action action, List<Middleware> middlewares) {
        super(middlewares);
        this.name = name;
        this.configuration = with(new HashMap())
            .assoc(ConfigurationKeys.KEYWORD_VALUE, keywordValue)
            .value();
        this.action = action;
        this.meta.put("name", name);
        this.meta.put("class", getClass().getName());
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
        return "KeywordExtension{"
                + "name='" + this.name + '\''
                + ", action=" + this.action
                + ", configuration=" + this.configuration
                + '}';
    }
}
