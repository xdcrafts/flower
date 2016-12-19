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

package org.xdcrafts.flower.spring.impl.flows;

import org.xdcrafts.flower.core.Action;
import org.xdcrafts.flower.core.Middleware;
import org.xdcrafts.flower.core.impl.flows.BasicAsyncFlow;
import org.xdcrafts.flower.spring.AbstractActionFactoryBean;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Spring factory bean for basic async flow that uses bean name as action name.
 */
public class BasicAsyncFlowFactory extends AbstractActionFactoryBean<BasicAsyncFlow> {

    private final List<Action> flow;
    private final ExecutorService executorService;

    public BasicAsyncFlowFactory(List<Action> flow, ExecutorService executorService) {
        super(Collections.emptyList());
        this.flow = flow;
        this.executorService = executorService;
    }

    public BasicAsyncFlowFactory(List<Action> flow, ExecutorService executorService, List<Middleware> middlewares) {
        super(middlewares);
        this.flow = flow;
        this.executorService = executorService;
    }

    @Override
    public Class<?> getObjectType() {
        return BasicAsyncFlow.class;
    }

    @Override
    protected BasicAsyncFlow createInstance() throws Exception {
        return new BasicAsyncFlow(getBeanName(), this.flow, this.executorService, getMiddlewares());
    }
}
