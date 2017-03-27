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

import com.github.xdcrafts.flower.spring.impl.selectors.KeywordSelectorFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * KeywordSelectorBeanDefinitionHandler.
 */
public class KeywordSelectorBeanDefinitionHandler extends AbstractSingleBeanDefinitionParser {

    protected Class getBeanClass(Element element) {
        return KeywordSelectorFactory.class;
    }

    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        final String keyword = element.getAttribute("keyword");
        bean.addPropertyValue("keyword", keyword);
        final String required = element.getAttribute("required");
        if (required != null && !required.isEmpty()) {
            bean.addPropertyValue("required", Boolean.valueOf(required));
        }
    }
}
