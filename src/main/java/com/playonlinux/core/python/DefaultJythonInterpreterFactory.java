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

import java.io.File;

import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PySystemState;
import org.python.modules.zipimport.zipimport;
import org.python.util.PythonInterpreter;

public class DefaultJythonInterpreterFactory implements JythonInterpreterFactory {
    private int numberOfInstances = 0;

    @Override
    public synchronized PythonInterpreter createInstance() throws PythonException {
        return createInstance(PythonInterpreter.class);
    }

    @Override
    public synchronized <T extends PythonInterpreter> T createInstance(Class<T> clazz) throws PythonException {
        File pythonPath = new File("src/main/python"); // TODO: Pass this in the properties
        System.setProperty("python.path", pythonPath.getAbsolutePath());
        numberOfInstances++;
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new PythonException("Unable to create a Python interpreter", e);
        }
    }

    @Override
    public synchronized void close(PythonInterpreter interpreter) {
        interpreter.cleanup();
        numberOfInstances--;
        if(numberOfInstances == 0) {
            Py.defaultSystemState = new PySystemState();
            zipimport._zip_directory_cache = new PyDictionary();
        }
    }

}
