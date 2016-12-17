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

package org.xdcrafts.flower.core.impl.flows;

import org.xdcrafts.flower.core.Action;
import org.xdcrafts.flower.core.Flow;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static org.xdcrafts.flower.core.utils.MapApi.Mutable.assoc;

/**
 * Basic implementation of asynchronous flow.
 */
public class BasicAsyncFlow implements Flow {

    private final String name;
    private final List<Action> flow;
    private final ExecutorService executorService;

    public BasicAsyncFlow(String name, List<Action> flow, ExecutorService executorService) {
        this.name = name;
        this.flow = Collections.unmodifiableList(flow);
        this.executorService = executorService;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Action> actions() {
        return flow;
    }

    @Override
    public Map apply(Map context) {
        final CompletableFuture<Map> completion = flow.stream().reduce(
            CompletableFuture.completedFuture(context),
            (future, action) -> future.thenApplyAsync(action, this.executorService),
            (left, right) -> right
        );
        return assoc(context, "meta", "completion", completion);
    }

    @Override
    public String toString() {
        return "BasicAsyncFlow{"
                + "name='" + this.name + '\''
                + ", actions=" + this.flow
                + ", executorService=" + this.executorService
                + '}';
    }
}
