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

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import jline.console.ConsoleReader;
import jline.console.completer.Completer;
import jline.console.completer.FileNameCompleter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static de.eleon.console.Ask.ask;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConsoleReader.class)
public class AskTest {

    public enum TestEnum {a, b, c}

    @Mock
    ConsoleReader consoleReader;

    @Captor
    ArgumentCaptor<CharSequence> printlnCaptor;

    @Before
    public void setUp() throws IOException {
        Console.getInstance().consoleReader = consoleReader;
    }

    @Test
    public void shouldAnswer() throws IOException {
        when(consoleReader.readLine()).thenReturn("test");
        String answer = ask("Hallo").answer();
        assertThat(answer, is("test"));
    }

    @Test
    public void shouldAnswerInteger() throws IOException {
        when(consoleReader.readLine()).thenReturn("12");
        int answer = ask("Question")
                .answer(Transformers.toInteger());
        assertThat(answer, is(12));
    }

    @Test
    public void shouldAnswerIntegerAfterGrubbyInput() throws IOException {
        when(consoleReader.readLine()).thenReturn(" 12 ");
        int answer = ask("Question")
                .answer(Transformers.toInteger());
        assertThat(answer, is(12));
    }

    @Test
    public void shouldValidateInteger() throws IOException {
        when(consoleReader.readLine()).thenReturn("test", "12");
        int answer = ask("Question")
                .answer(Transformers.toInteger());
        assertThat(answer, is(12));

        verify(consoleReader, atLeastOnce()).println(printlnCaptor.capture());
        assertTrue(findBy("unknown value").isPresent());
    }

    @Test
    public void shouldValidateIntegerWithCustomMessage() throws IOException {
        when(consoleReader.readLine()).thenReturn("test", "12");
        int answer = ask("Question")
                .answer(Transformers.toInteger(), "wrong number");
        assertThat(answer, is(12));

        verify(consoleReader, atLeastOnce()).println(printlnCaptor.capture());
        assertFalse(findBy("unknown value").isPresent());
        assertTrue(findBy("wrong number").isPresent());
    }

    @Test
    public void shouldAddCompleter() {
        Completer completer = new FileNameCompleter();
        ask("Question").completeWith(completer).answer();
        verify(consoleReader).addCompleter(completer);
    }

    @Test
    public void shouldNotPrintErrorMessageIfValid() throws IOException {
        when(consoleReader.readLine()).thenReturn("test");
        ask("Question").validateWith(Validators.notEmpty("should not be empty")).answer();
        verify(consoleReader, atLeastOnce()).println(printlnCaptor.capture());
        assertFalse(findBy("should not be empty").isPresent());
    }

    @Test
    public void shouldPrintErrorMessage() throws IOException {
        when(consoleReader.readLine()).thenReturn("", "test");
        ask("Question").validateWith(Validators.notEmpty("should not be empty")).answer();
        verify(consoleReader, atLeastOnce()).println(printlnCaptor.capture());
        assertTrue(findBy("should not be empty").isPresent());
    }

    @Test
    public void shouldAnswerEnum() throws IOException {
        when(consoleReader.readLine()).thenReturn("a");
        TestEnum testEnum = ask("a or b").answer(TestEnum.class);

        Assert.assertThat(testEnum, is(TestEnum.a));
    }

    @Test
    public void shouldAnswerEnumInWrongCase() throws IOException {
        when(consoleReader.readLine()).thenReturn("A");
        TestEnum testEnum = ask("a or b").answer(TestEnum.class);

        Assert.assertThat(testEnum, is(TestEnum.a));
    }

    @Test
    public void shouldValidateEnum() throws IOException {
        when(consoleReader.readLine()).thenReturn("test", "a");
        TestEnum testEnum = ask("a or b").answer(TestEnum.class);

        Assert.assertThat(testEnum, is(TestEnum.a));

        verify(consoleReader, atLeastOnce()).println(printlnCaptor.capture());
        assertTrue(findBy("unknown value").isPresent());
    }



    private Optional<CharSequence> findBy(final String text) {
        return FluentIterable.from(printlnCaptor.getAllValues()).firstMatch(new Predicate<CharSequence>() {
            @Override
            public boolean apply(CharSequence input) {
                return input.toString().equals(text);
            }
        });
    }

}