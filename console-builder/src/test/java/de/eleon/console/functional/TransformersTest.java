package de.eleon.console.functional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TransformersTest {

    public enum TestEnum {a, b}

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldTransformStringToEnum() {
        TestEnum testEnum = Transformers.toEnum(TestEnum.class).apply("a");
        assertThat(testEnum, is(TestEnum.a));
    }

    @Test
    public void shouldNotTransformUnknownValueToEnum() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("could not find value c in enum de.eleon.console.functional.TransformersTest$TestEnum");
        Transformers.toEnum(TestEnum.class).apply("c");
    }

    @Test
    public void shouldTransformToInteger() {
        Integer testInteger = Transformers.toInteger().apply(" 1 ");
        assertThat(testInteger, is(1));
    }

    @Test
    public void shouldNotTransformUnknownValueToInteger() {
        thrown.expect(NumberFormatException.class);
        thrown.expectMessage("For input string: \"a\"");
        Transformers.toInteger().apply("a");
    }

}