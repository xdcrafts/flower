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

import com.github.xdcrafts.flower.spring.impl.flows.SyncFlowFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * SyncFlowBeanDefinitionHandler.
 */
public class SyncFlowBeanDefinitionHandler extends AbstractSingleBeanDefinitionParser {

    protected Class getBeanClass(Element element) {
        return SyncFlowFactory.class;
    }

    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        final ManagedList<Object> actions = new ManagedList<>();
        final NodeList actionNodes = element.getElementsByTagName("*");
        if (actionNodes != null && actionNodes.getLength() != 0) {
            for (int i = 0; i < actionNodes.getLength(); i++) {
                final Node node = actionNodes.item(i);
                final String type = node.getLocalName();
                if (type.equalsIgnoreCase("method")) {
                    actions.add(node.getTextContent());
                } else if (type.equalsIgnoreCase("action")) {
                    actions.add(new RuntimeBeanReference(node.getTextContent()));
                } else {
                    throw new IllegalArgumentException("Unknown element type: " + type);
                }
            }
        }
        bean.addPropertyValue("actions", actions);
    }
}
