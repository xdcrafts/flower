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

package com.github.xdcrafts.flower.core.impl.selectors;

import com.github.xdcrafts.flower.core.Extension;
import com.github.xdcrafts.flower.core.Action;
import com.github.xdcrafts.flower.core.Middleware;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.xdcrafts.flower.tools.MapApi.DotNotation.dotGetString;

/**
 * Implementation of Selector that selects Action based on value of keyword in context.
 */
public class KeywordSelector extends WithMiddlewareSelectorBase {

    /**
     * Class with configuration keys.
     */
    public static final class ConfigurationKeys {
        public static final String KEYWORD_VALUE = "keyword-value";
    }

    private final String name;
    private final String keyword;
    private final List<Extension> extensions;
    private final Map<String, Action> actionsMapping;

    public KeywordSelector(String name, String keyword, List<Extension> extensions) {
        this(name, keyword, extensions, Collections.emptyList());
    }

    public KeywordSelector(String name, String keyword, List<Extension> extensions, List<Middleware> middleware) {
        super(middleware);
        this.name = name;
        this.keyword = keyword;
        this.extensions = extensions;
        this.actionsMapping = new HashMap<>();
        for (Extension extension : extensions) {
            final Map configuration = extension.configuration();
            final String keywordValue = dotGetString(configuration, ConfigurationKeys.KEYWORD_VALUE)
                .orElseThrow(() -> new IllegalArgumentException(
                    extension + ": '" + ConfigurationKeys.KEYWORD_VALUE + "' key required."
                ));
            actionsMapping.put(keywordValue, extension.action());
        }
        this.meta.put("name", name);
        this.meta.put("class", getClass().getName());
        this.meta.put("middleware", middleware);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Extension> extensions() {
        return this.extensions;
    }

    @Override
    public Action selectAction(Map context) {
        final String keywordValue = dotGetString(context, this.keyword).orElseThrow(
            () -> new IllegalArgumentException(
                "Unable to selectAction request, '" + this.keyword + "' key required"
            ));
        final Action action = this.actionsMapping.get(keywordValue);
        if (action == null) {
            throw new IllegalArgumentException(
                "Unable to selectAction request, '" + keywordValue + "' is unknown keyword value."
            );
        }
        return action;
    }

    @Override
    public String toString() {
        return "KeywordSelector{"
                + "name='" + this.name + '\''
                + ", keyword=" + this.keyword
                + ", extensions=" + extensions
                + ", actionsMapping=" + actionsMapping
                + '}';
    }
}
