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

import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PySystemState;
import org.python.modules.zipimport.zipimport;
import org.python.util.PythonInterpreter;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Interpreter extends PythonInterpreter implements AutoCloseable {
    private static int numberOfInstances = 0;
    private Interpreter() {
        super();
    }
    
    synchronized public static Interpreter createInstance() {
        File pythonPath = new File("src/main/python"); // TODO: Pass this in the properties
        System.setProperty("python.path", pythonPath.getAbsolutePath());
        numberOfInstances++;
        Interpreter interpreter = new Interpreter();
        return interpreter;
    }

    @Override
    synchronized public void close() {
        numberOfInstances--;
        if(numberOfInstances == 0) {
            Py.defaultSystemState = new PySystemState();
            zipimport._zip_directory_cache = new PyDictionary();
        }
    }
}
