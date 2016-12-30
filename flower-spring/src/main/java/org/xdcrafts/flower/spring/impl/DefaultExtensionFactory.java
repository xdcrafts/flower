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

import org.xdcrafts.flower.core.Action;
import org.xdcrafts.flower.core.impl.extensions.DefaultExtension;
import org.xdcrafts.flower.spring.AbstractActionFactoryBean;

import java.util.Map;

/**
 * Spring factory bean for default extension that uses bean name as it's name.
 */
public class DefaultExtensionFactory extends AbstractActionFactoryBean<DefaultExtension> {

    private final Action action;
    private final Map configuration;

    public DefaultExtensionFactory(Action action, Map configuration) {
        this.action = action;
        this.configuration = configuration;
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultExtension.class;
    }

    @Override
    protected DefaultExtension createInstance() throws Exception {
        return new DefaultExtension(getBeanName(), this.action, this.configuration, getMiddleware(getBeanName()));
    }
}
