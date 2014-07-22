package de.eleon.console;

import de.eleon.console.builder.AskBuilder;
import de.eleon.console.builder.ConsoleReaderWrapper;
import jline.console.ConsoleReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConsoleReader.class)
public class ConsoleTest {

    @Mock
    ConsoleReader consoleReader;

    @Captor
    ArgumentCaptor<CharSequence> printlnCaptor;

    @Before
    public void setUp() throws Exception {
        System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
        ConsoleReaderWrapper.getInstance().setConsoleReader(consoleReader);
    }

    @Test
    public void shouldAsk() throws Exception {
        AskBuilder askBuilder = Console.ask("question");
        assertThat(askBuilder, notNullValue());
    }

    @Test
    public void shouldPrintln() throws Exception {
        Console.println("test");
        Mockito.verify(consoleReader).println(printlnCaptor.capture());
        assertThat(printlnCaptor.getValue().toString(), is("test"));
    }

}