package de.eleon.console.builder;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import static com.google.common.base.Optional.fromNullable;

/**
 * Extend AskBuilder to answer with an Optional.
 */
public class AnswerOptional {

    private final AskBuilder askBuilder;

    protected AnswerOptional(AskBuilder askBuilder) {
        this.askBuilder = askBuilder;
    }

    /**
     * Return user input
     *
     * @return user input as Optional<String>
     */
    public Optional<String> answer() {
        return optionalOf(askBuilder.answer());
    }

    /**
     * Return user input as Optional<T>
     *
     * @param function {@link com.google.common.base.Function} or {@link de.eleon.console.functional.Transformer} for value conversion
     * @param <T> the return type
     * @return user input as Optional<T>
     */
    public <T> Optional<T> answer(Function<String, T> function) {
        return fromNullable(askBuilder.answer(function));
    }

    /**
     * Return user input as Optional<T>
     *
     * @param function {@link Function} or {@link de.eleon.console.functional.Transformer} for value conversion
     * @param validationErrorMessage error message if function conversion fails
     * @param <T> the return type
     * @return user input as Optional<T>
     */
    public <T> Optional<T> answer(Function<String, T> function, final String validationErrorMessage) {
        return fromNullable(askBuilder.answer(function, validationErrorMessage));
    }

    /**
     * Return user input as Optional<T extends Enum<T>>. Complete with enum values.
     *
     * @param enumClass Class of enum to return
     * @param <T> the return type
     * @return user input as enum value
     */
    public <T extends Enum<T>> Optional<T> answer(final Class<T> enumClass) {
        return fromNullable(askBuilder.answer(enumClass));
    }

    /**
     * Return user input as Optional<T extends Enum<T>>. Complete with enum values.
     *
     * @param enumClass Class of enum to return
     * @param validationErrorMessage error message if enum conversion fails
     * @param <T> the return type
     * @return user input as enum value
     */
    public <T extends Enum<T>> Optional<T> answer(final Class<T> enumClass, final String validationErrorMessage) {
        return fromNullable(askBuilder.answer(enumClass, validationErrorMessage));
    }

    private Optional<String> optionalOf(String answer) {
        if (answer.trim().isEmpty()) return Optional.absent();
        return Optional.of(answer);
    }

}
