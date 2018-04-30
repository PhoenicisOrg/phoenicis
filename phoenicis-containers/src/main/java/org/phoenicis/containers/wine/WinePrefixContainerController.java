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

package org.phoenicis.containers.wine;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.parameters.RegistryParameter;
import org.phoenicis.engines.EnginesManager;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.phoenicis.win32.registry.RegistryWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.Consumer;

public class WinePrefixContainerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WinePrefixContainerController.class);
    private final ScriptInterpreter scriptInterpreter;
    private final RegistryWriter registryWriter;
    private EnginesManager enginesManager;

    public WinePrefixContainerController(ScriptInterpreter scriptInterpreter,
            RegistryWriter registryWriter,
            EnginesManager enginesManager) {
        this.scriptInterpreter = scriptInterpreter;
        this.registryWriter = registryWriter;
        this.enginesManager = enginesManager;
    }

    public void changeSetting(WinePrefixContainerDTO winePrefix, RegistryParameter setting, Runnable doneCallback,
            Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();
        final String registryPatch = registryWriter.generateRegFileContent(setting.toRegistryPatch());

        LOGGER.info("Updating registry for prefix: " + winePrefix.getPath());
        LOGGER.info(registryPatch);

        interactiveScriptSession.eval("include([\"engines\", \"wine\", \"engine\", \"object\"]);",
                ignored -> interactiveScriptSession.eval("new Wine()", output -> {
                    final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                    wine.callMember("prefix", winePrefix.getName());
                    final ScriptObjectMirror regedit = (ScriptObjectMirror) wine.callMember("regedit");
                    regedit.callMember("patch", registryPatch);
                    wine.callMember("wait");
                    doneCallback.run();
                }, errorCallback), errorCallback);
    }

    public void runInContainer(ContainerDTO container, String command, Runnable doneCallback,
            Consumer<Exception> errorCallback) {
        // TODO: better way to get engine ID
        final String engineId = container.getEngine().toLowerCase();
        this.enginesManager.getEngine(engineId, engine -> {
            engine.setWorkingContainer(container.getName());
            engine.run(command, null, container.getPath(), false, true);
            // TODO: wait
            doneCallback.run();
        }, errorCallback);
    }
}
