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

package com.github.xdcrafts.flower.core.impl.flows;

import com.github.xdcrafts.flower.core.Action;
import com.github.xdcrafts.flower.core.Core;
import com.github.xdcrafts.flower.core.Middleware;
import com.github.xdcrafts.flower.core.Flow;
import com.github.xdcrafts.flower.core.impl.actions.WithMiddlewareActionBase;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.github.xdcrafts.flower.tools.map.MapDotApi.dotAssoc;
import static com.github.xdcrafts.flower.tools.map.MapDotApi.dotGet;
import static com.github.xdcrafts.flower.tools.map.MapDotApi.dotGetUnsafe;

/**
 * Basic implementation of serial asynchronous flow.
 */
@SuppressWarnings("unchecked")
public class AsyncFlow extends WithMiddlewareActionBase implements Flow {

    public static final String EXECUTOR_SERVICE = "executorService";

    private final String name;
    private final List<Action> actions;
    private final Map configuration;
    private final ExecutorService executorService;

    public AsyncFlow(
        String name, List<Action> flow, Map configuration
    ) {
        this(name, flow, configuration, Collections.emptyList());
    }

    public AsyncFlow(
        String name, List<Action> actions, Map configuration, List<Middleware> middleware
    ) {
        super(middleware);
        this.name = name;
        this.actions = Collections.unmodifiableList(actions);
        this.configuration = configuration;
        this.executorService = dotGetUnsafe(configuration, ExecutorService.class, EXECUTOR_SERVICE);
        this.meta.put(Core.ActionMeta.NAME, name);
        this.meta.put(Core.ActionMeta.TYPE, getClass().getName());
        this.meta.put(Core.ActionMeta.MIDDLEWARE, middleware);
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
        CompletableFuture<Map> expectation = dotGet(
            context, CompletableFuture.class, Core.FlowMeta.EXPECTATION
        ).orElse(CompletableFuture.completedFuture(context));
        for (Action action : this.actions) {
            expectation = expectation.thenApplyAsync(action, this.executorService);
        }
        return dotAssoc(context, Core.FlowMeta.EXPECTATION, expectation);
    }

    @Override
    public String toString() {
        return "AsyncFlow{"
                + "name='" + this.name + '\''
                + ", actions=" + this.actions
                + ", configuration=" + this.configuration
                + '}';
    }
}
