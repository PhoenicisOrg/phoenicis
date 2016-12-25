package com.playonlinux.javafx.controller.containers;

import com.playonlinux.containers.ContainersManager;
import com.playonlinux.javafx.views.mainwindow.containers.ViewContainers;

public class ContainersController {
    private final ViewContainers viewContainers;
    private final ContainersManager containersManager;

    public ContainersController(ViewContainers viewContainers, ContainersManager containersManager) {
        this.viewContainers = viewContainers;
        this.containersManager = containersManager;
    }

    public ViewContainers getView() {
        return viewContainers;
    }

    public void loadContainers() {
        containersManager.fetchContainers(viewContainers::populate, Throwable::printStackTrace);
    }
}
