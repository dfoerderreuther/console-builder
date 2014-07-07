package de.eleon.console.example;

import de.eleon.console.Validators;
import jline.console.completer.FileNameCompleter;
import jline.console.completer.StringsCompleter;

import java.io.IOException;

import static de.eleon.console.Ask.ask;

public class Main {

    public Main() {

        String answer =
                ask("Was möchten Sie tun?")
                .validateWith(Validators.notEmpty("Darf nicht leer sein"))
                .validateWith(Validators.regex("[a-zA-Z0-9\\-]{2,}", "Muss gültig sein"))
                .completeWith(new FileNameCompleter())
                .answer();

        String answer2 =
                ask("Was möchten Sie jetzt tun?")
                .validateWith(Validators.notEmpty("Darf nicht leer sein"))
                .completeWith(new StringsCompleter("test", "lala"))
                .answer();

        System.out.println("Answer: " + answer + " / " + answer2);
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }
}
