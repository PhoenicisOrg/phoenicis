/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.python;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.messages.RunnableWithParameter;
import com.playonlinux.messages.SynchronousMessage;
import com.playonlinux.ui.Controller;
import com.playonlinux.ui.UIMessageSender;
import com.playonlinux.ui.api.CommandInterpreter;
import org.python.util.InteractiveInterpreter;

import java.io.StringWriter;
import java.util.concurrent.ExecutorService;

@Scan
public class JythonCommandInterpreter implements CommandInterpreter {
    @Inject
    private static JythonInterpreterFactory jythonInterpreterFactory;

    @Inject
    static Controller controller;

    private InteractiveInterpreter interactiveInterpreter;
    private final StringWriter commandBuffer;
    private final ExecutorService executorService;
    private final UIMessageSender<Object> uiMessageSender;

    public JythonCommandInterpreter(ExecutorService executorService) {
        this.commandBuffer = new StringWriter();
        this.executorService = executorService;
        this.uiMessageSender = controller.createUIMessageSender();
    }

    @Override
    public void sendCommand(String text, RunnableWithParameter<String> callback) {
        if(interactiveInterpreter == null) {
            try {
                interactiveInterpreter = jythonInterpreterFactory.createInstance(InteractiveInterpreter.class);
                interactiveInterpreter.setOut(commandBuffer);
            } catch (PlayOnLinuxException e) {
                e.printStackTrace();
            }
        }

        executorService.submit(() -> {
            commandBuffer.getBuffer().setLength(0);
            interactiveInterpreter.exec(text);
            callback.run(commandBuffer.toString());
        });

    }
}
