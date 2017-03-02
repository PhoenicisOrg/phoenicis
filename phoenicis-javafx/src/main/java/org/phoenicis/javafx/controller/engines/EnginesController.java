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
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.phoenicis.engines.EnginesSource;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.mainwindow.engines.ViewEngines;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

import java.util.function.Consumer;

public class EnginesController {
    private final ViewEngines viewEngines;
    private final EnginesSource enginesSource;
    private final String wineEnginesPath;
    private final ScriptInterpreter scriptInterpreter;

    public EnginesController(ViewEngines viewEngines, EnginesSource enginesSource, String wineEnginesPath, ScriptInterpreter scriptInterpreter) {
        this.viewEngines = viewEngines;
        this.enginesSource = enginesSource;
        this.wineEnginesPath = wineEnginesPath;
        this.scriptInterpreter = scriptInterpreter;

        this.viewEngines.setOnApplyFilter(filter -> {
            enginesSource.fetchAvailableEngines(versions -> Platform.runLater(() -> {
                filter.apply(versions, wineEnginesPath);
                this.viewEngines.populate(versions, wineEnginesPath);
            }));
            this.viewEngines.showWineVersions();
        });

        this.viewEngines.setOnInstallEngine(engineDTO -> {
            new ConfirmMessage("Install " + engineDTO.getVersion(), "Are you sure you want to install " + engineDTO.getVersion() + "?")
                    .ask(() -> {
                        installEngine(engineDTO, e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
                    });
        });

        this.viewEngines.setOnDeleteEngine(engineDTO -> {
            new ConfirmMessage("Delete " + engineDTO.getVersion(), "Are you sure you want to delete " + engineDTO.getVersion() + "?")
            .ask(() -> {
                deleteEngine(engineDTO, e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
            });
        });
    }

    public ViewEngines getView() {
        return viewEngines;
    }

    public void loadEngines() {
        enginesSource.fetchAvailableEngines(versions -> Platform.runLater(() -> this.viewEngines.populate(versions, wineEnginesPath)));
        this.viewEngines.setOnSelectCategory(categoryDTO -> this.viewEngines.populateEngines(categoryDTO.getName(), categoryDTO.getSubCategories(), wineEnginesPath));
        this.viewEngines.showWineVersions();
    }

    private void installEngine(EngineDTO engineDTO, Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Functions\", \"Functions\", \"Engines\", \"" + engineDTO.getCategory() + "\"]);",
                ignored -> interactiveScriptSession.eval(
                        "new Wine()",
                        output -> {
                            final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                            wine.callMember("install", engineDTO.getCategory()
                                    , engineDTO.getSubCategory()
                                    , engineDTO.getVersion()
                                    , engineDTO.getUserData());
                        },
                        errorCallback),
                errorCallback
        );
    }

    private void deleteEngine(EngineDTO engineDTO, Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Functions\", \"Functions\", \"Engines\", \"" + engineDTO.getCategory() + "\"]);",
                ignored -> interactiveScriptSession.eval(
                        "new Wine()",
                        output -> {
                            final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                            wine.callMember("delete", engineDTO.getCategory()
                                    , engineDTO.getSubCategory()
                                    , engineDTO.getVersion()
                                    , engineDTO.getUserData());
                        },
                        errorCallback),
                errorCallback
        );
    }
}
