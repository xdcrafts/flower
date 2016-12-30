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

package org.xdcrafts.flower.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.xdcrafts.flower.core.Middleware;

import java.util.Collections;
import java.util.List;

/**
 * Abstract bean factory that aware of bean name.
 * @param <T> class type
 */
public abstract class AbstractActionFactoryBean<T>
    extends AbstractFactoryBean<T>
    implements BeanNameAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private String beanName;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

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

    public List<Middleware> getMiddlewares() {
        return Collections.emptyList();
    }
}
