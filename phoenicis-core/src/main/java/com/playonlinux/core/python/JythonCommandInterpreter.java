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

package com.playonlinux.core.python;

import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.python.core.PyException;
import org.python.util.InteractiveInterpreter;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.services.manager.Service;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;

@Scan
public class JythonCommandInterpreter implements CommandInterpreter, Service {
    private static final Logger LOGGER = Logger.getLogger(JythonCommandInterpreter.class);

    @Inject
    static JythonInterpreterFactory jythonJythonInterpreterFactory;

    @Inject
    static ServiceManager serviceManager;

    private InteractiveInterpreter interactiveInterpreter;
    private final StringWriter returnBuffer;
    private final ExecutorService executorService;
    private Future currentTask;
    private final StringBuilder commandBuffer;

    public JythonCommandInterpreter(ExecutorService executorService) {
        this.returnBuffer = new StringWriter();
        this.executorService = executorService;
        this.commandBuffer = new StringBuilder();
    }

    @Override
    public boolean sendLine(String command, Function<String, Void> callback) {
        if(interactiveInterpreter == null) {
            try {
                interactiveInterpreter = jythonJythonInterpreterFactory.createInstance(InteractiveInterpreter.class);
                interactiveInterpreter.setOut(returnBuffer);
                interactiveInterpreter.setErr(returnBuffer);
            } catch (PlayOnLinuxException e) {
                LOGGER.error(e);
            }
        }

        commandBuffer.append(command);

        if(command.startsWith("\t") || command.startsWith(" ") || command.trim().endsWith(":")) {
            commandBuffer.append("\n");
            callback.apply("");
            return false;
        } else {
            String completeCommand = commandBuffer.toString();
            commandBuffer.setLength(0);
            currentTask = executorService.submit(() -> {
                returnBuffer.getBuffer().setLength(0);
                try {
                    interactiveInterpreter.exec(completeCommand);
                    callback.apply(returnBuffer.toString());
                } catch (PyException e) {
                    LOGGER.debug(e);
                    callback.apply(e.toString());
                }
            });
            return true;
        }

    }

    @Override
    public void shutdown() {
        if(this.interactiveInterpreter != null) {
            jythonJythonInterpreterFactory.close(this.interactiveInterpreter);
        }
        if(currentTask != null) {
            currentTask.cancel(true);
        }
    }

    @Override
    public void close() {
        serviceManager.unregister(this);
    }

    @Override
    public void init() {
        // Nothing to start
    }
}
