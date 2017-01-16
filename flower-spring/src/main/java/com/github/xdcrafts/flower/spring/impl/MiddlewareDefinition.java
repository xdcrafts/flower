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

package com.github.xdcrafts.flower.spring.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.github.xdcrafts.flower.core.Middleware;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Definition of action to middleware mapping.
 */
public class MiddlewareDefinition implements ApplicationContextAware {

    private static final String SPLITTER_REGEX = ",";

    private static List<String> split(String string) {
        return Arrays.stream(string.split(SPLITTER_REGEX)).map(String::trim).collect(Collectors.toList());
    }

    private final boolean shared;
    private final String namespace;
    private final Map<String, String> rawDefinition;
    private Map<String, List<Middleware>> definition;

    public MiddlewareDefinition(boolean shared, Map<String, String> definition) {
        this(shared, null, definition);
    }

    public MiddlewareDefinition(boolean shared, String namespace, Map<String, String> definition) {
        if (definition == null) {
            throw new IllegalArgumentException("'definition' can not be null.");
        }
        this.shared = shared;
        this.rawDefinition = definition;
        this.namespace = namespace;
    }

    public boolean isShared() {
        return shared;
    }

    public Map<String, List<Middleware>> getDefinition() {
        return definition;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        final Map<String, List<Middleware>> groupedInjections = this.rawDefinition
            .entrySet()
            .stream()
            .flatMap(e -> {
                final List<Middleware> middleware = split(e.getValue())
                    .stream()
                    .map(name -> applicationContext.getBean(name, Middleware.class))
                    .collect(Collectors.toList());
                return split(e.getKey())
                    .stream()
                    .map(name -> new AbstractMap.SimpleEntry<>(name, middleware));
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.definition = namespace == null || namespace.isEmpty()
            ? groupedInjections
            : groupedInjections
            .entrySet()
            .stream()
            .map(e -> new AbstractMap.SimpleEntry<>(namespace + "." + e.getKey(), e.getValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public String toString() {
        return "MiddlewareDefinition{"
                + "namespace='" + namespace + '\''
                + ", rawDefinition=" + rawDefinition
                + ", definition=" + definition
                + ", shared=" + shared
                + '}';
    }
}
