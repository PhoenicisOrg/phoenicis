package com.playonlinux.javafx.controller.engines;

import com.playonlinux.engines.WineVersionsManager;
import com.playonlinux.javafx.views.mainwindow.engines.ViewEngines;
import javafx.application.Platform;

public class EnginesController {
    private final ViewEngines viewEngines;
    private final WineVersionsManager wineVersionsManager;

    public EnginesController(ViewEngines viewEngines, WineVersionsManager wineVersionsManager) {
        this.viewEngines = viewEngines;
        this.wineVersionsManager = wineVersionsManager;
    }

    public ViewEngines getView() {
        return viewEngines;
    }

    public void loadEngines() {
        wineVersionsManager.fetchAvailableWineVersions(versions -> Platform.runLater(() -> this.viewEngines.populate(versions)));
        this.viewEngines.showWineVersions();
    }
}
