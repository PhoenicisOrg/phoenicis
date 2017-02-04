/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.phoenicis.javafx.controller.library.console;

import com.phoenicis.javafx.views.mainwindow.console.ConsoleTab;
import com.phoenicis.javafx.views.mainwindow.console.ConsoleTabFactory;
import com.phoenicis.javafx.views.mainwindow.console.ConsoleTextType;
import com.phoenicis.scripts.interpreter.InteractiveScriptSession;
import com.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.apache.commons.lang.exception.ExceptionUtils;

public class ConsoleController {
    private final ConsoleTabFactory consoleTabFactory;
    private final ScriptInterpreter scriptInterpreter;

    public ConsoleController(ConsoleTabFactory consoleTabFactory, ScriptInterpreter scriptInterpreter) {
        this.consoleTabFactory = consoleTabFactory;
        this.scriptInterpreter = scriptInterpreter;
    }

    public ConsoleTab createConsole() {
        final ConsoleTab consoleTab = consoleTabFactory.createInstance();
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        consoleTab.setOnSendCommand(command -> {
            consoleTab.appendTextToConsole("> " + command + "\n", ConsoleTextType.NORMAL);
            consoleTab.disableCommand();
            interactiveScriptSession.eval(command, result -> {
                consoleTab.appendTextToConsole(result == null ? "null\n" : result.toString() + "\n");
                consoleTab.enableCommand();
            }, error -> {
                consoleTab.appendTextToConsole(ExceptionUtils.getFullStackTrace(error), ConsoleTextType.ERROR);
                consoleTab.enableCommand();
            });
        });

        return consoleTab;
    }
}
