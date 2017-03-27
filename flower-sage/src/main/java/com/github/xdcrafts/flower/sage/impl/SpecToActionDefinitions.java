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

package com.github.xdcrafts.flower.sage.impl;

import com.github.xdcrafts.flower.sage.Spec;

import java.util.Map;

/**
 * Validation definition class.
 */
public class SpecToActionDefinitions {

    private final Map<Spec, String> specActions;

    public SpecToActionDefinitions(Map<Spec, String> specActions) {
        this.specActions = specActions;
    }

    public Map<Spec, String> getSpecActions() {
        return specActions;
    }

    @Override
    public String toString() {
        return "SpecToActionDefinitions{"
                + "specActions=" + specActions
                + '}';
    }
}
