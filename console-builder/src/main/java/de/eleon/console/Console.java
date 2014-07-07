package de.eleon.console;

import com.google.common.collect.Iterables;
import jline.console.ConsoleReader;
import jline.console.completer.AggregateCompleter;
import jline.console.completer.Completer;

import java.io.IOException;
import java.util.List;

public class Console {

    private static Console instance;

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
            return consoleReader.readLine();
        } catch (IOException e) {
            throw new IllegalStateException("Can't read from console", e);
        }
    }


    public void beep() {
        try {
            consoleReader.beep();
        } catch (IOException e) {
            throw new IllegalStateException("Can'T write to console", e);
        }
    }
}
