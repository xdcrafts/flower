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

package org.xdcrafts.flower.spring.impl.switches;

import org.xdcrafts.flower.core.Middleware;
import org.xdcrafts.flower.core.impl.switches.KeywordSwitch;

import java.util.Collections;
import java.util.List;

/**
 * KeywordSwitch factory bean.
 */
public class KeywordSwitchFactory extends AbstractSwitchFactoryBean<KeywordSwitch> {

    private final String keyword;

    public KeywordSwitchFactory(String keyword) {
        super(Collections.emptyList());
        this.keyword = keyword;
    }

    public KeywordSwitchFactory(String keyword, List<Middleware> middlewares) {
        super(middlewares);
        this.keyword = keyword;
    }

    @Override
    public Class<?> getObjectType() {
        return KeywordSwitch.class;
    }

    @Override
    protected KeywordSwitch createInstance() throws Exception {
        return new KeywordSwitch(getBeanName(), this.keyword, getRoutes(getBeanName()), getMiddlewares());
    }
}
