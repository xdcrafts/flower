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

import com.github.xdcrafts.flower.core.impl.actions.DefaultAction;

/**
 * Spring factory bean for default action that uses bean name as action name.
 */
@SuppressWarnings("unchecked")
public class DefaultActionFactory
    extends AbstractActionFactoryBean<DefaultAction> {

    private final String method;

    public DefaultActionFactory(String method) {
        this.method = method;
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultAction.class;
    }

    @Override
    protected DefaultAction createInstance() throws Exception {
        return new DefaultAction(
            getBeanName(),
            resolveDataFunction(this.method),
            getMiddleware(getBeanName())
        );
    }
}
