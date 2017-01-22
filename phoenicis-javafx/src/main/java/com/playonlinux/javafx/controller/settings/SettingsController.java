package com.playonlinux.javafx.controller.settings;

import com.phoenicis.settings.SettingsManager;
import com.playonlinux.javafx.views.mainwindow.settings.ViewSettings;

public class SettingsController {
    private final ViewSettings view;
    private final SettingsManager settingsManager;

    public SettingsController(ViewSettings view,
                              SettingsManager settingsManager) {
        this.view = view;
        this.settingsManager = settingsManager;
        this.view.setOnSave(settingsManager::save);
        this.view.setSettings(this.settingsManager.load());
    }

    public ViewSettings getView() {
        return view;
    }
}
