package de.eleon.console;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import jline.console.completer.Completer;

import java.util.List;

public class Ask {

    private String question;
    private List<Validator> validators = Lists.newArrayList();
    private List<Completer> completers = Lists.newArrayList();

    protected Ask(String question) {
        this.question = question;
    }

    public static Ask ask(String s) {
        return new Ask(s);
    }

    public Ask validateWith(Validator validator) {
        this.validators.add(validator);
        return this;
    }

    public Ask completeWith(Completer completer) {
        this.completers.add(completer);
        return this;
    }

    public String answer() {
        return getAnswerFromConsole();
    }

    private String getAnswerFromConsole() {

        Console console = Console.getInstance();
        console.println(question);
        console.setCompleters(completers);

        while (true) {
            String input = console.getInput();
            if (validate(console, input)) {
                return input;
            } else {
                console.println(question);
            }
        }
    }

    private boolean validate(Console console, String input) {
        Iterable<String> errors = validate(input);
        if (!Iterables.isEmpty(errors)) {
            console.beep();
            for (String error : errors) {
                console.println(error);
            }
            return false;
        } else {
            return true;
        }
    }

    private Iterable<String> validate(final String input) {
        return FluentIterable
                .from(validators)
                .filter(new Predicate<Validator>() {
                    @Override
                    public boolean apply(Validator validator) {
                        return !validator.valid(input);
                    }
                })
                .transform(new Function<Validator, String>() {
                    @Override
                    public String apply(Validator input) {
                        return input.message();
                    }
                });
    }

}
