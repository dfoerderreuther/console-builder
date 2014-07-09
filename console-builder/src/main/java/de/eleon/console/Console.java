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
package de.eleon.console;

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
import java.util.List;

public class Console {


    private static Console instance;

    protected FileHistory history;
    protected ConsoleReader consoleReader;

    private Console() {
        try {
            this.consoleReader = new ConsoleReader();
        } catch (IOException e) {
            throw new IllegalStateException("Can't create console", e);
        }
        this.init();
    }

    public static Console getInstance() {
        if (instance == null) {
            instance = new Console();
        }
        return instance;
    }

    protected void init() {
        consoleReader.setPrompt("> ");
    }

    public void enableHistoryFrom(String file) {
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

    public void disableHistory() {
        consoleReader.setHistory(new MemoryHistory());
        consoleReader.setHistoryEnabled(false);
    }

    public ConsoleReader getConsoleReader() {
        return consoleReader;
    }

    public void println(CharSequence charSequence) {
        try {
            consoleReader.println(charSequence);
        } catch (IOException e) {
            throw new IllegalStateException("Can't write to console", e);
        }
    }

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

    public String getInput() {
        try {
            String ret = consoleReader.readLine();
            if (ret != null && !ret.isEmpty() && consoleReader.isHistoryEnabled() && history != null) {
                history.add(ret);
                history.flush();
            }
            return ret;
        } catch (IOException e) {
            throw new IllegalStateException("Can't read from console", e);
        }
    }

    public void beep() {
        try {
            consoleReader.beep();
        } catch (IOException e) {
            throw new IllegalStateException("Can't write to console", e);
        }
    }

}
