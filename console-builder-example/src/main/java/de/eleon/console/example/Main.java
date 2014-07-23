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

import com.google.common.base.Optional;
import com.google.common.collect.Range;
import de.eleon.console.Console;
import de.eleon.console.functional.Validator;
import de.eleon.console.functional.Validators;
import jline.console.completer.StringsCompleter;

import java.io.IOException;

import static de.eleon.console.Console.ask;
import static de.eleon.console.example.Person.Gender;
import static de.eleon.console.functional.Transformers.toInteger;

class Main {


    private Main() {

        String firstName = Console.ask("Please enter your first name")
                .validateWith(Validators.notEmpty("Empty String not allowed"))
                .validateWith(Validators.regex("[a-zA-Z0-9\\-]{2,}", "Invalid format"))
                .useHistory()
                .answer();

        String lastName = Console.ask("Please enter your last name")
                .validateWith(Validators.notEmpty("Empty String not allowed"))
                .validateWith(Validators.regex("[a-zA-Z0-9\\-]{2,}", "Invalid format"))
                .useHistory()
                .answer();

        Optional<String> company = Console.ask("Please enter your company name")
                .validateWith(Validators.regex("[a-zA-Z0-9\\-]{2,}", "Invalid format"))
                .useHistory()
                .optional()
                .answer();

        Gender gender = Console.ask("Please enter your gender")
                .answer(Gender.class, "Please enter valid gender");

        String favoriteColor = Console.ask("What ist your favorite color?")
                .useHistoryFrom("color")
                .answer();

        Integer age = Console.ask("how old are you?")
                .completeWith(new StringsCompleter("22", "33", "44", "55", "66"))
                .validateWith(validateAge("please enter valid age"))
                .answer(toInteger());

        Console.println("First name " + firstName +
                ", last name " + lastName +
                ", company " + company.or("-") +
                ", gender " + gender +
                ", favoriteColor " + favoriteColor +
                ", age " + age);


        // Example creation of object

        Console.println("Create person");

        Person person = new Person(
                ask("Please enter your first name")
                .validateWith(Validators.notEmpty("Empty String not allowed")).answer(),
                ask("Please enter your gender").answer(Gender.class, "Please enter valid gender"),
                ask("how old are you?").answer(toInteger())
        );

        Console.println("person: " + person);

        System.exit(0);

    }

    private Validator validateAge(final String message) {
        return new Validator() {
            @Override
            public boolean valid(String input) {
                Integer age = Integer.valueOf(input);
                return Range.closed(10, 99).contains(age);
            }

            @Override
            public String message() {
                return message;
            }
        };
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }

}
