package de.eleon.console;

import com.google.common.collect.ImmutableList;
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConsoleReader.class)
public class ConsoleTest {

    @Mock
    ConsoleReader consoleReader;

    @Captor
    ArgumentCaptor<CharSequence> printlnCaptor;

    @Captor
    ArgumentCaptor<Completer> completerCaptor;

    @Before
    public void setUp() throws IOException {
        Console.getInstance().consoleReader = consoleReader;
    }

    @Test
    public void shouldPrintln() throws IOException {
        Console.getInstance().println("test");
        verify(consoleReader).println(printlnCaptor.capture());
        assertThat(printlnCaptor.getValue().toString(), is("test"));
    }

    @Test
    public void shouldSetCompleters() {
        Completer completer = new FileNameCompleter();
        Console.getInstance().setCompleters(ImmutableList.<Completer>builder().add(completer).build());
        verify(consoleReader).addCompleter(completerCaptor.capture());
        assertThat(completerCaptor.getValue(), is(completer));
    }

    @Test
    public void shouldGetInput() throws IOException {
        when(consoleReader.readLine()).thenReturn("input message");
        String input = Console.getInstance().getInput();
        assertThat(input, is("input message"));
    }

    @Test
    public void shouldBeep() throws IOException {
        Console.getInstance().beep();
        verify(consoleReader).beep();
    }

}