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

import java.util.concurrent.ExecutorService;

import com.playonlinux.core.services.manager.ServiceInitializationException;
import com.playonlinux.core.services.manager.ServiceManager;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.ui.api.CommandLineInterpreterFactory;

@Scan
public class JythonCommandLineInterpreterFactory implements CommandLineInterpreterFactory {
    @Inject
    static ServiceManager serviceManager;

    private final ExecutorService executorService;

    public JythonCommandLineInterpreterFactory(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public JythonCommandInterpreter createInstance() {
        JythonCommandInterpreter interpreter = new JythonCommandInterpreter(executorService);
        try {
            serviceManager.register(interpreter);
        } catch (ServiceInitializationException e) {
            throw new CommandInterpreterException("Unable to load Python Interpreter", e);
        }
        return interpreter;
    }
}
