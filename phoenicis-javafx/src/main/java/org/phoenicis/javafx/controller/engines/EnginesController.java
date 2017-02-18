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
import org.phoenicis.engines.dto.WineVersionDTO;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.mainwindow.engines.ViewEngines;

import java.util.function.Consumer;

public class EnginesController {
    private final ViewEngines viewEngines;
    private final WineVersionsManager wineVersionsManager;
    private final String wineEnginesPath;

    public EnginesController(ViewEngines viewEngines, WineVersionsManager wineVersionsManager, String wineEnginesPath) {
        this.viewEngines = viewEngines;
        this.wineVersionsManager = wineVersionsManager;
        this.wineEnginesPath = wineEnginesPath;

        this.viewEngines.setOnApplyFilter(filter -> {
            wineVersionsManager.fetchAvailableWineVersions(versions -> Platform.runLater(() -> {
                filter.apply(versions, wineEnginesPath);
                this.viewEngines.populate(versions, wineEnginesPath);
            }));
            this.viewEngines.showWineVersions();
        });

        this.viewEngines.setOnInstallEngine(wineVersionDTO -> {
            new ConfirmMessage("Install " + wineVersionDTO.getVersion(), "Are you sure you want to install " + wineVersionDTO.getVersion() + "?")
                    .ask(() -> {
                        installEngine(wineVersionDTO, e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
                    });
        });

        this.viewEngines.setOnDeleteEngine(wineVersionDTO -> {
            new ConfirmMessage("Delete " + wineVersionDTO.getVersion(), "Are you sure you want to delete " + wineVersionDTO.getVersion() + "?")
            .ask(() -> {
                deleteEngine(wineVersionDTO, e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
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

    private void installEngine(WineVersionDTO wineVersionDTO, Consumer<Exception> errorCallback) {
        // TODO
        System.out.println("install Engine");
    }

    private void deleteEngine(WineVersionDTO wineVersionDTO, Consumer<Exception> errorCallback) {
        // TODO
        System.out.println("delete Engine");
    }
}
