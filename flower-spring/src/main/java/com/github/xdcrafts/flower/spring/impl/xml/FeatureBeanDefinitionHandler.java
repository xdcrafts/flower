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

package com.github.xdcrafts.flower.spring.impl.xml;

import com.github.xdcrafts.flower.spring.impl.DefaultFeatureFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * FeatureBeanDefinitionHandler.
 */
public class FeatureBeanDefinitionHandler extends AbstractSingleBeanDefinitionParser {

    protected Class getBeanClass(Element element) {
        return DefaultFeatureFactory.class;
    }

    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        final ManagedMap<Object, Object> extensions = new ManagedMap<>();
        final NodeList bindingNodes = element
            .getElementsByTagNameNS("http://xdcrafts.github.com/schema/flower", "binding");
        if (bindingNodes != null && bindingNodes.getLength() != 0) {
            for (int i = 0; i < bindingNodes.getLength(); i++) {
                final Node bindingNode = bindingNodes.item(i);
                final String extension = bindingNode
                    .getAttributes()
                    .getNamedItem("extension")
                    .getNodeValue();
                final String selector = bindingNode
                    .getAttributes()
                    .getNamedItem("selector")
                    .getNodeValue();
                extensions.put(new RuntimeBeanReference(extension), new RuntimeBeanReference(selector));
            }
        }
        bean.addPropertyValue("extensions", extensions);
    }
}
