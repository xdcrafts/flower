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

package org.xdcrafts.flower.spring.impl;

import org.xdcrafts.flower.core.impl.DefaultAction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.function.Function;

/**
 * Spring factory bean for default action that uses bean name as action name.
 */
public class DefaultActionFactory
    extends AbstractActionFactoryBean<DefaultAction> {

    private static final String SPLITTER = "::";

    private final String subject;
    private final String method;

    public DefaultActionFactory(String method) {
        final String[] subjectAndMethod = method.split(SPLITTER);
        if (subjectAndMethod.length != 2) {
            throw new IllegalArgumentException();
        }
        this.subject = subjectAndMethod[0];
        this.method = subjectAndMethod[1];
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultAction.class;
    }

    @Override
    protected DefaultAction createInstance() throws Exception {
        return new DefaultAction(
            getBeanName(),
            buildActionFunction(this.subject, this.method),
            getMiddleware(getBeanName())
        );
    }


    private static Map safeVirtualInvoke(MethodHandle methodHandle, Object bean, Map context) {
        try {
            return (Map) methodHandle.invoke(bean, context);
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RuntimeException(throwable);
        }
    }

    private static Map safeStaticInvoke(MethodHandle methodHandle, Map context) {
        try {
            return (Map) methodHandle.invoke(context);
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RuntimeException(throwable);
        }
    }

    private Function<Map, Map> buildActionFunction(
        String classOrBeanName, String methodName
    ) {
        try {
            final Object bean = this.getApplicationContext().containsBean(classOrBeanName)
                ? this.getApplicationContext().getBean(classOrBeanName)
                : null;
            final boolean isVirtual = bean != null;
            final Class clazz = isVirtual ? bean.getClass() : Class.forName(classOrBeanName);
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final MethodType methodType = MethodType.methodType(Map.class, Map.class);
            final MethodHandle methodHandle = isVirtual
                ? lookup.findVirtual(clazz, methodName, methodType)
                : lookup.findStatic(clazz, methodName, methodType);
            return isVirtual
                ? ctx -> safeVirtualInvoke(methodHandle, bean, ctx)
                : ctx -> safeStaticInvoke(methodHandle, ctx);
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RuntimeException(throwable);
        }
    }
}
