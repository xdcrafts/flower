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
import org.xdcrafts.flower.core.Middleware;
import org.xdcrafts.flower.core.impl.extensions.PredicateExtension;
import org.xdcrafts.flower.spring.AbstractActionFactoryBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Spring factory bean for predicate extension that uses bean name as it's name.
 */
public class PredicateExtensionFactory extends AbstractActionFactoryBean<PredicateExtension> {

    private final Predicate<Map> predicate;
    private final Action action;

    public PredicateExtensionFactory(Predicate<Map> predicate, Action action) {
        super(Collections.emptyList());
        this.predicate = predicate;
        this.action = action;
    }

    public PredicateExtensionFactory(Predicate<Map> predicate, Action action, List<Middleware> middlewares) {
        super(middlewares);
        this.predicate = predicate;
        this.action = action;
    }

    @Override
    public Class<?> getObjectType() {
        return PredicateExtension.class;
    }

    @Override
    protected PredicateExtension createInstance() throws Exception {
        return new PredicateExtension(getBeanName(), this.action, this.predicate, getMiddlewares());
    }
}
