package de.eleon.console.functional;

public interface Validator {

    boolean valid(String input);

    String message();

}
