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
import org.python.util.PythonInterpreter;

/**
 * Represents a Jython's {@link PythonInterpreter} factory
 */
public interface JythonInterpreterFactory {
    /**
     * Creates an instance of a {@link PythonInterpreter} and keeps its reference
     * @return The instance
     */
    PythonInterpreter createInstance() throws PythonException;

    /**
     * Creates an instance of a class extending {@link PythonInterpreter} and keeps its reference
     * @param clazz The type of the class
     * @param <T> The type of the class
     * @return The instance
     * @throws PlayOnLinuxException If the interpreter cannot be created
     */
    <T extends PythonInterpreter> T createInstance(Class<T> clazz) throws PlayOnLinuxException;

    /**
     * Close an interpreter and clean Jython cache to free memory if it is required
     * @param interpreter The interpreter to close
     */
    void close(PythonInterpreter interpreter);
}
