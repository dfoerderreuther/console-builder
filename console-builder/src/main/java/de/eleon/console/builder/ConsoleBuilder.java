
package de.eleon.console.builder;

import de.eleon.console.builder.functional.Applyable;

import java.util.Collection;

/**
 * Use the ConsoleBuilder to build console dialogs or simply to print something on the terminal.
 *
 * Example:
 *
 *    String answer = ConsoleBuilder.ask("What is your Name?").answer();
 *    System.out.print("Answer: " + answer);
 *
 * The result in the terminal will looks like
 *
 *    What is your Name?
 *    > Dominik|
 *    Answer: Dominik
 *
 */
public class ConsoleBuilder {

    /**
     * Start new AskBuilder to build a user dialog within the terminal
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
    public static void print(final CharSequence line) {
        print(new Applyable<ConsoleReaderWrapper>() {
            public void apply(ConsoleReaderWrapper consoleReaderWrapper) {
                consoleReaderWrapper.print(line);
            }
        });
    }

    /**
     * Print columns to console
     *
     * @param columns Collections of CharSequences to print
     */
    public static void print(final Collection<? extends CharSequence> columns) {
        print(new Applyable<ConsoleReaderWrapper>() {
            public void apply(ConsoleReaderWrapper consoleReaderWrapper) {
                consoleReaderWrapper.print(columns);
            }
        });
    }

    /**
     * Print empty line
     */
    public static void newline() {
        print("");
    }

    /**
     * print applyable
     *
     * @param applyable Applyable with print method
     */
    private static void print(Applyable<ConsoleReaderWrapper> applyable) {
        ConsoleReaderWrapper consoleReaderWrapper = new ConsoleReaderWrapper();
        applyable.apply(consoleReaderWrapper);
        consoleReaderWrapper.close();
    }
}
