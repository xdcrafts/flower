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

package org.xdcrafts.flower.core.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.xdcrafts.flower.core.utils.MapDsl.Mutable.with;


/**
 * Simple MapDsl tests.
 */
public class MapDslTest {

    @Test
    public void test() {
        final Map map = MapDsl.Mutable.with(new HashMap())
            .assoc(":user", ":firstname", "John")
            .assoc(":user", ":surname", "Doe")
            .assoc(":user", ":gender", true)
            .dissoc(":user", ":surname")
            .assoc(":user", ":lastname", "Doe")
            .value();
        Assert.assertEquals("John", MapApi.getNullableString(map, ":user", ":firstname"));
        Assert.assertEquals("Doe", MapApi.getNullableString(map, ":user", ":lastname"));
        Assert.assertTrue(MapApi.getUnsafe(map, Boolean.class, ":user", ":gender"));
        Assert.assertFalse(MapApi.contains(map, ":user", ":surname"));
    }
}
