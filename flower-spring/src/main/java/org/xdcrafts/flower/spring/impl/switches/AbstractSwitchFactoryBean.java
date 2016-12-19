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

package org.xdcrafts.flower.spring.impl.switches;

import org.xdcrafts.flower.core.Extension;
import org.xdcrafts.flower.core.Middleware;
import org.xdcrafts.flower.spring.AbstractActionFactoryBean;
import org.xdcrafts.flower.spring.Feature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Abstract class for switches factory beans.
 * @param <T>
 */
public abstract class AbstractSwitchFactoryBean<T>
    extends AbstractActionFactoryBean<T> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public AbstractSwitchFactoryBean(List<Middleware> middlewares) {
        super(middlewares);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    /**
     * Scan application context and find all extensions within bundles that should be attached to router with full name.
     */
    public List<Extension> getRoutes(String fullRouterName) {
        return this.applicationContext
            .getBeansOfType(Feature.class, true, false)
            .values()
            .stream()
            .filter(Feature::isEnabled)
            .map(Feature::extensions)
            .flatMap(m -> m.entrySet().stream())
            .filter(e -> e.getValue().equals(fullRouterName))
            .map(Map.Entry::getKey)
            .map(name -> applicationContext.getBean(name, Extension.class))
            .collect(Collectors.toList());
    }
}
