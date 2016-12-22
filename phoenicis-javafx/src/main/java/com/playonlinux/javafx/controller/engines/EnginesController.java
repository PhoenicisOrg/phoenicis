package com.playonlinux.javafx.controller.engines;

import com.playonlinux.engines.WineVersionsFetcher;
import com.playonlinux.javafx.views.mainwindow.engines.ViewEngines;

public class EnginesController {
    private final ViewEngines viewEngines;
    private final WineVersionsFetcher wineVersionsFetcher;

    public EnginesController(ViewEngines viewEngines, WineVersionsFetcher wineVersionsFetcher) {
        this.viewEngines = viewEngines;
        this.wineVersionsFetcher = wineVersionsFetcher;
    }

    public ViewEngines getView() {
        return viewEngines;
    }

    public void loadEngines() {
        this.viewEngines.populate(wineVersionsFetcher.fetchAvailableWineVersions());
        this.viewEngines.showWineVersions();
    }
}
