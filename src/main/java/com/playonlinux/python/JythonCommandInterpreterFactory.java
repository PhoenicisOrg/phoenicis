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

import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.services.BackgroundServiceManager;
import com.playonlinux.ui.api.CommandInterpreterFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Scan
public class JythonCommandInterpreterFactory implements CommandInterpreterFactory {
    @Inject
    private static BackgroundServiceManager backgroundServiceManager;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public JythonCommandInterpreter createInstance() {
        JythonCommandInterpreter interpreter = new JythonCommandInterpreter(executorService);
        backgroundServiceManager.register(interpreter);
        return interpreter;
    }
}
