package com.playonlinux.javafx.controller.containers;

import com.playonlinux.containers.ContainersManager;
import com.playonlinux.containers.dto.ContainerDTO;
import com.playonlinux.containers.dto.WinePrefixDTO;
import com.playonlinux.containers.wine.WineContainerController;
import com.playonlinux.javafx.views.common.ErrorMessage;
import com.playonlinux.javafx.views.mainwindow.containers.ContainerPanelFactory;
import com.playonlinux.javafx.views.mainwindow.containers.ViewContainers;
import com.playonlinux.javafx.views.mainwindow.containers.WinePrefixContainerPanel;
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
            final WinePrefixContainerPanel panel = wineContainerPanelFactory.createContainerPanel((WinePrefixDTO) containerDTO);
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
