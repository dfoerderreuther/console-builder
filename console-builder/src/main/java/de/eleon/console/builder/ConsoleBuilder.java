
package de.eleon.console.builder;

import java.util.Collection;

public class ConsoleBuilder {

    /**
     * Start new Ask builder
     *
     * @param question The question to ask
     * @return the builder instance
     */
    public static AskBuilder ask(String question) {
        return AskBuilder.ask(question);
    }

    /**
     * Print line to console
     *
     * @param line CharSequence to print
     */
    public static void print(CharSequence line) {
        PrintBuilder.print(line);
    }

    /**
     * Print columns to console
     *
     * @param columns Collections of CharSequences to print
     */
    public static void print(Collection<? extends CharSequence> columns) {
        PrintBuilder.print(columns);
    }

    /**
     * Print empty line
     */
    public static void newline() {
        print("");
    }
}
