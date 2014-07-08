package de.eleon.console.example;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import de.eleon.console.Transform;
import de.eleon.console.Validator;
import de.eleon.console.Validators;
import jline.console.completer.StringsCompleter;

import java.io.IOException;

import static com.google.common.collect.Lists.newArrayList;
import static de.eleon.console.Ask.ask;

public class Main {

    public enum Gender {
        MALE, FEMALE
    }

    public Main() {

        String firstName = ask("Please enter your first name")
                .validateWith(Validators.notEmpty("Empty String not allowed"))
                .validateWith(Validators.regex("[a-zA-Z0-9\\-]{2,}", "Invalid format"))
                .useHistory()
                .answer();

        String lastName = ask("Please enter your last name")
                .validateWith(Validators.notEmpty("Empty String not allowed"))
                .validateWith(Validators.regex("[a-zA-Z0-9\\-]{2,}", "Invalid format"))
                .useHistory()
                .answer();

        Gender gender = ask("Please enter your gender")
                .validateWith(validateGender("Please enter valid gender"))
                .completeWith(new StringsCompleter(FluentIterable.from(newArrayList(Gender.values())).transform(fromGender()).toList()))
                .answer(toGender());

        String favoriteColor = ask("What ist your favorite color?")
                .useHistoryFrom("color")
                .answer();

        System.out.println("First name " + firstName + ", last name " + lastName + ", gender " + gender + ", favoriteColor " + favoriteColor);
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }

    private static Validator validateGender(final String message) {
        return new Validator() {
            @Override
            public boolean valid(String input) {
                try {
                    toGender().apply(input);
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

    private static Function<Gender, String> fromGender() {
        return new Function<Gender, String>() {
            @Override
            public String apply(Gender input) {
                return input.toString();
            }
        };
    }

    private static Transform<Gender> toGender() {
        return new Transform<Gender>() {
            @Override
            public Gender apply(String input) {
                return Gender.valueOf(input.toUpperCase());
            }
        };
    }
}
