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

import com.github.xdcrafts.flower.spring.impl.DefaultExtensionFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Map;

/**
 * ExtensionBeanDefinitionHandler.
 */
public class ExtensionBeanDefinitionHandler extends AbstractSingleBeanDefinitionParser {

    protected Class getBeanClass(Element element) {
        return DefaultExtensionFactory.class;
    }

    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        final Map<Object, Object> configuration = new ManagedMap<>();
        final Node keywordValueNode = element
            .getElementsByTagNameNS("http://xdcrafts.github.com/schema/flower", "keyword-value")
            .item(0);
        final Node predicateNode = element
            .getElementsByTagNameNS("http://xdcrafts.github.com/schema/flower", "predicate")
            .item(0);
        if (keywordValueNode != null) {
            configuration.put("keyword-value", keywordValueNode.getTextContent());
        }
        if (predicateNode != null) {
            configuration.put("predicate", new RuntimeBeanReference(predicateNode.getTextContent()));
        }
        bean
            .addPropertyReference("action", element.getAttribute("action"))
            .addPropertyValue("configuration", configuration);
    }
}
