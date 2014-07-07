package de.eleon.console;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import jline.console.ConsoleReader;
import jline.console.completer.Completer;
import jline.console.completer.FileNameCompleter;
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

    @Mock
    ConsoleReader consoleReader;

    @Captor
    ArgumentCaptor<CharSequence> printlnCaptor;

    @Before
    public void setUp() throws IOException {
        Console.getInstance().consoleReader = consoleReader;
    }

    @Test
    public void shouldGetAnswer() throws IOException {
        when(consoleReader.readLine()).thenReturn("test");
        String answer = ask("Hallo").answer();
        assertThat(answer, is("test"));
    }

    @Test
    public void shouldAddCompleter() {
        Completer completer = new FileNameCompleter();
        ask("Frage").completeWith(completer).answer();
        verify(consoleReader).addCompleter(completer);
    }

    @Test
    public void shouldNotPrintErrorMessageIfValid() throws IOException {
        when(consoleReader.readLine()).thenReturn("test");
        ask("Hallo").validateWith(Validators.notEmpty("should not be empty")).answer();
        verify(consoleReader, atLeastOnce()).println(printlnCaptor.capture());
        assertFalse(findBy("should not be empty").isPresent());
    }

    @Test
    public void shouldPrintErrorMessage() throws IOException {
        when(consoleReader.readLine()).thenReturn("", "test");
        ask("Frage").validateWith(Validators.notEmpty("should not be empty")).answer();
        verify(consoleReader, atLeastOnce()).println(printlnCaptor.capture());
        assertTrue(findBy("should not be empty").isPresent());
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