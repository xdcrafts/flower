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
import com.github.xdcrafts.flower.core.impl.flows.BasicSyncFlow;
import com.github.xdcrafts.flower.spring.impl.AbstractActionFactoryBean;

import java.util.List;

/**
 * Spring factory bean for basic sync actions that uses bean name as it's name.
 */
public class BasicSyncFlowFactory extends AbstractActionFactoryBean<BasicSyncFlow> {

    private List<Action> actions;

    public BasicSyncFlowFactory(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public Class<?> getObjectType() {
        return BasicSyncFlow.class;
    }

    @Override
    protected BasicSyncFlow createInstance() throws Exception {
        return new BasicSyncFlow(getBeanName(), this.actions, getMiddleware(getBeanName()));
    }
}
