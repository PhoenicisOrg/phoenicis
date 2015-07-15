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

import com.playonlinux.app.PlayOnLinuxException;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PySystemState;
import org.python.modules.zipimport.zipimport;
import org.python.util.PythonInterpreter;

import java.io.File;

public class JythonInterpreterFactory implements InterpreterFactory {
    private int numberOfInstances = 0;

    synchronized public PythonInterpreter createInstance() throws PlayOnLinuxException {
        return createInstance(PythonInterpreter.class);
    }

    synchronized public <T extends PythonInterpreter> T createInstance(Class<T> clazz) throws PlayOnLinuxException {
        File pythonPath = new File("src/main/python"); // TODO: Pass this in the properties
        System.setProperty("python.path", pythonPath.getAbsolutePath());
        numberOfInstances++;
        T interpreter = null;
        try {
            interpreter = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new PlayOnLinuxException("Unable to create a Python interpreter", e);
        }
        return interpreter;
    }

    synchronized public void close(PythonInterpreter interpreter) {
        interpreter.cleanup();
        numberOfInstances--;
        if(numberOfInstances == 0) {
            Py.defaultSystemState = new PySystemState();
            zipimport._zip_directory_cache = new PyDictionary();
        }
    }

}
