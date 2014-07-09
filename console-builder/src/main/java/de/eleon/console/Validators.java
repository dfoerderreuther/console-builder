/*
* Copyright 2014 Dominik Foerderreuther <dominik@eleon.de>
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package de.eleon.console;

import com.google.common.base.Function;

public class Validators {

    public static Validator notEmpty(final String message) {
        return new Validator() {
            @Override
            public boolean valid(String input) {
                return input != null && ! input.isEmpty();
            }

            @Override
            public String message() {
                return message;
            }
        };
    }

    public static Validator regex(final String regex, final String message) {
        return new Validator() {
            @Override
            public boolean valid(String input) {
                return input != null && input.matches(regex);
            }

            @Override
            public String message() {
                return message;
            }
        };
    }

    public static <T extends Enum<T>> Validator enumValidator(final Class<T> enumClass, final String message) {
        return new Validator() {
            @Override
            public boolean valid(String input) {
                try {
                    Transformers.toEnum(enumClass).apply(input);
                    return true;
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }

            @Override
            public String message() {
                return message;
            }
        };
    }

    public static Validator functionValidator(final Function function, final String message) {
        return new Validator() {
            @Override
            public boolean valid(String input) {
                try {
                    function.apply(input);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public String message() {
                return message;
            }
        };
    }

}
