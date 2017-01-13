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

package com.github.xdcrafts.flower.core;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Actor is a function that takes function, initializing action context, and provides result of type T.
 * @param <T> return type
 */
public interface Actor<T> extends Function<Consumer<Map>, T> {

    /**
     * Apply actor's action to map context initialized by supported closure.
     * @param contextInitializer closure that fills context with data to process
     * @return value of type T produced from map context
     */
    @Override
    T apply(Consumer<Map> contextInitializer);

    /**
     * Call apply without any initializer function provided.
     */
    default T apply() {
        return apply(ctx -> {
        });
    }
}
