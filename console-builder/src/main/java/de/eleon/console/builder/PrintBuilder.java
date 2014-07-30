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

import java.util.Collection;

public class PrintBuilder {

    static void print(CharSequence line) {
        ConsoleReaderWrapper consoleReaderWrapper = new ConsoleReaderWrapper();
        consoleReaderWrapper.print(line);
        consoleReaderWrapper.close();
    }

    static void print(Collection<? extends CharSequence> columns) {
        ConsoleReaderWrapper consoleReaderWrapper = new ConsoleReaderWrapper();
        consoleReaderWrapper.print(columns);
        consoleReaderWrapper.close();
    }

}
