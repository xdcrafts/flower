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

import java.util.Collections;
import java.util.List;

/**
 * Conformance class.
 */
public final class Conformance {

    private final Object value;
    private final List<Issue> issues;

    public Conformance(Object value, List<Issue> issues) {
        this.value = value;
        this.issues = issues;
    }

    public Conformance(Object value, Issue issue) {
        this(value, Collections.singletonList(issue));
    }

    public Object getValue() {
        return value;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public boolean isCorrect() {
        return issues.isEmpty();
    }

    @Override
    public String toString() {
        return "Conformance{"
                + ", value=" + value
                + ", issues=" + issues
                + '}';
    }
}
