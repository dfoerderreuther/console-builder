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
package de.eleon.console.example;

import com.google.common.base.Objects;

public class Person {

    public enum Gender {
        MALE, FEMALE
    }

    private final String name;
    private final Gender gender;
    private final Integer age;

    public Person(String name, Gender gender, Integer age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(Person.class)
                .add("name", name)
                .add("gender", gender)
                .add("age", age)
                .toString();
    }
}
