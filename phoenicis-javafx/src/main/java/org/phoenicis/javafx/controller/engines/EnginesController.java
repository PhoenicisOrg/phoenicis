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

package org.phoenicis.javafx.controller.engines;

import javafx.application.Platform;
import org.phoenicis.engines.WineVersionsManager;
import org.phoenicis.engines.dto.WineEngineDTO;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.mainwindow.engines.ViewEngines;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

import java.util.function.Consumer;

public class EnginesController {
    private final ViewEngines viewEngines;
    private final WineVersionsManager wineVersionsManager;
    private final String wineEnginesPath;
    private final ScriptInterpreter scriptInterpreter;

    public EnginesController(ViewEngines viewEngines, WineVersionsManager wineVersionsManager, String wineEnginesPath, ScriptInterpreter scriptInterpreter) {
        this.viewEngines = viewEngines;
        this.wineVersionsManager = wineVersionsManager;
        this.wineEnginesPath = wineEnginesPath;
        this.scriptInterpreter = scriptInterpreter;

        this.viewEngines.setOnApplyFilter(filter -> {
            wineVersionsManager.fetchAvailableWineVersions(versions -> Platform.runLater(() -> {
                filter.apply(versions, wineEnginesPath);
                this.viewEngines.populate(versions, wineEnginesPath);
            }));
            this.viewEngines.showWineVersions();
        });

        this.viewEngines.setOnInstallEngine(wineEngineDTO -> {
            new ConfirmMessage("Install " + wineEngineDTO.getVersion(), "Are you sure you want to install " + wineEngineDTO.getVersion() + "?")
                    .ask(() -> {
                        installEngine(wineEngineDTO, e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
                    });
        });

        this.viewEngines.setOnDeleteEngine(wineEngineDTO -> {
            new ConfirmMessage("Delete " + wineEngineDTO.getVersion(), "Are you sure you want to delete " + wineEngineDTO.getVersion() + "?")
            .ask(() -> {
                deleteEngine(wineEngineDTO, e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
            });
        });
    }

    public ViewEngines getView() {
        return viewEngines;
    }

    public void loadEngines() {
        wineVersionsManager.fetchAvailableWineVersions(versions -> Platform.runLater(() -> this.viewEngines.populate(versions, wineEnginesPath)));
        this.viewEngines.showWineVersions();
    }

    private void installEngine(WineEngineDTO wineEngineDTO, Consumer<Exception> errorCallback) {
        /*final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Functions\", \"Engines\", \"Wine\"]);",
                ignored -> interactiveScriptSession.eval(
                        "new Wine()",
                        output -> {
                            final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                            wine.callMember("architecture", wineEngineDTO.getArchitecture());
                            wine.callMember("distribution", wineEngineDTO.getDistribution());
                            wine.callMember("version", wineEngineDTO.getVersion());
                            wine.callMember("_installVersion");
                            wine.callMember("wait");
                        },
                        errorCallback),
                errorCallback
        );*/
        // TODO
        System.out.println("install Engine");
    }

    private void deleteEngine(WineEngineDTO wineEngineDTO, Consumer<Exception> errorCallback) {
        // TODO
        System.out.println("delete Engine");
    }
}
