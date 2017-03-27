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

package com.github.xdcrafts.flower.sage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entity that represents description of conformance errors.
 */
public final class Issue {

    /**
     * Issue builder class.
     */
    public static final class Builder {
        private final Object value;
        private final String message;
        private List<String> path;
        private List<String> in;
        private Throwable throwable;
        private Builder(Object value, String message) {
            this.value = value;
            this.message = message;
        }
        /**
         * Path setter.
         */
        public Builder setPath(List<String> path) {
            this.path = path;
            return this;
        }
        /**
         * In setter.
         */
        public Builder setIn(List<String> in) {
            this.in = in;
            return this;
        }
        /**
         * In setter.
         */
        public Builder setIn(String in) {
            this.in = Collections.singletonList(in);
            return this;
        }
        /**
         * Throwable setter.
         */
        public Builder setThrowable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }
        /**
         * Creates new Issue instance.
         */
        public Issue issue() {
            return new Issue(
                path == null ? new ArrayList<>() : path,
                in == null ? new ArrayList<>() : in,
                value,
                message,
                throwable
            );
        }
    }

    /**
     * Creates new instance of builder builder.
     */
    public static Builder builder(Object value, String message) {
        return new Builder(value, message);
    }

    private final List<String> path;
    private final List<String> in;
    private final Object value;
    private final String message;
    private final Throwable throwable;

    private Issue(List<String> path, List<String> in, Object value, String message, Throwable throwable) {
        this.path = path;
        this.in = in;
        this.value = value;
        this.message = message;
        this.throwable = throwable;
    }

    public List<String> getPath() {
        return path;
    }

    public List<String> getIn() {
        return in;
    }

    public Object getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public String toString() {
        return "Issue{"
                + "path=" + path
                + ", in=" + in
                + ", value=" + value
                + ", message='" + message + '\''
                + ", throwable=" + throwable
                + '}';
    }
}
