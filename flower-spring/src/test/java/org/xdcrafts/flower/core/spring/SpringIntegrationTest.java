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

package org.xdcrafts.flower.core.spring;

import org.xdcrafts.flower.core.Flow;
import org.xdcrafts.flower.core.spring.example.email.EmailSender;
import org.xdcrafts.flower.core.spring.example.sms.SmsSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xdcrafts.flower.core.utils.MapApi;
import org.xdcrafts.flower.core.utils.MapDsl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.xdcrafts.flower.core.utils.MapApi.DotNotation.dotGetUnsafe;
import static org.xdcrafts.flower.core.utils.MapDsl.Mutable.with;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing flower integration with spring.
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(locations = "/application-context.groovy")
public class SpringIntegrationTest {

    @Autowired
    private Flow mainFlow;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private SmsSender smsSender;

    @Test
    public void smsTest() throws ExecutionException, InterruptedException {
        final Map request = MapDsl.Mutable.with(new HashMap())
            .dotAssoc("auth.token", "secret-admin")
            .dotAssoc("request.type", "sms")
            .dotAssoc("number", "+78005555555")
            .dotAssoc("text", "spam")
            .value();
        final Map response = mainFlow.apply(request);
        assertTrue(MapApi.DotNotation.dotGetUnsafe(response, Boolean.class, "processed"));
        assertEquals(1, this.smsSender.getRequestsSent());
    }

    @Test
    public void emailTest() throws ExecutionException, InterruptedException {
        final Map request = MapDsl.Mutable.with(new HashMap())
            .dotAssoc("auth.token", "secret-admin")
            .dotAssoc("request.type", "email")
            .dotAssoc("to", "to@example.com")
            .dotAssoc("cc", "cc@example.com")
            .dotAssoc("text", "spam")
            .value();
        final Map response = mainFlow.apply(request);
        assertTrue(MapApi.DotNotation.dotGetUnsafe(response, Boolean.class, "processed"));
        assertEquals(1, this.emailSender.getRequestsSent());
    }
}
