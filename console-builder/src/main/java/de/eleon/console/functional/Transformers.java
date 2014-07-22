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
package de.eleon.console.functional;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

public class Transformers {

    public static <T extends Enum<T>> Transformer<T> toEnum(final Class<T> enumClass) {
        return new Transformer<T>() {
            @Override
            public T apply(final String userIn) {
                Optional<T> ret = FluentIterable
                        .from(newArrayList(enumClass.getEnumConstants()))
                        .firstMatch(new Predicate<T>() {
                            @Override
                            public boolean apply(T enumValue) {
                                return enumValue.toString()
                                        .equalsIgnoreCase(userIn);
                            }
                        });
                if (!ret.isPresent()) throw new IllegalArgumentException(format("could not find value %s in enum %s", userIn, enumClass.getName()));
                return ret.get();
            }
        };
    }

    public static  Transformer<Integer> toInteger() {
        return new Transformer<Integer>() {
            @Override
            public Integer apply(final String userIn) {
                return Integer.parseInt(userIn);
            }
        };
    }


}
