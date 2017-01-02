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
import org.xdcrafts.flower.core.Middleware;
import org.xdcrafts.flower.core.impl.WithMiddlewareActionBase;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static org.xdcrafts.flower.tools.MapApi.Mutable.assoc;

/**
 * Basic implementation of asynchronous flow.
 */
public class BasicAsyncFlow extends WithMiddlewareActionBase implements Flow {

    private final String name;
    private final List<Action> actions;
    private final ExecutorService executorService;

    public BasicAsyncFlow(
        String name, List<Action> flow, ExecutorService executorService
    ) {
        this(name, flow, executorService, Collections.emptyList());
    }

    public BasicAsyncFlow(
        String name, List<Action> actions, ExecutorService executorService, List<Middleware> middleware
    ) {
        super(middleware);
        this.name = name;
        this.actions = Collections.unmodifiableList(actions);
        this.executorService = executorService;
        this.meta.put("name", name);
        this.meta.put("class", getClass().getName());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Action> actions() {
        return this.actions;
    }

    @Override
    public Map act(Map context) {
        final CompletableFuture<Map> completion = actions.stream().reduce(
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
                + ", actions=" + this.actions
                + ", executorService=" + this.executorService
                + '}';
    }
}
