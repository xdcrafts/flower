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

package org.xdcrafts.flower.core.spring.example;

import org.xdcrafts.flower.core.Middleware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;

/**
 * Middleware that log incoming and outgoing map.
 */
public class LoggingMiddleware implements Middleware {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingMiddleware.class);

    @Override
    public Function<Map, Map> apply(Map<String, Object> actionMeta, Function<Map, Map> mapMapFunction) {
        return map -> {
            LOGGER.info("Action {} input: {}", actionMeta, map);
            final Map out = mapMapFunction.apply(map);
            LOGGER.info("Action {} output: {}", actionMeta, out);
            return out;
        };
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
