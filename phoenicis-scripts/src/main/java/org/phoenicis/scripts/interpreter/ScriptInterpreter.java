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

package org.phoenicis.scripts.interpreter;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.function.Consumer;

public interface ScriptInterpreter {
    void runScript(String scriptContent, Consumer<Exception> errorCallback);

    default void runScript(File scriptFile, Consumer<Exception> errorCallback) {
        try {
            runScript(IOUtils.toString(new FileInputStream(scriptFile), "UTF-8"), errorCallback);
        } catch (Exception e) {
            errorCallback.accept(e);
        }
    }

    InteractiveScriptSession createInteractiveSession();
}
