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
package de.eleon.console.builder;

import com.google.common.collect.Iterables;
import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;
import jline.console.history.FileHistory;
import jline.console.history.MemoryHistory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

/**
 * ConsoleReaderWrapper is a Wrapper around ConsoleReader. It capsules IOExceptions into Runtime Exceptions and facilitate usage of history. ConsoleReaderWrapper is a singleton.
 */
class ConsoleReaderWrapper {

    private FileHistory history;
    private ConsoleReader consoleReader;

    public ConsoleReaderWrapper() {
        try {
            this.consoleReader = ConsoleReaderFactory.get();
        } catch (IOException e) {
            throw new IllegalStateException("Can't create console", e);
        }
        this.init();
    }

    void init() {
        consoleReader.setPrompt("> ");
    }

    void enableHistoryFrom(String file) {
        try {
            Path directory = Paths.get(System.getProperty("user.home"), ".jline");
            Path historyFile = Paths.get(directory.toString(), file);
            if (!directory.toFile().exists()) {
                Files.createDirectories(directory);
            }
            if (!historyFile.toFile().exists()) {
                Files.createFile(historyFile);
            }
            history = new FileHistory(historyFile.toFile());
            consoleReader.setHistory(history);
            consoleReader.setHistoryEnabled(true);
        } catch (IOException e) {
            throw new IllegalStateException("Can't create history file", e);
        }
    }

    void disableHistory() {
        consoleReader.setHistory(new MemoryHistory());
        consoleReader.setHistoryEnabled(false);
    }

    /**
     * Get JLine ConsoleReader
     *
     * @return ConsoleReader
     */
    public ConsoleReader getConsoleReader() {
        return consoleReader;
    }

    /**
     * Set JLine ConsoleReader
     *
     * @param consoleReader the ConsoleReader
     */
    public void setConsoleReader(ConsoleReader consoleReader) {
        this.consoleReader = consoleReader;
    }

    /**
     * Print charSequence to console
     *
     * @param charSequence to print
     */
    public void print(CharSequence charSequence) {
        try {
            consoleReader.println(charSequence);
            consoleReader.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Can't write to console", e);
        }
    }

    /**
     * Print columns to console
     *
     * @param columns Collection of CharSequences to print
     */
    public void print(Collection<? extends CharSequence> columns) {
        try {
            consoleReader.printColumns(columns);
            consoleReader.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Can't write columns to console", e);
        }
    }

    /**
     * Remove all completers and add the new ones. If completers contains more then one element create an AggregateCompleter with the completers and add it.
     *
     * @param completers to add
     */
    public void setCompleters(List<Completer> completers) {
        for (Completer completer : consoleReader.getCompleters()) {
            consoleReader.removeCompleter(completer);
        }
        if (Iterables.size(completers) > 1) {
            Completer completer = new AggregateCompleter(completers);
            consoleReader.addCompleter(completer);
        }
        for (Completer completer : completers) {
            consoleReader.addCompleter(completer);
        }
    }

    /**
     * Get User input
     *
     * @return the user input
     */
    public String getInput() {
        try {
            String ret = consoleReader.readLine();
            if (ret != null) {
                ret = ret.trim();
            }
            if (ret != null && !ret.isEmpty() && consoleReader.isHistoryEnabled() && history != null) {
                history.add(ret);
                history.flush();
            }
            return ret;
        } catch (IOException e) {
            throw new IllegalStateException("Can't read from console", e);
        }
    }

    /**
     * Make beep sound if supported.
     *
     */
    public void beep() {
        try {
            consoleReader.beep();
        } catch (IOException e) {
            throw new IllegalStateException("Can't write to console", e);
        }
    }


    public void close() {
        consoleReader.shutdown();
    }

}
