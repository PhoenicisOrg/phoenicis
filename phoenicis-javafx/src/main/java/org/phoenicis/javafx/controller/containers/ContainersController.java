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

import org.phoenicis.containers.ContainersManager;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixDTO;
import org.phoenicis.containers.wine.WineContainerController;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.mainwindow.containers.ContainerPanelFactory;
import org.phoenicis.javafx.views.mainwindow.containers.ViewContainers;
import org.phoenicis.javafx.views.mainwindow.containers.WinePrefixContainerPanel;
import javafx.application.Platform;

public class ContainersController {
    private final ViewContainers viewContainers;
    private final ContainersManager containersManager;
    private final ContainerPanelFactory<WinePrefixContainerPanel, WinePrefixDTO> wineContainerPanelFactory;
    private final WineContainerController wineContainerController;

    public ContainersController(ViewContainers viewContainers,
                                ContainersManager containersManager,
                                ContainerPanelFactory<WinePrefixContainerPanel, WinePrefixDTO> wineContainerPanelFactory, WineContainerController wineContainerController) {
        this.viewContainers = viewContainers;
        this.containersManager = containersManager;
        this.wineContainerPanelFactory = wineContainerPanelFactory;
        this.wineContainerController = wineContainerController;

        viewContainers.setOnSelectionChanged(event -> {
            if (viewContainers.isSelected()) {
                loadContainers();
            }
        });

        viewContainers.setOnSelectContainer((ContainerDTO containerDTO) -> {
            final WinePrefixContainerPanel panel = wineContainerPanelFactory.createContainerPanel((WinePrefixDTO) containerDTO, viewContainers.getThemeManager());
            panel.setOnWineCfg(winePrefixDTO -> wineContainerController.runInPrefix(
                    winePrefixDTO,
                    "winecfg",
                    panel::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
            );

            panel.setOnRegedit(winePrefixDTO -> wineContainerController.runInPrefix(
                    winePrefixDTO,
                    "regedit",
                    panel::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
            );

            panel.setOnWineboot(winePrefixDTO -> wineContainerController.runInPrefix(
                    winePrefixDTO,
                    "wineboot",
                    panel::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
            );

            panel.setOnWinebootRepair(winePrefixDTO -> wineContainerController.repairPrefix(
                    winePrefixDTO,
                    panel::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
            );

            panel.setOnWineConsole(winePrefixDTO -> wineContainerController.runInPrefix(
                    winePrefixDTO,
                    "wineconsole",
                    panel::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
            );

            panel.setOnTaskMgr(winePrefixDTO -> wineContainerController.runInPrefix(
                    winePrefixDTO,
                    "taskmgr",
                    panel::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
            );

            panel.setOnUninstaller(winePrefixDTO -> wineContainerController.runInPrefix(
                    winePrefixDTO,
                    "uninstaller",
                    panel::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
            );

            panel.setOnKillProcess(winePrefixDTO -> wineContainerController.killProcesses(
                    winePrefixDTO,
                    panel::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()))
            );

            panel.setOnOpenTerminalInWinePrefix(winePrefix -> {
                wineContainerController.openTerminalInPrefix(winePrefix);
                panel.unlockAll();
            });

            panel.setOnChangeSetting((winePrefixDTO, setting) -> {
                wineContainerController.changeSetting(
                        winePrefixDTO,
                        setting,
                        panel::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show())
                );
                panel.unlockAll();
            });

            viewContainers.showRightView(panel);
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
