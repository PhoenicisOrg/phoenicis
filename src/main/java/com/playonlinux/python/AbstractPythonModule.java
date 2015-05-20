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

import org.python.core.PyObject;
import org.python.core.PyType;
import org.python.util.PythonInterpreter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPythonModule<T> {
    private final Class<T> type;
    protected final PythonInterpreter pythonInterpreter;

    public AbstractPythonModule(PythonInterpreter pythonInterpreter, Class<T> type) {
        this.type = type;
        this.pythonInterpreter = pythonInterpreter;
    }

    protected List<PyType> getCandidateClasses() {
        List <PyType> pyClasses = new ArrayList<>();

        for(PyObject localObject: pythonInterpreter.getLocals().asIterable()) {
            PyObject objectFound = pythonInterpreter.get(localObject.asString());
            if(objectFound instanceof PyType) {
                PyType typeFound = (PyType)objectFound;

                if(typeFound.isSubType(PyType.fromClass(type))) {
                    if(typeFound.isCallable() && "__main__".equals(typeFound.getModule().toString())) {
                        pyClasses.add(typeFound);
                    }
                }
            }

        }
        return pyClasses;
    }

}
