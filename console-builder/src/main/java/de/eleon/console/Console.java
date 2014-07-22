
package de.eleon.console;

import de.eleon.console.builder.AskBuilder;
import de.eleon.console.builder.ConsoleReaderWrapper;

public class Console {

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
    public static void println(CharSequence line) {
        ConsoleReaderWrapper.getInstance().println(line);
    }
}
