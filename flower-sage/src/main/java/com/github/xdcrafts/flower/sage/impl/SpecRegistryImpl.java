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

package com.github.xdcrafts.flower.sage.impl;

import com.github.xdcrafts.flower.sage.Spec;
import com.github.xdcrafts.flower.sage.SpecApi;
import com.github.xdcrafts.flower.sage.SpecRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Validation registry class.
 */
public class SpecRegistryImpl implements SpecRegistry {

    private final Map<String, Spec> specs;

    public SpecRegistryImpl(Collection<SpecToActionDefinitions> specToActionDefinitionsList) {
        final Map<String, List<Spec>> merged = new HashMap<>();
        for (SpecToActionDefinitions definition : specToActionDefinitionsList) {
            for (Map.Entry<Spec, String> entry : definition.getSpecActions().entrySet()) {
                final String keyword = entry.getValue();
                final Spec entrySpec = entry.getKey();
                if (merged.containsKey(keyword)) {
                    merged.get(keyword).add(entrySpec);
                } else {
                    final List<Spec> actionSpecs = new ArrayList<>();
                    actionSpecs.add(entrySpec);
                    merged.put(keyword, actionSpecs);
                }
            }
        }
        this.specs = merged
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> SpecApi.greedyAnd(e.getValue())));
    }

    public SpecRegistryImpl(Map<String, Spec> specs) {
        this.specs = specs;
    }

    @Override
    public Optional<Spec> get(String keyword) {
        return Optional.ofNullable(this.specs.get(keyword));
    }

    @Override
    public String toString() {
        return "SpecRegistryImpl{"
                + "specs=" + specs
                + '}';
    }
}
