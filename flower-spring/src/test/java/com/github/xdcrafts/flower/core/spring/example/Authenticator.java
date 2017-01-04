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

package com.github.xdcrafts.flower.core.spring.example;

import java.util.Map;

import static com.github.xdcrafts.flower.tools.MapApi.DotNotation.Mutable.dotAssoc;
import static com.github.xdcrafts.flower.tools.MapApi.DotNotation.dotGetString;

/**
 * Performs user authentication.
 */
public class Authenticator {

    /**
     * Keywords.
     */
    public static final class Keywords {
        public static final String TOKEN = "auth.token";
        public static final String USER = "auth.user";
    }

    private final Map<String, User> users;

    public Authenticator(Map<String, User> users) {
        this.users = users;
    }

    public Map authenticate(Map request) {
        final User user = dotGetString(request, Keywords.TOKEN)
            .map(this.users::get)
            .orElseThrow(() -> new RuntimeException("Unauthenticated"));
        return dotAssoc(request, Keywords.USER, user);
    }
}
