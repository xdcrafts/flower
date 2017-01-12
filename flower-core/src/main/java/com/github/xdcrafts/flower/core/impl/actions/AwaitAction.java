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

package com.github.xdcrafts.flower.core.impl.actions;

import com.github.xdcrafts.flower.core.Action;
import com.github.xdcrafts.flower.core.Core;
import com.github.xdcrafts.flower.tools.MapApi;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Awaits completion of async expectation.
 */
public class AwaitAction implements Action {

    private final String name;
    private final int timeout;
    private final TimeUnit timeUnit;

    public AwaitAction(String name, int timeout) {
        this(name, timeout, TimeUnit.MILLISECONDS);
    }

    public AwaitAction(String name, int timeout, TimeUnit timeUnit) {
        this.name = name;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Map apply(Map map) {
        return MapApi.DotNotation.dotGet(map, CompletableFuture.class, Core.FlowMeta.EXPECTATION)
            .map(f -> {
                try {
                    return (Map) f.get(this.timeout, this.timeUnit);
                } catch (ExecutionException | InterruptedException | TimeoutException e) {
                    throw new RuntimeException(e);
                }
            })
            .orElse(map);
    }

    @Override
    public String toString() {
        return "AwaitAction{"
                + "name='" + name + '\''
                + ", timeout=" + timeout
                + ", timeUnit=" + timeUnit
                + '}';
    }
}
