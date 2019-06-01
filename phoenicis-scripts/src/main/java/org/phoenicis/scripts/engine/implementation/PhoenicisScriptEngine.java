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

package org.phoenicis.scripts.engine.implementation;

import com.google.common.util.concurrent.Runnables;

import java.io.InputStreamReader;
import java.util.function.Consumer;

public interface PhoenicisScriptEngine {
    void eval(InputStreamReader inputStreamReader, Consumer<Exception> errorCallback);

    void eval(String script, Runnable doneCallback, Consumer<Exception> errorCallback);

    default void eval(String script, Consumer<Exception> errorCallback) {
        eval(script, Runnables.doNothing(), errorCallback);
    }

    Object evalAndReturn(String line, Consumer<Exception> errorCallback);

    void put(String name, Object object, Consumer<Exception> errorCallback);

    void addErrorHandler(Consumer<Exception> errorHandler);
}
