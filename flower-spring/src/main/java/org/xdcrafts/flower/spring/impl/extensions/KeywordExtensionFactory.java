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

package org.xdcrafts.flower.spring.impl.extensions;

import org.xdcrafts.flower.core.Action;
import org.xdcrafts.flower.core.impl.extensions.KeywordExtension;
import org.xdcrafts.flower.spring.AbstractBeanNameAwareFactoryBean;

/**
 * Spring factory bean for keyword extension that uses bean name as it's name.
 */
public class KeywordExtensionFactory extends AbstractBeanNameAwareFactoryBean<KeywordExtension> {

    private final String keywordValue;
    private final Action action;

    public KeywordExtensionFactory(String keywordValue, Action action) {
        this.keywordValue = keywordValue;
        this.action = action;
    }

    @Override
    public Class<?> getObjectType() {
        return KeywordExtension.class;
    }

    @Override
    protected KeywordExtension createInstance() throws Exception {
        return new KeywordExtension(getBeanName(), this.keywordValue, this.action);
    }
}
