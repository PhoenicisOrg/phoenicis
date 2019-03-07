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

import org.phoenicis.scripts.session.PhoenicisInteractiveScriptSession;
import org.phoenicis.scripts.engine.PhoenicisScriptEngineFactory;
import org.phoenicis.scripts.session.InteractiveScriptSession;

import java.util.function.Consumer;

public class PhoenicisScriptInterpreter implements ScriptInterpreter {
    private final PhoenicisScriptEngineFactory phoenicisScriptEngineFactory;

    public PhoenicisScriptInterpreter(PhoenicisScriptEngineFactory phoenicisScriptEngineFactory) {
        this.phoenicisScriptEngineFactory = phoenicisScriptEngineFactory;
    }

    @Override
    public void runScript(String scriptContent, Runnable doneCallback, Consumer<Exception> errorCallback) {
        phoenicisScriptEngineFactory.createEngine().eval(scriptContent, doneCallback, errorCallback);
    }

    @Override
    public InteractiveScriptSession createInteractiveSession() {
        return new PhoenicisInteractiveScriptSession(phoenicisScriptEngineFactory);
    }

}
