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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.xdcrafts.flower.tools.MapDsl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bean that resolve middleware assigned to action.
 */
public class MiddlewareResolver implements ApplicationContextAware {

    private static final String DELIMITER = ".";
    private static final String SPLITTER_REGEX = ",";

    /**
     * Configuration keywords.
     */
    private static final class Keywords {
        private static final String ROOT = ":root";
        private static final String THIS = ":this";
        private static final String MIDDLEWARE = ":middleware";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        final MapDsl.Mutable.MapOperator operator = MapDsl.Mutable.with(new HashMap());
        final Collection<MiddlewareDefinition> middlewareDefinitions = applicationContext
            .getBeansOfType(MiddlewareDefinition.class)
            .values();
        final Map<String, List<MiddlewareDefinition>> definitionsByName = middlewareDefinitions
            .stream()
            .collect(Collectors.groupingBy(MiddlewareDefinition::getNamespace));
        final Map<String, List<MiddlewareDefinition>> duplicatedDefinitions = definitionsByName
            .entrySet()
            .stream()
            .filter(e -> e.getValue().size() > 1)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (!duplicatedDefinitions.isEmpty()) {
            throw new IllegalStateException(
                "MiddlewareResolver: duplicated namespace declarations. " + duplicatedDefinitions
            );
        }
//        middlewareDefinitions
//            .stream()
//            .map(definition -> {
//                final String namespace = definition.getNamespace().equals(Keywords.ROOT)
//                    ? Keywords.ROOT
//                    : Keywords.ROOT + DELIMITER + definition.getNamespace();
//                definition.getInjections()
//            });
        System.err.println(operator.value());
    }
}
