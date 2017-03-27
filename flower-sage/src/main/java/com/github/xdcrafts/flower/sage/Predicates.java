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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Basic predicates.
 */
public class Predicates {

    /**
     * NotNull predicate.
     */
    public static Predicate notNull() {
        return Objects::nonNull;
    }

    /**
     * Constructs predicate that performs check if value is not null.
     */
    public static <T> Predicate<T> ifNotNull(Predicate<T> predicate) {
        return (T t) -> t == null || predicate.test(t);
    }

    /**
     * Local date predicate.
     */
    public static Predicate<String> date() {
        return (String value) -> {
            if (value == null) {
                return true;
            }
            try {
                LocalDate.parse(value);
                return true;
            } catch (DateTimeParseException dtpe) {
                return false;
            }
        };
    }

    /**
     * BigDecimal predicate.
     */
    public static Predicate decimal() {
        return (Object value) -> {
            if (value == null) {
                return true;
            }
            try {
                new BigDecimal(value.toString());
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
        };
    }

    /**
     * Predicate that checks value type.
     */
    public static Predicate ofType(Class<?> type) {
        return value -> value == null || type.isInstance(value);
    }

    /**
     * IsEmptyString predicate.
     */
    public static Predicate<String> isEmptyString() {
        return String::isEmpty;
    }
}
