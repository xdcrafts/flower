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

import com.github.xdcrafts.flower.core.Core;
import com.github.xdcrafts.flower.core.Extension;
import com.github.xdcrafts.flower.core.Action;
import com.github.xdcrafts.flower.core.Middleware;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.github.xdcrafts.flower.tools.map.MapDotApi.dotGet;
import static com.github.xdcrafts.flower.tools.map.MapDotApi.dotGetString;

/**
 * Implementation of Selector that selects Action based on value of keyword in context.
 */
@SuppressWarnings("unchecked")
public class KeywordSelector extends WithMiddlewareSelectorBase {

    /**
     * Class with configuration keys.
     */
    public static final class ConfigurationKeys {
        public static final String KEYWORD_VALUE = "keyword-value";
    }

    private final String name;
    private final String keyword;
    private final Map<String, Extension> extensions;

    public KeywordSelector(String name, String keyword) {
        this(name, keyword, Collections.emptyList());
    }

    public KeywordSelector(String name, String keyword, List<Middleware> middleware) {
        super(middleware);
        this.name = name;
        this.keyword = keyword;
        this.extensions = new ConcurrentHashMap<>();
        this.meta.put(Core.ActionMeta.NAME, name);
        this.meta.put(Core.ActionMeta.TYPE, getClass().getName());
        this.meta.put(Core.ActionMeta.MIDDLEWARE, middleware);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Collection<Extension> extensions() {
        return this.extensions.values();
    }

    @Override
    public List<Action> selectAction(Map context) {
        final Object keywordValue = dotGet(context, this.keyword).orElseThrow(
            () -> new IllegalArgumentException(
                "Unable to selectAction request, '" + this.keyword + "' key required"
            ));
        final Collection<String> keywordValues;
        if (keywordValue instanceof String) {
            keywordValues = Collections.singletonList((String) keywordValue);
        } else if (keywordValue instanceof Collection) {
            keywordValues = (Collection<String>) keywordValue;
        } else {
            throw new IllegalArgumentException("'" + this.keyword + "' should be a String or Collection<String>.");
        }
        final List<Action> actions = keywordValues
            .stream()
            .map(value -> Optional.ofNullable(this.extensions.get(value)))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        if (actions.isEmpty()) {
            throw new IllegalArgumentException(
                "Unable to selectAction request, '" + keywordValue + "' is unknown keyword value."
            );
        }
        return actions;
    }

    @Override
    public void register(Extension extension) {
        final Map configuration = extension.configuration();
        final String keywordValue = dotGetString(configuration, ConfigurationKeys.KEYWORD_VALUE)
            .orElseThrow(() -> new IllegalArgumentException(
                extension + ": '" + ConfigurationKeys.KEYWORD_VALUE + "' key required."
            ));
        if (this.extensions.containsKey(keywordValue)) {
            throw new IllegalArgumentException("'" + keywordValue + "' already registered!");
        }
        this.extensions.put(keywordValue, extension);
    }

    @Override
    public String toString() {
        return "KeywordSelector{"
                + "name='" + this.name + '\''
                + ", keyword=" + this.keyword
                + ", extensions=" + extensions
                + ", extensions=" + extensions
                + '}';
    }
}
