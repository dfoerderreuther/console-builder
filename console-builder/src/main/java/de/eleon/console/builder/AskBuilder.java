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

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.eleon.console.functional.Validator;
import jline.console.completer.Completer;
import jline.console.completer.EnumCompleter;

import java.util.List;

import static de.eleon.console.functional.Transformers.toEnum;
import static de.eleon.console.functional.Validators.enumValidator;
import static de.eleon.console.functional.Validators.functionValidator;

/**
 * Ask is a builder for a single Question console.
 *
 * Example:
 *
 *    String answer = Ask.ask("What is your Name?").answer();
 *    System.out.println("Answer: " + answer);
 *
 * The result in the terminal will looks like
 *
 *    What is your Name?
 *    > Dominik|
 *    Answer: Dominik
 *
 */
public class AskBuilder {

    private final String question;
    private final List<Validator> validators = Lists.newArrayList();
    private final List<Completer> completers = Lists.newArrayList();
    private Optional<String> history = Optional.absent();
    private boolean optional = false;

    public static AskBuilder ask(String question) {
        return new AskBuilder(question);
    }

    private AskBuilder(String question) {
        this.question = question;
    }

    /**
     * Add validator to validate answer before returning
     *
     * @param validator Validator to add
     * @return the builder instance
     */
    public AskBuilder validateWith(Validator validator) {
        this.validators.add(validator);
        return this;
    }

    /**
     * Add completer for tab completion
     *
     * @param completer {@see Completer} to add
     * @return the builder instance
     */
    public AskBuilder completeWith(Completer completer) {
        this.completers.add(completer);
        return this;
    }

    /**
     * Enable usage of history. History will be saved to ~/.jline/history
     *
     * @return the builder instance
     */
    public AskBuilder useHistory() {
        this.history = Optional.of("history");
        return this;
    }

    /**
     * Enable usage of history of specific file. History will be saved to ~/.jline/{@param file}
     *
     * @param file Filename as String
     * @return the builder instance
     */
    public AskBuilder useHistoryFrom(String file) {
        this.history = Optional.of(file);
        return this;
    }

    /**
     * Disable validation of empty user inputs
     *
     * @return the builder instance
     */
    public AnswerOptional optional() {
        this.optional = true;
        return new AnswerOptional(this);
    }

    /**
     * Return user input
     *
     * @return user input as String
     */
    public String answer() {
        return initConsoleAndGetAnswer();
    }

    /**
     * Return user input as T
     *
     * @param function {@link Function} or {@link de.eleon.console.functional.Transformer} for value conversion
     * @param <T> the return type
     * @return user input as T
     */
    public <T> T answer(Function<String, T> function) {
        return answer(function, "unknown value");
    }

    /**
     * Return user input as T
     *
     * @param function {@link Function} or {@link de.eleon.console.functional.Transformer} for value conversion
     * @param validationErrorMessage error message if function conversion fails
     * @param <T> the return type
     * @return user input as T
     */
    public <T> T answer(Function<String, T> function, final String validationErrorMessage) {
        validateWith(functionValidator(function, validationErrorMessage));
        return function.apply(answer());
    }

    /**
     * Return user input as T extends Enum<T>. Complete with enum values.
     *
     * @param enumClass Class of enum to return
     * @param <T> the return type
     * @return user input as enum value
     */
    public <T extends Enum<T>> T answer(final Class<T> enumClass) {
        return answer(enumClass, "unknown value");
    }

    /**
     * Return user input as T extends Enum<T>. Complete with enum values.
     *
     * @param enumClass Class of enum to return
     * @param validationErrorMessage error message if enum conversion fails
     * @param <T> the return type
     * @return user input as enum value
     */
    public <T extends Enum<T>> T answer(final Class<T> enumClass, final String validationErrorMessage) {
        validateWith(enumValidator(enumClass, validationErrorMessage));
        completeWith(new EnumCompleter(enumClass));
        return toEnum(enumClass).apply(answer());
    }

    /**
     * Initialize console and get user input as answer
     *
     * @return user input as String
     */
    private String initConsoleAndGetAnswer() {

        ConsoleReaderWrapper consoleReaderWrapper = initConsole();

        while (true) {
            String input = consoleReaderWrapper.getInput();
            if (validate(consoleReaderWrapper, input)) {
                return input;
            } else {
                consoleReaderWrapper.println("");
                consoleReaderWrapper.println(question);
            }
        }
    }

    /**
     * Initialize Console, write question, add completers and enable / disable history
     *
     * @return Console instance
     */
    private ConsoleReaderWrapper initConsole() {
        ConsoleReaderWrapper consoleReaderWrapper = ConsoleReaderWrapper.getInstance();
        consoleReaderWrapper.println("");
        consoleReaderWrapper.println(question);
        consoleReaderWrapper.setCompleters(completers);
        if (history.isPresent()) {
            consoleReaderWrapper.enableHistoryFrom(history.get());
        } else {
            consoleReaderWrapper.disableHistory();
        }
        return consoleReaderWrapper;
    }

    /**
     * Validate user input with available validators
     *
     * @param consoleReaderWrapper Console to print errir messages
     * @param input User input as String
     * @return boolean of validation result. valid == true
     */
    private boolean validate(ConsoleReaderWrapper consoleReaderWrapper, String input) {
        Iterable<String> errors = validate(input);
        if (!Iterables.isEmpty(errors)) {
            consoleReaderWrapper.beep();
            for (String error : errors) {
                consoleReaderWrapper.println(error);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Validate user input with list of {@see Validator} and collect error messages if available
     *
     * @param input user input as String
     * @return Iterable with error messages. Empty if valid.
     */
    private Iterable<String> validate(final String input) {
        if (optional && input.isEmpty()) return Lists.newArrayList();
        return FluentIterable
                .from(validators)
                .filter(new Predicate<Validator>() {
                    @Override
                    public boolean apply(Validator validator) {
                        try {
                            return !validator.valid(input);
                        } catch (Exception e) {
                            return true;
                        }
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
