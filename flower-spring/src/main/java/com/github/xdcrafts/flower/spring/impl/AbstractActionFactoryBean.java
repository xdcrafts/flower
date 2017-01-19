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

import com.github.xdcrafts.flower.core.DataFunctionExtractor;
import com.github.xdcrafts.flower.core.MethodConverter;
import com.github.xdcrafts.flower.core.impl.actions.DefaultDataFunctionExtractor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.github.xdcrafts.flower.core.Middleware;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Abstract action bean factory that aware of bean name.
 * @param <T> class type
 */
public abstract class AbstractActionFactoryBean<T>
    extends AbstractNameAwareFactoryBean<T>
    implements ApplicationContextAware {

    protected static final String DEFAULT_FUNCTION = "apply";
    protected static final String SPLITTER = "::";

    private ApplicationContext applicationContext;
    private DataFunctionExtractor dataFunctionExtractor;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.dataFunctionExtractor = new DefaultDataFunctionExtractor(
            applicationContext
                .getBeansOfType(MethodConverter.class, true, false).values()
        );
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public DataFunctionExtractor getDataFunctionExtractor() {
        return dataFunctionExtractor;
    }

    protected Function<Map, Map> resolveDataFunction(String definition) {
        final String subject;
        final String method;
        if (definition.contains(SPLITTER)) {
            final String[] subjectAndMethod = definition.split(SPLITTER);
            if (subjectAndMethod.length != 2) {
                throw new IllegalArgumentException(
                    "Invalid action declaration: <class-or-bean-name>::<method-name> expected."
                );
            }
            subject = subjectAndMethod[0];
            method = subjectAndMethod[1];
        } else {
            subject = definition;
            method = DEFAULT_FUNCTION;
        }
        final Object classOrBean;
        try {
            classOrBean = this.getApplicationContext().containsBean(subject)
                ? this.getApplicationContext().getBean(subject)
                : Class.forName(subject);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        return dataFunctionExtractor.apply(classOrBean, method);
    }

    /**
     * Resolves middleware assigned to action by it's name.
     */
    protected List<Middleware> getMiddleware(String name) {
        final List<Middleware> middleware = this.applicationContext
            .getBeansOfType(MiddlewareDefinition.class, true, false)
            .values()
            .stream()
            .filter(MiddlewareDefinition::isShared)
            .flatMap(d -> d.getDefinition().entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            .get(name);
        return middleware == null ? Collections.emptyList() : middleware;
    }
}
