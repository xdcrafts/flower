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

package com.github.xdcrafts.flower.core.impl;

import com.github.xdcrafts.flower.core.Action;
import com.github.xdcrafts.flower.core.Actor;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Default implementation of Actor.
 * @param <T> return type;
 */
public class DefaultActor<T> implements Actor<T> {

    private final Supplier<Map> contextFactory;
    private final Action action;
    private final Function<Map, T> conclusion;
    private final Function<Map, T> body;

    public DefaultActor(
        Supplier<Map> contextFactory,
        Action action,
        Function<Map, T> conclusion
    ) {
        if (contextFactory == null) {
            throw new IllegalArgumentException("ContextFactory can not be null.");
        }
        if (action == null) {
            throw new IllegalArgumentException("Action can not be null.");
        }
        if (conclusion == null) {
            throw new IllegalArgumentException("Conclusion can not be null.");
        }
        this.contextFactory = contextFactory;
        this.action = action;
        this.conclusion = conclusion;
        this.body = this.action.andThen(this.conclusion);
    }

    @Override
    public T apply(Consumer<Map> contextInitializer) {
        final Map ctx = this.contextFactory.get();
        if (contextInitializer != null) {
            contextInitializer.accept(ctx);
        }
        return this.body.apply(ctx);
    }

    @Override
    public String toString() {
        return "DefaultActor{"
                + "contextFactory=" + this.contextFactory
                + ", action=" + this.action
                + ", conclusion=" + this.conclusion
                + '}';
    }
}
