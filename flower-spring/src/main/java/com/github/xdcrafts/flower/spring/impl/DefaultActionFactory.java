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

import com.github.xdcrafts.flower.core.DataFunctionExtractor;
import com.github.xdcrafts.flower.core.impl.DefaultAction;
import com.github.xdcrafts.flower.core.MethodConverter;
import com.github.xdcrafts.flower.core.impl.DefaultDataFunctionExtractor;
import org.springframework.context.ApplicationContext;

/**
 * Spring factory bean for default action that uses bean name as action name.
 */
@SuppressWarnings("unchecked")
public class DefaultActionFactory
    extends AbstractActionFactoryBean<DefaultAction> {

    private static final String SPLITTER = "::";

    private final String subject;
    private final String method;
    private DataFunctionExtractor dataFunctionExtractor;

    public DefaultActionFactory(String method) {
        final String[] subjectAndMethod = method.split(SPLITTER);
        if (subjectAndMethod.length != 2) {
            throw new IllegalArgumentException(
                "Invalid action declaration. Either "
                + "<qualified-class-name>::<method-name> or <bean-name>::<method-name> expected,"
            );
        }
        this.subject = subjectAndMethod[0];
        this.method = subjectAndMethod[1];
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.dataFunctionExtractor = new DefaultDataFunctionExtractor(
            applicationContext
                .getBeansOfType(MethodConverter.class, true, false).values()
        );
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultAction.class;
    }

    @Override
    protected DefaultAction createInstance() throws Exception {
        final Object classOrBean = this.getApplicationContext().containsBean(this.subject)
            ? this.getApplicationContext().getBean(this.subject)
            : Class.forName(this.subject);
        return new DefaultAction(
            getBeanName(),
            this.dataFunctionExtractor.apply(classOrBean, this.method),
            getMiddleware(getBeanName())
        );
    }
}
