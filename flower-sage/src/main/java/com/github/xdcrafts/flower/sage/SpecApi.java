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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Common validation api functions.
 * Possible improvements:
 * - implement or spec
 * - implement collection spec
 */
@SuppressWarnings("unchecked")
public class SpecApi {

    /**
     * Required spec.
     */
    public static Spec required(String message) {
        return value -> new Conformance(
            value,
            value == null
                ? Collections.singletonList(Issue.builder(null, message).issue())
                : Collections.emptyList()
        );
    }

    private static final Spec REQUIRED = required("value is required");

    /**
     * Required spec.
     */
    public static Spec required() {
        return REQUIRED;
    }

    private static final Spec NOT_EMPTY = value -> {
        final boolean isValid;
        if (value == null) {
            isValid = true;
        } else if (value instanceof String) {
            isValid = !((String) value).isEmpty();
        } else if (value instanceof Collection) {
            isValid = !((Collection) value).isEmpty();
        } else {
            isValid = true;
        }
        return new Conformance(
            value,
            isValid
                ? Collections.emptyList()
                : Collections.singletonList(Issue.builder(value, "can not be empty").issue())
        );
    };

    /**
     * Not empty spec.
     */
    public static Spec notEmpty() {
        return NOT_EMPTY;
    }

    /**
     * Spec that checks size of value.
     */
    public static Spec ofSize(String message, int min, int max) {
        return value -> {
            final boolean isValid;
            if (value == null) {
                isValid = true;
            } else if (value instanceof String) {
                isValid = ((String) value).length() >= min && ((String) value).length() <= max;
            } else if (value instanceof Collection) {
                isValid = ((Collection) value).size() >= min && ((Collection) value).size() <= max;
            } else {
                isValid = true;
            }
            return new Conformance(
                value,
                isValid
                    ? Collections.emptyList()
                    : Collections.singletonList(Issue.builder(value, message).issue())
            );
        };
    }

    /**
     * Spec that checks value type.
     */
    public static <T> Spec ofType(Class<T> clazz) {
        return value -> {
            if (value != null && !clazz.isAssignableFrom(value.getClass())) {
                return new Conformance(
                    value,
                    Issue.builder(value, "should be of type " + clazz.getSimpleName()).issue()
                );
            }
            return new Conformance(value, Collections.emptyList());
        };
    }

    /**
     * Constructs spec from message and predicate.
     */
    public static <T> Spec spec(String message, Class<T> clazz, Predicate<T> predicate) {
        return value -> {
            if (value == null) {
                return new Conformance(null, Collections.emptyList());
            }
            if (!clazz.isInstance(value)) {
                return new Conformance(
                    value,
                    Issue.builder(value, "should be of type " + clazz.getSimpleName()).issue()
                );
            }
            List<Issue> issues = Collections.emptyList();
            try {
                if (!predicate.test((T) value)) {
                    issues = Collections.singletonList(
                        Issue.builder(value, message).issue()
                    );
                }
            } catch (Throwable throwable) {
                issues = Collections.singletonList(
                    Issue.builder(value, message).setThrowable(throwable).issue()
                );
            }
            return new Conformance(value, issues);
        };
    }

    /**
     * Constructs spec from message and predicate function.
     */
    public static Spec spec(String message, Predicate predicate) {
        return spec(message, Object.class, predicate);
    }

    /**
     * Constructs spec from message and transformation function.
     */
    public static <T, U> Spec transpec(String message, Class<T> clazz, Function<T, U> transformation) {
        return value -> {
            if (value == null) {
                return new Conformance(null, Collections.emptyList());
            }
            if (!clazz.isInstance(value)) {
                return new Conformance(
                    value,
                    Issue.builder(value, "should be of type " + clazz.getSimpleName()).issue()
                );
            }
            List<Issue> issues = Collections.emptyList();
            U conformed = null;
            try {
                conformed = transformation.apply((T) value);
            } catch (Throwable throwable) {
                issues = Collections.singletonList(
                    Issue.builder(value, message).setThrowable(throwable).issue()
                );
            }
            return new Conformance(conformed, issues);
        };
    }

    /**
     * Constructs spec from message and transformation function.
     */
    public static <U> Spec transpec(String message, Function<Object, U> transformation) {
        return transpec(message, Object.class, transformation);
    }

