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
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.phoenicis.tools.system.OperatingSystemFetcher;
import org.phoenicis.tools.system.terminal.TerminalOpener;
import org.phoenicis.win32.registry.RegistryWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WinePrefixContainerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WinePrefixContainerController.class);
    private final ScriptInterpreter scriptInterpreter;
    private final TerminalOpener terminalOpener;
    private final String wineEnginesPath;
    private final OperatingSystemFetcher operatingSystemFetcher;
    private final RegistryWriter registryWriter;

    public WinePrefixContainerController(ScriptInterpreter scriptInterpreter,
            TerminalOpener terminalOpener,
            String wineEnginesPath,
            OperatingSystemFetcher operatingSystemFetcher,
            RegistryWriter registryWriter) {
        this.scriptInterpreter = scriptInterpreter;
        this.terminalOpener = terminalOpener;
        this.wineEnginesPath = wineEnginesPath;
        this.operatingSystemFetcher = operatingSystemFetcher;
        this.registryWriter = registryWriter;
    }

    public void changeSetting(WinePrefixContainerDTO winePrefix, RegistryParameter setting, Runnable doneCallback,
            Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();
        final String registryPatch = registryWriter.generateRegFileContent(setting.toRegistryPatch());

        LOGGER.info("Updating registry for prefix: " + winePrefix.getPath());
        LOGGER.info(registryPatch);

        interactiveScriptSession.eval("include([\"Engines\", \"Wine\", \"Engine\", \"Object\"]);",
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
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval(
                "include([\"Engines\", \"" + container.getEngine() + "\", \"Engine\", \"Object\"]);",
                ignored -> interactiveScriptSession.eval("new " + container.getEngine() + "()", output -> {
                    final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                    wine.callMember("prefix", container.getName());
                    wine.callMember("run", command);
                    wine.callMember("wait");
                    doneCallback.run();
                }, errorCallback), errorCallback);
    }
}
