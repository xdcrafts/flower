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
import org.xdcrafts.flower.spring.AbstractActionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class for switches factory beans.
 * @param <T>
 */
public abstract class AbstractSwitchFactoryBean<T>
    extends AbstractActionFactoryBean<T> implements ApplicationContextAware {

    private static final String SWITCH = "switch";

    private ApplicationContext applicationContext;

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
    protected List<Extension> fetchExtensions(String name) {
        return this.applicationContext
            .getBeansOfType(Extension.class, true, false)
            .values()
            .stream()
            .filter(e -> {
                if (!e.configuration().containsKey(SWITCH)) {
                    throw new IllegalStateException(
                        "Expression '" + e.getName()
                        + "' configuration: required key" + SWITCH + " not found. " + e.configuration()
                    );
                }
                return e.configuration().get(SWITCH).toString().equals(name);
            }).collect(Collectors.toList());
    }
}
