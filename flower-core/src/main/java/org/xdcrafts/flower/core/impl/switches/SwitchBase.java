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

package org.xdcrafts.flower.core.impl.switches;

import org.xdcrafts.flower.core.Middleware;
import org.xdcrafts.flower.core.Switch;
import org.xdcrafts.flower.core.impl.ActionBase;

import java.util.List;
import java.util.Map;

/**
 * Abstract class as a base for any Switch implementation.
 */
public abstract class SwitchBase extends ActionBase implements Switch {

    public SwitchBase(List<Middleware> middlewares) {
        super(middlewares);
    }

    @Override
    public Map act(Map ctx) {
        return selectAction(ctx).apply(ctx);
    }
}
