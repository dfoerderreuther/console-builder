package de.eleon.console;

public class Validators {

    public static Validator notNull(final String message) {
        return new Validator() {
            @Override
            public boolean valid(String input) {
                return input != null;
            }

            @Override
            public String message() {
                return message;
            }
        };
    }

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

}
