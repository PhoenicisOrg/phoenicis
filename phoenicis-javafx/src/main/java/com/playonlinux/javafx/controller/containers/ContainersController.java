package com.playonlinux.javafx.controller.containers;

import com.playonlinux.containers.ContainersManager;
import com.playonlinux.containers.dto.WinePrefixDTO;
import com.playonlinux.javafx.views.mainwindow.containers.ContainerPanelFactory;
import com.playonlinux.javafx.views.mainwindow.containers.ViewContainers;
import com.playonlinux.javafx.views.mainwindow.containers.WinePrefixContainerPanel;

public class ContainersController {
    private final ViewContainers viewContainers;
    private final ContainersManager containersManager;
    private final ContainerPanelFactory<WinePrefixContainerPanel, WinePrefixDTO> wineContainerPanelFactory;

    public ContainersController(ViewContainers viewContainers,
                                ContainersManager containersManager,
                                ContainerPanelFactory<WinePrefixContainerPanel, WinePrefixDTO> wineContainerPanelFactory) {
        this.viewContainers = viewContainers;
        this.containersManager = containersManager;
        this.wineContainerPanelFactory = wineContainerPanelFactory;

        viewContainers.setOnSelectContainer(containerDTO -> {
            WinePrefixContainerPanel panel = wineContainerPanelFactory.createContainerPanel((WinePrefixDTO) containerDTO);
            viewContainers.showRightView(panel);
        });
    }

    public ViewContainers getView() {
        return viewContainers;
    }

    public void loadContainers() {
        containersManager.fetchContainers(viewContainers::populate, Throwable::printStackTrace);
    }
}
