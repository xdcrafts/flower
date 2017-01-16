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

package com.github.xdcrafts.flower.spring.impl;

import com.github.xdcrafts.flower.core.Extension;
import com.github.xdcrafts.flower.core.Selector;
import com.github.xdcrafts.flower.core.impl.DefaultFeature;

import java.util.Map;

/**
 * Spring factory bean for default feature that uses bean name as it's name..
 */
public class DefaultFeatureFactory extends AbstractNameAwareFactoryBean<DefaultFeature> {

    private final Map<Extension, Selector> extensions;

    public DefaultFeatureFactory(Map<Extension, Selector> extensions) {
        this.extensions = extensions;
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultFeature.class;
    }

    @Override
    protected DefaultFeature createInstance() throws Exception {
        return new DefaultFeature(getBeanName(), this.extensions);
    }
}