    private static <T> T readValue(ObjectMapper objectMapper, Object value, Class<T> clazz) {
        try {
            final Class valueClass = value.getClass();
            final String jsonString;
            if (valueClass == String.class) {
                jsonString = "\"" + value + "\"";
            } else {
                jsonString = value.toString();
            }
            return objectMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Transformation spec that convert value to type T using jackson object mapper.
     */
    public static <T> Spec transpecJson(ObjectMapper objectMapper, Class<T> clazz) {
        return transpec("failed to convert to " + clazz.getSimpleName(), v -> readValue(objectMapper, v, clazz));
    }

    /**
     * And spec.
     */
    public static Spec and(boolean isLazy, List<Spec> specs) {
        return value -> {
            List<Issue> issues = new ArrayList<>();
            Object currentValue = value;
            for (Spec spec: specs) {
                final Conformance specConformance = spec.conform(currentValue);
                if (!specConformance.isCorrect()) {
                    issues.addAll(specConformance.getIssues());
                    if (isLazy) {
                        break;
                    }
                }
                currentValue = specConformance.getValue();
            }
            return new Conformance(currentValue, issues);
        };
    }

    /**
     * And spec.
     */
    public static Spec and(boolean isLazy, Spec... specs) {
        return and(isLazy, Arrays.asList(specs));
    }

    /**
     * And spec.
     */
    public static Spec greedyAnd(List<Spec> specs) {
        return and(false, specs);
    }

    /**
     * And spec.
     */
    public static Spec greedyAnd(Spec... specs) {
        return greedyAnd(Arrays.asList(specs));
    }

    /**
     * And spec.
     */
    public static Spec lazyAnd(List<Spec> specs) {
        return and(true, specs);
    }

    /**
     * And spec.
     */
    public static Spec lazyAnd(Spec... specs) {
        return lazyAnd(Arrays.asList(specs));
    }

    /**
     * Keys spec.
     */
    public static Spec keys(List<String> required, List<String> optional) {
        return value -> {
            if (value == null) {
                return new Conformance(null, Collections.emptyList());
            }
            if (!Map.class.isAssignableFrom(value.getClass())) {
                return new Conformance(
                    value,
                    Issue.builder(value, "should be of type " + Map.class.getSimpleName()).issue()
                );
            }
            final Map originalMap = (Map) value;
            final Map conformedMap = new HashMap();
            final List<Issue> issues = new ArrayList<>();
            for (String requiredKey : required) {
                final Object keyValue = originalMap.get(requiredKey);
                conformedMap.put(requiredKey, keyValue);
                if (keyValue == null) {
                    issues.add(
                        Issue
                            .builder(null, "is required")
                            .setIn(requiredKey)
                            .issue()
                    );
                }
            }
            for (String optionalKey : optional) {
                conformedMap.put(optionalKey, originalMap.get(optionalKey));
            }
            return new Conformance(conformedMap, issues);
        };
    }

    /**
     * Key spec.
     */
    public static Spec key(String key, String conformKey, Spec spec) {
        return value -> {
            if (value == null) {
                return new Conformance(null, Collections.emptyList());
            }
            if (!Map.class.isAssignableFrom(value.getClass())) {
                return new Conformance(
                    value,
                    Issue.builder(value, "should be of type " + Map.class.getSimpleName()).issue()
                );
            }
            final Map originalMap = (Map) value;
            final Map conformedMap = new HashMap(originalMap);
            final List<Issue> issues = new ArrayList<>();
            final Object keyValue = conformedMap.get(key);
            final Conformance specConformance = spec.conform(keyValue);
            if (!key.equals(conformKey)) {
                conformedMap.put(key, keyValue);
            }
            conformedMap.put(conformKey, specConformance.getValue());
            for (Issue issue : specConformance.getIssues()) {
                issue.getIn().add(0, key);
                issues.add(issue);
            }
            System.err.println(conformedMap);
            return new Conformance(conformedMap, issues);
        };
    }

    /**
     * Key spec.
     */
    public static Spec key(String key, Spec spec) {
        return key(key, key, spec);
    }

    /**
     * Key spec.
     */
    public static Spec key(String key, String conformKey, boolean isLazy, Spec... specs) {
        return key(key, conformKey, and(isLazy, specs));
    }

    /**
     * Key spec.
     */
    public static Spec key(String key, boolean isLazy, Spec... specs) {
        return key(key, key, isLazy, specs);
    }

    /**
     * Key spec.
     */
    public static Spec lazyKey(String key, String conformKey, Spec... specs) {
        return key(key, conformKey, true, specs);
    }

    /**
     * Key spec.
     */
    public static Spec lazyKey(String key, Spec... specs) {
        return key(key, true, specs);
    }

    /**
     * Key spec.
     */
    public static Spec greedyKey(String key, String conformKey, Spec... specs) {
        return key(key, conformKey, false, specs);
    }

    /**
     * Key spec.
     */
    public static Spec greedyKey(String key, Spec... specs) {
        return key(key, false, specs);
    }
}
