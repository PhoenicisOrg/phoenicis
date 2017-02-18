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

import org.phoenicis.engines.WineVersionsManager;
import org.phoenicis.engines.dto.EnginesFilter;
import org.phoenicis.engines.dto.WineVersionDTO;
import org.phoenicis.engines.dto.WineVersionDistributionDTO;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.mainwindow.engines.ViewEngines;
import javafx.application.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
                if (filter != EnginesFilter.ALL) {
                    for (WineVersionDistributionDTO wineVersionDistributionDTO : versions) {
                        List<WineVersionDTO> filteredPackages = new ArrayList<>();
                        for (WineVersionDTO wineVersionDTO : wineVersionDistributionDTO.getPackages()) {
                            switch (filter) {
                                case INSTALLED:
                                    if (!isInstalled(wineVersionDistributionDTO, wineVersionDTO)) {
                                        filteredPackages.add(wineVersionDTO);
                                    }
                                        break;
                                case NOT_INSTALLED:
                                    if (isInstalled(wineVersionDistributionDTO, wineVersionDTO)) {
                                        filteredPackages.add(wineVersionDTO);
                                    }
                                    break;
                                case ALL:
                                default:
                            }
                        }
                        wineVersionDistributionDTO.getPackages().removeAll(filteredPackages);
                    }
                }
                this.viewEngines.populate(versions);
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
        wineVersionsManager.fetchAvailableWineVersions(versions -> Platform.runLater(() -> this.viewEngines.populate(versions)));
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

    private boolean isInstalled(WineVersionDistributionDTO wineVersionDistributionDTO, WineVersionDTO wineVersionDTO) {
        File f = new File(wineEnginesPath + "/" + wineVersionDistributionDTO.getName() + "/" + wineVersionDTO.getVersion());
        return f.exists();
    }
}
