package de.eleon.console.builder;


import com.google.common.collect.ImmutableList;
import jline.console.ConsoleReader;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConsoleReader.class, ConsoleReaderFactory.class})
public class PrintBuilderTest {

    @Mock
    ConsoleReader consoleReader;

    @Captor
    ArgumentCaptor<CharSequence> printlnCaptor;

    @Captor
    ArgumentCaptor<Collection<? extends CharSequence>> columnCaptor;

    @Before
    public void setUp() throws Exception {
        PowerMockito.spy(ConsoleReaderFactory.class);
        PowerMockito.when(ConsoleReaderFactory.get()).thenReturn(consoleReader);
        System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
    }

    @Test
    public void shouldPrint() throws Exception {
        PrintBuilder.print("test");
        Mockito.verify(consoleReader).println(printlnCaptor.capture());
        assertThat(printlnCaptor.getValue().toString(), is("test"));
    }

    @Test
    public void shouldPrintColumns() throws Exception {
        PrintBuilder.print(ImmutableList.of("Test1", "Test2"));
        Mockito.verify(consoleReader).printColumns(columnCaptor.capture());
        assertThat(columnCaptor.getValue(), Matchers.contains((CharSequence) "Test1", "Test2"));
    }
}