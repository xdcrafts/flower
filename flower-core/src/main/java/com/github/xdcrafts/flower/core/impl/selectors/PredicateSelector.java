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

import com.github.xdcrafts.flower.core.Action;
import com.github.xdcrafts.flower.core.Extension;
import com.github.xdcrafts.flower.core.Middleware;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.github.xdcrafts.flower.tools.MapApi.get;

/**
 * Implementation of Selector that selects Action based on predicates.
 */
@SuppressWarnings("unchecked")
public class PredicateSelector extends WithMiddlewareSelectorBase {

    /**
     * Class with configuration keys.
     */
    public static final class ConfigurationKeys {
        public static final String PREDICATE = "predicate";
    }

    private final String name;
    private final List<Extension> extensions;
    private final List<Predicate<Map>> predicates;
    private final Map<Predicate, Action> actionsMapping;

    public PredicateSelector(String name, List<Extension> extensions) {
        this(name, extensions, Collections.emptyList());
    }

    public PredicateSelector(String name, List<Extension> extensions, List<Middleware> middleware) {
        super(middleware);
        this.name = name;
        this.extensions = extensions;
        this.actionsMapping = new HashMap<>();
        this.predicates = new ArrayList<>();
        for (Extension extension : extensions) {
            final Map configuration = extension.configuration();
            final Predicate predicate =
                get(configuration, Predicate.class, ConfigurationKeys.PREDICATE)
                .orElseThrow(() -> new IllegalArgumentException(
                    extension + ": '" + ConfigurationKeys.PREDICATE + "' key required."
                ));
            predicates.add(predicate);
            actionsMapping.put(predicate, extension.action());
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
        for (Predicate<Map> predicate: this.predicates) {
            if (predicate.test(context)) {
                return this.actionsMapping.get(predicate);
            }
        }
        throw new IllegalArgumentException("Unable to selectAction request, no suitable action found.");
    }

    @Override
    public String toString() {
        return "PredicateSelector{"
                + "name='" + this.name + '\''
                + ", extensions=" + extensions
                + ", predicates=" + predicates
                + ", actionsMapping=" + actionsMapping
                + '}';
    }
}
