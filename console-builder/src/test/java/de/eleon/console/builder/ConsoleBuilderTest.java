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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConsoleReader.class, ConsoleReaderFactory.class})
public class ConsoleBuilderTest {

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
    public void shouldAsk() throws Exception {
        when(consoleReader.readLine()).thenReturn("test");
        String answer = ConsoleBuilder.ask("Hallo").answer();
        assertThat(answer, is("test"));
    }

    @Test
    public void shouldPrint() throws Exception {
        ConsoleBuilder.print("test");
        verify(consoleReader).println(printlnCaptor.capture());
        assertThat(printlnCaptor.getValue().toString(), is("test"));
    }

    @Test
    public void shouldPrintColumns() throws Exception {
        ConsoleBuilder.print(ImmutableList.of("Test1", "Test2"));
        verify(consoleReader).printColumns(columnCaptor.capture());
        assertThat(columnCaptor.getValue(), contains((CharSequence) "Test1", "Test2"));
    }

    @Test
    public void shouldPrintNewline() throws Exception {
        ConsoleBuilder.newline();
        verify(consoleReader).println(printlnCaptor.capture());
        assertThat(printlnCaptor.getValue().toString(), is(""));
    }

}