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