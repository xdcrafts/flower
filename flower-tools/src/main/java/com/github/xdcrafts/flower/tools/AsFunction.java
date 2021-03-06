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

package com.github.xdcrafts.flower.tools;

import java.util.function.Function;

/**
 * AsFunction interface that can be represented as simple function.
 * @param <A>
 * @param <B>
 */
public interface AsFunction<A, B> extends Function<A, B> {

    /**
     * Casts itself to function.
     */
    default Function<A, B> asFunction() {
        return this;
    }
}
