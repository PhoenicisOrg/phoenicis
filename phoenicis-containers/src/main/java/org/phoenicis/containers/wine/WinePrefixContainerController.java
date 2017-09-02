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
import org.phoenicis.library.LibraryManager;
import org.phoenicis.library.ShortcutManager;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.phoenicis.tools.files.FileUtilities;
import org.phoenicis.tools.system.OperatingSystemFetcher;
import org.phoenicis.tools.system.terminal.TerminalOpener;
import org.phoenicis.win32.registry.RegistryWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class WinePrefixContainerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WinePrefixContainerController.class);
    private final ScriptInterpreter scriptInterpreter;
    private final TerminalOpener terminalOpener;
    private final String wineEnginesPath;
    private final OperatingSystemFetcher operatingSystemFetcher;
    private final RegistryWriter registryWriter;
    private final LibraryManager libraryManager;
    private final ShortcutManager shortcutManager;
    private final FileUtilities fileUtilities;

    public WinePrefixContainerController(ScriptInterpreter scriptInterpreter, TerminalOpener terminalOpener,
            String wineEnginesPath, OperatingSystemFetcher operatingSystemFetcher, RegistryWriter registryWriter,
            LibraryManager libraryManager, ShortcutManager shortcutManager, FileUtilities fileUtilities) {
        this.scriptInterpreter = scriptInterpreter;
        this.terminalOpener = terminalOpener;
        this.wineEnginesPath = wineEnginesPath;
        this.operatingSystemFetcher = operatingSystemFetcher;
        this.registryWriter = registryWriter;
        this.libraryManager = libraryManager;
        this.shortcutManager = shortcutManager;
        this.fileUtilities = fileUtilities;
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

    public void deletePrefix(WinePrefixContainerDTO winePrefix, Consumer<Exception> errorCallback) {
        try {
            fileUtilities.remove(new File(winePrefix.getPath()));
        } catch (IOException e) {
            LOGGER.error("Cannot delete Wine prefix (" + winePrefix.getPath() + ")! Exception: " + e.toString());
            errorCallback.accept(e);
        }

        List<ShortcutCategoryDTO> categories = libraryManager.fetchShortcuts();
        categories.stream().flatMap(shortcutCategoryDTO -> shortcutCategoryDTO.getShortcuts().stream())
                .forEach(shortcutDTO -> {
                    final InteractiveScriptSession interactiveScriptSession = scriptInterpreter
                            .createInteractiveSession();
                    interactiveScriptSession.eval("include([\"Engines\", \"Wine\", \"Shortcuts\", \"Reader\"]);",
                            ignored -> interactiveScriptSession.eval("new ShortcutReader()", output -> {
                                final ScriptObjectMirror shortcutReader = (ScriptObjectMirror) output;
                                shortcutReader.callMember("of", shortcutDTO);
                                final String container = (String) shortcutReader.callMember("container");
                                if (container.equals(winePrefix.getName())) {
                                    shortcutManager.deleteShortcut(shortcutDTO);
                                }
                            }, errorCallback), errorCallback);
                });
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
