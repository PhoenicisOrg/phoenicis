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

import org.phoenicis.containers.dto.WinePrefixDTO;
import org.phoenicis.containers.wine.parameters.RegistryParameter;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.phoenicis.tools.system.OperatingSystemFetcher;
import org.phoenicis.tools.system.terminal.TerminalOpener;
import org.phoenicis.win32.registry.RegistryWriter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class WineContainerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WineContainerController.class);
    private final ScriptInterpreter scriptInterpreter;
    private final TerminalOpener terminalOpener;
    private final String wineEnginesPath;
    private final OperatingSystemFetcher operatingSystemFetcher;
    private final RegistryWriter registryWriter;

    public WineContainerController(ScriptInterpreter scriptInterpreter,
                                   TerminalOpener terminalOpener,
                                   String wineEnginesPath,
                                   OperatingSystemFetcher operatingSystemFetcher, RegistryWriter registryWriter) {
        this.scriptInterpreter = scriptInterpreter;
        this.terminalOpener = terminalOpener;
        this.wineEnginesPath = wineEnginesPath;
        this.operatingSystemFetcher = operatingSystemFetcher;
        this.registryWriter = registryWriter;
    }

    public void repairPrefix(WinePrefixDTO winePrefix,
                             Runnable doneCallback,
                             Consumer<Exception> errorCallback) {
        // FIXME
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Functions\", \"Engines\", \"Wine\"]);",
                ignored -> interactiveScriptSession.eval(
                        "new Wine()",
                        output -> {
                            final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                            wine.callMember("prefix", winePrefix.getName());
                            wine.callMember("run", "wineboot");
                            wine.callMember("wait");
                            doneCallback.run();
                        },
                        errorCallback),
                errorCallback
        );
    }

    public void killProcesses(WinePrefixDTO winePrefix,
                              Runnable doneCallback,
                              Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Functions\", \"Engines\", \"Wine\"]);",
                ignored -> interactiveScriptSession.eval(
                        "new Wine()",
                        output -> {
                            final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                            wine.callMember("prefix", winePrefix.getName());
                            wine.callMember("kill");
                            doneCallback.run();
                        },
                        errorCallback),
                errorCallback
        );
    }

    public void changeSetting(WinePrefixDTO winePrefix,
                              RegistryParameter setting,
                              Runnable doneCallback,
                              Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();
        final String registryPatch = registryWriter.generateRegFileContent(setting.toRegistryPatch());

        LOGGER.info("Updating registry for prefix: " + winePrefix.getPath());
        LOGGER.info(registryPatch);

        interactiveScriptSession.eval("include([\"Functions\", \"Engines\", \"Wine\"]);",
                ignored -> interactiveScriptSession.eval(
                        "new Wine()",
                        output -> {
                            final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                            wine.callMember("prefix", winePrefix.getName());
                            final ScriptObjectMirror regedit = (ScriptObjectMirror) wine.callMember("regedit");
                            regedit.callMember("patch", registryPatch);
                            wine.callMember("wait");
                            doneCallback.run();
                        },
                        errorCallback),
                errorCallback
        );
    }

    public void runInPrefix(WinePrefixDTO winePrefix,
                            String command,
                            Runnable doneCallback,
                            Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Functions\", \"Engines\", \"Wine\"]);",
                ignored -> interactiveScriptSession.eval(
                        "new Wine()",
                        output -> {
                            final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                            wine.callMember("prefix", winePrefix.getName());
                            wine.callMember("run", command);
                            wine.callMember("wait");
                            doneCallback.run();
                        },
                        errorCallback),
                errorCallback
        );
    }

    public void openTerminalInPrefix(WinePrefixDTO winePrefixDTO) {
        final Map<String, String> environment = new HashMap<>();
        environment.put("WINEPREFIX", winePrefixDTO.getPath());
        environment.put("PATH", fetchWineVersionPath(winePrefixDTO) + "/bin/" + ":$PATH");
        terminalOpener.openTerminal(winePrefixDTO.getPath(), environment);
    }

    private String fetchWineVersionPath(WinePrefixDTO winePrefixDTO) {
        return wineEnginesPath +
                "/" +
                winePrefixDTO.getDistribution() +
                "-" +
                operatingSystemFetcher.fetchCurrentOperationSystem().getWinePackage() +
                "-" +
                winePrefixDTO.getArchitecture() +
                "/" +
                winePrefixDTO.getVersion();
    }
}
