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

package org.phoenicis.javafx.controller.containers;

import javafx.application.Platform;
import org.phoenicis.containers.ContainersManager;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.WinePrefixContainerController;
import org.phoenicis.engines.EnginesSource;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.mainwindow.containers.ContainerPanelFactory;
import org.phoenicis.javafx.views.mainwindow.containers.ViewContainers;
import org.phoenicis.javafx.views.mainwindow.containers.WinePrefixContainerPanel;

import java.util.stream.Collectors;

public class ContainersController {
    private final ViewContainers viewContainers;
    private final ContainersManager containersManager;
    private final ContainerPanelFactory<WinePrefixContainerPanel, WinePrefixContainerDTO> winePrefixContainerPanelFactory;
    private final WinePrefixContainerController winePrefixContainerController;
    private final EnginesSource enginesSource;

    public ContainersController(ViewContainers viewContainers,
                                ContainersManager containersManager,
                                ContainerPanelFactory<WinePrefixContainerPanel, WinePrefixContainerDTO> winePrefixContainerPanelFactory, WinePrefixContainerController winePrefixContainerController, EnginesSource enginesSource) {
        this.viewContainers = viewContainers;
        this.containersManager = containersManager;
        this.winePrefixContainerPanelFactory = winePrefixContainerPanelFactory;
        this.winePrefixContainerController = winePrefixContainerController;
        this.enginesSource = enginesSource;

        viewContainers.setOnSelectionChanged(event -> {
            if (viewContainers.isSelected()) {
                loadContainers();
            }
        });

        viewContainers.setOnSelectContainer((ContainerDTO containerDTO) -> {
            enginesSource.fetchAvailableEngines(engineCategoryDTOS -> {
                final WinePrefixContainerPanel panel = winePrefixContainerPanelFactory.createContainerPanel((WinePrefixContainerDTO) containerDTO, viewContainers.getThemeManager(), engineCategoryDTOS.stream().flatMap(category -> category.getSubCategories().stream()).flatMap(subCategory -> subCategory.getPackages().stream()).collect(Collectors.toList()));
                panel.setOnWineCfg(winePrefixDTO -> winePrefixContainerController.runInPrefix(
                        winePrefixDTO,
                        "winecfg",
                        panel::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
                );

                panel.setOnRegedit(winePrefixDTO -> winePrefixContainerController.runInPrefix(
                        winePrefixDTO,
                        "regedit",
                        panel::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
                );

                panel.setOnWineboot(winePrefixDTO -> winePrefixContainerController.runInPrefix(
                        winePrefixDTO,
                        "wineboot",
                        panel::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
                );

                panel.setOnWinebootRepair(winePrefixDTO -> winePrefixContainerController.repairPrefix(
                        winePrefixDTO,
                        panel::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
                );

                panel.setOnWineConsole(winePrefixDTO -> winePrefixContainerController.runInPrefix(
                        winePrefixDTO,
                        "wineconsole",
                        panel::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
                );

                panel.setOnTaskMgr(winePrefixDTO -> winePrefixContainerController.runInPrefix(
                        winePrefixDTO,
                        "taskmgr",
                        panel::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
                );

                panel.setOnUninstaller(winePrefixDTO -> winePrefixContainerController.runInPrefix(
                        winePrefixDTO,
                        "uninstaller",
                        panel::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
                );

                panel.setOnKillProcess(winePrefixDTO -> winePrefixContainerController.killProcesses(
                        winePrefixDTO,
                        panel::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
                );

                panel.setOnOpenTerminalInWinePrefix(winePrefix -> {
                    winePrefixContainerController.openTerminalInPrefix(winePrefix);
                    panel.unlockAll();
                });

                panel.setOnChangeSetting((winePrefixDTO, setting) -> {
                    winePrefixContainerController.changeSetting(
                            winePrefixDTO,
                            setting,
                            panel::unlockAll,
                            e -> Platform.runLater(() -> new ErrorMessage("Error", e).show())
                    );
                    panel.unlockAll();
                });

                panel.setOnDeletePrefix(winePrefixDTO -> {
                    new ConfirmMessage("Delete " + winePrefixDTO.getName() + " container", "Are you sure you want to delete the " + winePrefixDTO.getName() + " container?")
                            .ask(() -> {
                                winePrefixContainerController.deletePrefix(winePrefixDTO, e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
                                loadContainers();
                            });
                });

                Platform.runLater(() -> viewContainers.showRightView(panel));
            });
        });
    }

    public ViewContainers getView() {
        return viewContainers;
    }

    public void loadContainers() {
        this.viewContainers.showWait();
        containersManager.fetchContainers(viewContainers::populate, e -> this.viewContainers.showFailure());
    }
}
