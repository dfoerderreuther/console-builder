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

import com.google.common.collect.ImmutableList;
import jline.console.ConsoleReader;
import jline.console.completer.Completer;
import jline.console.completer.FileNameCompleter;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
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

import java.io.IOException;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConsoleReader.class, ConsoleReaderFactory.class})
public class ConsoleReaderWrapperTest {

    @Mock
    ConsoleReader consoleReader;

    @Captor
    ArgumentCaptor<CharSequence> printlnCaptor;

    @Captor
    ArgumentCaptor<Completer> completerCaptor;

    @Captor
    ArgumentCaptor<Collection<? extends CharSequence>> columnCaptor;

    ConsoleReaderWrapper underTest;

    @Before
    public void setUp() throws IOException {
        PowerMockito.spy(ConsoleReaderFactory.class);
        PowerMockito.when(ConsoleReaderFactory.get()).thenReturn(consoleReader);
        underTest = new ConsoleReaderWrapper();
        System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
    }

    @After
    public void tearDown() {
        underTest.close();
    }

    @Test
    public void shouldPrint() throws IOException {
        underTest.print("test");
        verify(consoleReader).println(printlnCaptor.capture());
        assertThat(printlnCaptor.getValue().toString(), is("test"));
    }

    @Test
    public void shouldPrintColumns() throws Exception {
        underTest.print(ImmutableList.of("Test1", "Test2"));
        verify(consoleReader).printColumns(columnCaptor.capture());
        assertThat(columnCaptor.getValue(), contains((CharSequence) "Test1", "Test2"));
    }

    @Test
    public void shouldSetCompleters() {
        Completer completer = new FileNameCompleter();
        underTest.setCompleters(ImmutableList.<Completer>builder().add(completer).build());
        verify(consoleReader).addCompleter(completerCaptor.capture());
        assertThat(completerCaptor.getValue(), is(completer));
    }

    @Test
    public void shouldGetInput() throws IOException {
        when(consoleReader.readLine()).thenReturn("input message");
        String input = underTest.getInput();
        assertThat(input, is("input message"));
    }

    @Test
    public void shouldBeep() throws IOException {
        underTest.beep();
        verify(consoleReader).beep();
    }

}