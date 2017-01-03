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

package org.xdcrafts.flower.core;

import org.xdcrafts.flower.core.impl.extensions.DefaultExtension;

import java.util.Map;

/**
 * Extension is an action with configuration that defines conditions when this selectAction should be chosen.
 */
public interface Extension extends Action {

    /**
     * Creates default implementation of selectAction.
     * @param name extension's name
     * @param action extension's action
     * @param configuration extension's configuration
     * @return default selectAction
     */
    static Extension route(String name, Action action, Map configuration) {
        return new DefaultExtension(name, action, configuration);
    }

    /**
     * Action that will process data.
     */
    Action action();

    /**
     * Conditions when this selectAction should be chosen.
     * Contains data specific for concrete selector implementation.
     */
    Map configuration();
}
