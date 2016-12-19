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

package org.xdcrafts.flower.core.spring.example.email;

import org.xdcrafts.flower.tools.MapApi;

import java.util.Map;

/**
 * Email request validator.
 */
public class EmailRequestValidator {

    /**
     * Keywords.
     */
    public static final class Keywords {
        public static final String TO = "to";
        public static final String CC = "cc";
        public static final String TEXT = "text";
    }

    public Map validate(Map request) {
        MapApi.getString(request, Keywords.TO).orElseThrow(() -> new RuntimeException("'to' is required field."));
        MapApi.getString(request, Keywords.CC).orElseThrow(() -> new RuntimeException("'cc' is required field."));
        MapApi.getString(request, Keywords.TEXT).orElseThrow(() -> new RuntimeException("'text' is required field."));
        return request;
    }
}
