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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import com.github.xdcrafts.flower.tools.Named;

import java.util.Map;

/**
 * Factory that defines set of default actions.
 */
public class DefaultActionDefinitionFactory implements BeanDefinitionRegistryPostProcessor {

    private final String namespace;
    private final Map<String, Object> actions;

    public DefaultActionDefinitionFactory(Map<String, Object> actions) {
        this.namespace = null;
        this.actions = actions;
    }

    public DefaultActionDefinitionFactory(String namespace, Map<String, Object> actions) {
        this.namespace = namespace;
        this.actions = actions;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        actions.entrySet().forEach(entry -> {
            final String name = entry.getKey();
            final String qualifiedName = Named.qualifiedName(this.namespace, name);
            final Object method = entry.getValue();
            final ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
            constructorArgumentValues.addGenericArgumentValue(method);
            final GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(DefaultActionFactory.class);
            beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
            registry.registerBeanDefinition(qualifiedName, beanDefinition);
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Nothing
    }
}
