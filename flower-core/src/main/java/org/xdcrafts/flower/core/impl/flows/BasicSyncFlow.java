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
import org.xdcrafts.flower.core.AsFunction;
import org.xdcrafts.flower.core.Flow;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Basic and straightforward implementation of Flow.
 */
public class BasicSyncFlow implements Flow {

    private final String name;
    private final List<Action> flow;
    private final Function<Map, Map> flowFunction;

    public BasicSyncFlow(String name, List<Action> flow) {
        this.name = name;
        this.flow = Collections.unmodifiableList(flow);
        this.flowFunction = flow
            .stream()
            .map(AsFunction::asFunction)
            .reduce(Function.identity(), Function::andThen);
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
        return this.flowFunction.apply(context);
    }

    @Override
    public String toString() {
        return "BasicSyncFlow{"
                + "name='" + this.name + '\''
                + ", actions=" + this.flow
                + '}';
    }
}
