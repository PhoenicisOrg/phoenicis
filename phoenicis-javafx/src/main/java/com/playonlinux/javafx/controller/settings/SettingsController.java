package com.playonlinux.javafx.controller.settings;

import com.phoenicis.settings.SettingsSaver;
import com.playonlinux.javafx.views.mainwindow.settings.ViewSettings;

public class SettingsController {
    private final ViewSettings view;
    private final SettingsSaver settingsSaver;

    public SettingsController(ViewSettings view,
                              SettingsSaver settingsSaver) {
        this.view = view;
        this.settingsSaver = settingsSaver;
        this.view.setOnSave(settings -> settingsSaver.save(settings));
        this.view.setSettings(this.settingsSaver.load());
    }

    public ViewSettings getView() {
        return view;
    }
}
