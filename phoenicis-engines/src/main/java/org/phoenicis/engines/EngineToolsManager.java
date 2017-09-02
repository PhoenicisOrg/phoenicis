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

package org.phoenicis.engines;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * manages the engine tools
 */
public class EngineToolsManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineToolsManager.class);
    private final ScriptInterpreter scriptInterpreter;

    /**
     * constructor
     * @param scriptInterpreter
     */
    public EngineToolsManager(ScriptInterpreter scriptInterpreter) {
        this.scriptInterpreter = scriptInterpreter;
    }

    /**
     * runs a tool in a given prefix
     * @param engineName
     * @param container
     * @param toolName
     * @param doneCallback
     * @param errorCallback
     */
    public void runTool(String engineName, String container, String toolName, Runnable doneCallback,
            Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval(
                "include([\"Engines\", \"" + engineName + "\", \"Tools\", \"" + toolName + "\"]);",
                ignored -> interactiveScriptSession.eval("new " + toolName + "()", output -> {
                    final ScriptObjectMirror toolObject = (ScriptObjectMirror) output;
                    toolObject.callMember("run", container);
                    doneCallback.run();
                }, errorCallback), errorCallback);
    }
}
