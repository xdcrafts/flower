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

import com.github.xdcrafts.flower.core.impl.flows.AsyncFlow;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Spring factory bean for basic async actions that uses bean name as action name.
 */
public class AsyncFlowFactory extends AbstractFlowFactoryBean<AsyncFlow> {

    private List<Object> actions;
    private Map configuration;

    public void setActions(List<Object> actions) {
        this.actions = actions;
    }

    @Required
    public void setConfiguration(Map configuration) {
        this.configuration = configuration;
    }

    @Override
    public Class<?> getObjectType() {
        return AsyncFlow.class;
    }

    @Override
    protected AsyncFlow createInstance() throws Exception {
        return new AsyncFlow(
            getBeanName(),
            this.actions.stream().map(this::toAction).collect(Collectors.toList()),
            this.configuration, getMiddleware(getBeanName())
        );
    }
}
