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

import org.phoenicis.scripts.engine.PhoenicisInteractiveScriptSession;
import org.phoenicis.scripts.engine.PhoenicisScriptContextFactory;

import java.util.function.Consumer;

public class PhoenicisScriptInterpreter implements ScriptInterpreter {
    private final PhoenicisScriptContextFactory phoenicisScriptContextFactory;

    public PhoenicisScriptInterpreter(PhoenicisScriptContextFactory phoenicisScriptContextFactory) {
        this.phoenicisScriptContextFactory = phoenicisScriptContextFactory;
    }

    @Override
    public void runScript(String scriptContent, Runnable doneCallback, Consumer<Exception> errorCallback) {
        phoenicisScriptContextFactory.createScriptContext().eval(scriptContent, doneCallback, errorCallback);
    }

    @Override
    public InteractiveScriptSession createInteractiveSession() {
        return new PhoenicisInteractiveScriptSession(phoenicisScriptContextFactory);
    }

}
