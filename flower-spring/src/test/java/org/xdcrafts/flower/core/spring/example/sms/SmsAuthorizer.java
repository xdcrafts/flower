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

package org.xdcrafts.flower.core.spring.example.sms;

import org.xdcrafts.flower.core.spring.example.Authenticator;
import org.xdcrafts.flower.core.spring.example.Roles;
import org.xdcrafts.flower.core.spring.example.User;

import java.util.Map;

import static org.xdcrafts.flower.core.utils.MapApi.DotNotation.dotGetUnsafe;

/**
 * Sms authorizer.
 */
public class SmsAuthorizer {

    /**
     * Keywords.
     */
    public static final class Keywords {
        public static final String USER = "auth.user";
    }

    public Map authorize(Map request) {
        final User user = dotGetUnsafe(request, User.class, Authenticator.Keywords.USER);
        if (!user.getRoles().contains(Roles.SMS)) {
            throw new RuntimeException("Unauthorized");
        }
        return request;
    }
}
