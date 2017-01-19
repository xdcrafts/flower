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

import com.github.xdcrafts.flower.core.impl.DefaultActor;
import com.github.xdcrafts.flower.core.impl.DefaultFeature;
import com.github.xdcrafts.flower.core.impl.actions.AwaitAction;
import com.github.xdcrafts.flower.core.impl.actions.DefaultAction;
import com.github.xdcrafts.flower.core.impl.flows.AsyncFlow;
import com.github.xdcrafts.flower.core.impl.selectors.KeywordSelector;
import com.github.xdcrafts.flower.core.impl.extensions.DefaultExtension;
import com.github.xdcrafts.flower.core.impl.flows.SyncFlow;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.xdcrafts.flower.tools.map.MapApi.get;
import static com.github.xdcrafts.flower.tools.map.MapApi.getUnsafe;
import static com.github.xdcrafts.flower.tools.map.MapApi.assoc;
import static com.github.xdcrafts.flower.tools.map.MapDsl.with;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Simple tests.
 */
@SuppressWarnings("unchecked")
public class FlowerTest {

    private static final class ConclusionValue {
        private final boolean success;
        private final int steps;
        ConclusionValue(boolean success, int steps) {
            this.success = success;
            this.steps = steps;
        }
        boolean isSuccess() {
            return success;
        }
        int getSteps() {
            return steps;
        }
    }

    @Test
    public void test() {
        final Middleware counterMiddleware = Middleware.middleware("counterMiddleware", (map, function) -> ctx -> {
            final AtomicInteger counter = get(ctx, AtomicInteger.class, "meta", "dummy").orElse(new AtomicInteger());
            counter.incrementAndGet();
            return function.apply(assoc(ctx, "meta", "dummy", counter));
        });
        final Action firstAction = new DefaultAction(
            "firstAction",
            ctx -> assoc(ctx, "data", "first", true),
            Collections.singletonList(counterMiddleware)
        );
        final Action secondAction = new DefaultAction(
            "secondAction",
            ctx -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return assoc(ctx, "data", "second", true);
            },
            Collections.singletonList(counterMiddleware)
        );
        final Action thirdAction = new DefaultAction(
            "thirdAction",
            ctx -> assoc(ctx, "data", "third", true),
            Collections.singletonList(counterMiddleware)
        );
        final Action awaitAction = new AwaitAction("awaitAction", 110);
        final Flow simpleAsyncFlow = new AsyncFlow(
            "simpleFlow",
            Arrays.asList(firstAction, secondAction),
            with(new HashMap<String, Object>())
                .assoc(AsyncFlow.EXECUTOR_SERVICE, Executors.newSingleThreadExecutor())
                .value()
        );
        final Flow complexFlow = new SyncFlow(
            "complexFlow",
            Arrays.asList(simpleAsyncFlow, awaitAction, thirdAction)
        );
        final Actor<ConclusionValue> actor = new DefaultActor<>(
            ConcurrentHashMap::new,
            complexFlow,
            ctx -> new ConclusionValue(
                getUnsafe(ctx, Boolean.class, "data", "first")
                && getUnsafe(ctx, Boolean.class, "data", "second")
                && getUnsafe(ctx, Boolean.class, "data", "third"),
                getUnsafe(ctx, AtomicInteger.class, "meta", "dummy").intValue()
            )
        );
        final ConclusionValue result = actor.apply();
        assertTrue(result.isSuccess());
        assertEquals(3, result.getSteps());
    }

    @Test
    public void extensionsTest() {
        final Action firstAction = Action.action("first-action", ctx -> assoc(ctx, "data", "first", true));
        final Extension firstExtension = new DefaultExtension(
            "firstExtension",
            firstAction,
            with(new HashMap()).assoc(KeywordSelector.ConfigurationKeys.KEYWORD_VALUE, "first").value()
        );
        final Action secondAction = Action.action("second-action", ctx -> assoc(ctx, "data", "second", true));
        final Extension secondExtension = new DefaultExtension(
            "secondExtension",
            secondAction,
            with(new HashMap()).assoc(KeywordSelector.ConfigurationKeys.KEYWORD_VALUE, "second").value()
        );
        final Selector keywordSelector = new KeywordSelector("aSelector", "data.selectAction", true);
        new DefaultFeature(
            "feature",
            with(new HashMap())
                .assoc(firstExtension, keywordSelector)
                .assoc(secondExtension, keywordSelector)
                .value()
        );
        final Map firstResult = keywordSelector.apply(
            with(new HashMap()).assoc("data", "selectAction", "first").value()
        );
        final Map secondResult = keywordSelector.apply(
            with(new HashMap()).assoc("data", "selectAction", "second").value()
        );
        assertTrue(getUnsafe(firstResult, Boolean.class, "data", "first"));
        assertTrue(getUnsafe(secondResult, Boolean.class, "data", "second"));
    }
}
