package de.eleon.console.builder;

import jline.console.ConsoleReader;

import java.io.IOException;

class ConsoleReaderFactory {

    static ConsoleReader get() throws IOException {
        return new ConsoleReader();
    }
}
