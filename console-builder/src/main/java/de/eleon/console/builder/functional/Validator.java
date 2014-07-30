package de.eleon.console.builder.functional;

public interface Validator {

    boolean valid(String input);

    String message();

}
