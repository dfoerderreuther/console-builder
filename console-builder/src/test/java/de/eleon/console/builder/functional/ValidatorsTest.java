package de.eleon.console.builder.functional;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValidatorsTest {

    public enum TestEnum {a, b}

    @Test
    public void notEmpty_shouldBeValid() {
        assertThat(Validators.notEmpty("error message").valid("test"), is(true));
    }

    @Test
    public void notEmpty_shouldNotBeValid() {
        assertThat(Validators.notEmpty("error message").valid(""), is(false));
    }

    @Test
    public void notEmpty_shouldHaveErrorMessage() {
        assertThat(Validators.notEmpty("error message").message(), is("error message"));
    }

    @Test
    public void regex_shouldBeValid() {
        assertThat(Validators.regex("[0-9].", "error message").valid("21"), is(true));
    }

    @Test
    public void regex_shouldNotBeValid() {
        assertThat(Validators.regex("[0-9].", "error message").valid("aa"), is(false));
    }

    @Test
    public void regex_shouldHaveErrorMessage() {
        assertThat(Validators.regex("[0-9].", "error message").message(), is("error message"));
    }

    @Test
    public void enumValidator_shouldBeValid() {
        assertThat(
                Validators.enumValidator(TestEnum.class, "error message").valid("a"),
                is(true)
        );
    }

    @Test
    public void enumValidator_shouldNotBeValid() {
        assertThat(
                Validators.enumValidator(TestEnum.class, "error message").valid("c"),
                is(false)
        );
    }

    @Test
    public void enumValidator_shouldHaveErrrorMessage() {
        assertThat(
                Validators.enumValidator(TestEnum.class, "error message").message(),
                is("error message")
        );
    }

    @Test
    public void functionValidator_shouldBeValid() throws Exception {
        assertThat(
                Validators.functionValidator(Transformers.toInteger(), "error message").valid("1"),
                is(true)
        );
    }

    @Test
    public void functionValidator_shouldNotBeValid() throws Exception {
        assertThat(
                Validators.functionValidator(Transformers.toInteger(), "error message").valid("test"),
                is(false)
        );
    }

    @Test
    public void functionValidator_shouldHaveErrorMessage() {
        assertThat(
                Validators.functionValidator(Transformers.toInteger(), "error message").message(),
                is("error message")
        );
    }

}