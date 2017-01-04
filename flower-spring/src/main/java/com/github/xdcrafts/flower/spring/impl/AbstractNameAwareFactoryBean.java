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

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Abstract bean factory that aware of bean name.
 * @param <T> bean type
 */
public abstract class AbstractNameAwareFactoryBean<T>
    extends AbstractFactoryBean<T>
    implements BeanNameAware {

    private String beanName;

    @Override
    public void setBeanName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name can not be null or empty string!");
        }
        this.beanName = name;
    }

    public String getBeanName() {
        return this.beanName;
    }
}
