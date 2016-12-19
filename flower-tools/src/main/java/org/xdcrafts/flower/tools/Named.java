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

package org.xdcrafts.flower.tools;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

/**
 * Some thing that can have a name.
 */
public interface Named {

    /**
     * Transforms 'someCamelCasedName' to 'some-camel-cased-name'.
     */
    static String unCamelCase(String name) {
        final char[] charArray = name.toCharArray();
        final StringBuilder newName = new StringBuilder();
        boolean isPreviousCharInLowerCase = true;
        for (int i = 0; i < charArray.length; i++) {
            final char c = charArray[i];
            if (isAlphabetic(c)) {
                if (isUpperCase(c)) {
                    if (i != 0 && isPreviousCharInLowerCase) {
                        newName.append('-');
                    }
                    newName.append(toLowerCase(c));
                    isPreviousCharInLowerCase = false;
                } else {
                    newName.append(c);
                    isPreviousCharInLowerCase = true;
                }
            } else {
                if (c == '_') {
                    newName.append('-');
                } else {
                    newName.append(c);
                }
                isPreviousCharInLowerCase = false;
            }
        }
        return newName.toString();
    }

    /**
     * Transforms 'SOME_NAME' to 'someName'.
     */
    static String capsToCamelCase(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        String[] words = name.split("_");
        final StringBuilder newName = new StringBuilder(words[0].toLowerCase());
        for (int i = 1; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                newName.append(Character.toUpperCase(words[i].charAt(0)));
                newName.append(words[i].substring(1).toLowerCase());
            }
        }
        return newName.toString();
    }

    /**
     * Returns name.
     */
    String getName();

}
