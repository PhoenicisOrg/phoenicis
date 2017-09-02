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

    public void repairPrefix(WinePrefixContainerDTO winePrefix, Runnable doneCallback,
            Consumer<Exception> errorCallback) {
        // FIXME
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Engines\", \"Wine\", \"Engine\", \"Object\"]);",
                ignored -> interactiveScriptSession.eval("new Wine()", output -> {
                    final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                    wine.callMember("prefix", winePrefix.getName());
                    wine.callMember("run", "wineboot");
                    wine.callMember("wait");
                    doneCallback.run();
                }, errorCallback), errorCallback);
    }

    public void killProcesses(WinePrefixContainerDTO winePrefix, Runnable doneCallback,
            Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Engines\", \"Wine\", \"Engine\", \"Object\"]);",
                ignored -> interactiveScriptSession.eval("new Wine()", output -> {
                    final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                    wine.callMember("prefix", winePrefix.getName());
                    wine.callMember("kill");
                    doneCallback.run();
                }, errorCallback), errorCallback);
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

    public void runInPrefix(WinePrefixContainerDTO winePrefix, String command, Runnable doneCallback,
            Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Engines\", \"Wine\", \"Engine\", \"Object\"]);",
                ignored -> interactiveScriptSession.eval("new Wine()", output -> {
                    final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                    wine.callMember("prefix", winePrefix.getName());
                    wine.callMember("run", command);
                    wine.callMember("wait");
                    doneCallback.run();
                }, errorCallback), errorCallback);
    }

    /**
     * creates a shortcut for a given executable in a Wine prefix
     * @param winePrefix the Wine prefix
     * @param name name which is shown in the library
     * @param executable filename of the executable (WineShortcut will search for this file in the given prefix)
     * @param doneCallback callback executed after the shortcut has been created
     * @param errorCallback callback executed if there is an error
     */
    public void createShortcut(WinePrefixContainerDTO winePrefix, String name, String executable, Runnable doneCallback,
            Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Engines\", \"Wine\", \"Shortcuts\", \"Wine\"]);",
                ignored -> interactiveScriptSession.eval("new WineShortcut()", output -> {
                    final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                    wine.callMember("name", name);
                    wine.callMember("search", executable);
                    wine.callMember("prefix", winePrefix.getName());
                    wine.callMember("create");
                    doneCallback.run();
                }, errorCallback), errorCallback);
    }

    public void openTerminalInPrefix(WinePrefixContainerDTO winePrefixContainerDTO) {
        final Map<String, String> environment = new HashMap<>();
        environment.put("WINEPREFIX", winePrefixContainerDTO.getPath());
        environment.put("PATH", fetchWineVersionPath(winePrefixContainerDTO) + "/bin/" + ":$PATH");
        terminalOpener.openTerminal(winePrefixContainerDTO.getPath(), environment);
    }

    private String fetchWineVersionPath(WinePrefixContainerDTO winePrefixContainerDTO) {
        return wineEnginesPath + "/" + winePrefixContainerDTO.getDistribution() + "-"
                + operatingSystemFetcher.fetchCurrentOperationSystem().getWinePackage() + "-"
                + winePrefixContainerDTO.getArchitecture() + "/" + winePrefixContainerDTO.getVersion();
    }
}
