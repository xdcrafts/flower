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

package com.github.xdcrafts.flower.core.spring;

import com.github.xdcrafts.flower.core.Flow;
import com.github.xdcrafts.flower.core.spring.example.email.EmailSender;
import com.github.xdcrafts.flower.core.spring.example.sms.SmsSender;
import org.springframework.test.context.ActiveProfiles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.github.xdcrafts.flower.tools.map.MapDotApi.dotGetUnsafe;
import static com.github.xdcrafts.flower.tools.map.MapDsl.with;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing flower integration with spring.
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"email-feature", "sms-feature"})
@ContextConfiguration(locations = "/application-context.xml")
public class SpringIntegrationTest {

    @Autowired
    private Flow mainFlow;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private SmsSender smsSender;

    @Test
    public void smsTest() throws ExecutionException, InterruptedException {
        final Map request = with(new HashMap())
            .dotAssoc("auth.token", "secret-admin")
            .dotAssoc("request.type", "sms")
            .dotAssoc("number", "+78005555555")
            .dotAssoc("text", "spam")
            .value();
        final Map response = mainFlow.apply(request);
        assertTrue(dotGetUnsafe(response, Boolean.class, "processed"));
        assertEquals(1, this.smsSender.getRequestsSent());
    }

    @Test
    public void emailTest() throws ExecutionException, InterruptedException {
        final Map request = with(new HashMap())
            .dotAssoc("auth.token", "secret-admin")
            .dotAssoc("request.type", "email")
            .dotAssoc("to", "to@example.com")
            .dotAssoc("cc", "cc@example.com")
            .dotAssoc("text", "spam")
            .value();
        final Map response = mainFlow.apply(request);
        assertTrue(dotGetUnsafe(response, Boolean.class, "processed"));
        assertEquals(1, this.emailSender.getRequestsSent());
    }
}
