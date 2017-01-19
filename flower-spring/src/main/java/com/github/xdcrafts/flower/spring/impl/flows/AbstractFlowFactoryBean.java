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

package com.github.xdcrafts.flower.spring.impl.flows;

import com.github.xdcrafts.flower.core.Action;
import com.github.xdcrafts.flower.core.Middleware;
import com.github.xdcrafts.flower.core.impl.actions.DefaultAction;
import com.github.xdcrafts.flower.spring.impl.AbstractActionFactoryBean;
import com.github.xdcrafts.flower.spring.impl.MiddlewareDefinition;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Abstract flow factory bean.
 * @param <T> bean type
 */
public abstract class AbstractFlowFactoryBean<T> extends AbstractActionFactoryBean<T> {

    private static final SecureRandom RANDOM = new SecureRandom();

    private MiddlewareDefinition middleware;

    public void setMiddleware(MiddlewareDefinition middleware) {
        this.middleware = middleware;
    }

    /**
     * Convert supplied object to of action if possible.
     */
    protected Action toAction(Object item) {
        if (item instanceof Action) {
            return (Action) item;
        } else if (item instanceof String) {
            final String definition = (String) item;
            final List<Middleware> definedMiddleware = Optional.ofNullable(middleware)
                .map(m -> m
                    .getDefinition()
                    .getOrDefault(definition, Collections.emptyList())
                )
                .orElse(Collections.emptyList());
            return new DefaultAction(
                definition + "@" + RANDOM.nextInt(),
                resolveDataFunction((String) item),
                definedMiddleware
            );
        } else {
            throw new IllegalArgumentException(
                "Can not convert " + item + " to Action. "
                + "Action or '<bean-or-class-name>::<method-name>' string expected."
            );
        }
    }
}
