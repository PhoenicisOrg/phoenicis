package com.playonlinux.javafx.controller.apps;

import com.playonlinux.apps.AppsManager;
import com.playonlinux.apps.LocalAppsManager;
import com.playonlinux.javafx.views.mainwindow.apps.ViewApps;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;

public class AppsController {
    private final ViewApps view;
    private final AppsManager localAppsManager;
    private final ScriptInterpreter scriptInterpreter;

    public AppsController(ViewApps view,
                          AppsManager localAppsManager,
                          ScriptInterpreter scriptInterpreter) {
        this.view = view;
        this.localAppsManager = localAppsManager;
        this.scriptInterpreter = scriptInterpreter;

        this.view.showAvailableApps();
        this.view.populate(localAppsManager.fetchInstallableApplications());
        this.view.setOnSelectCategory(categoryDTO -> this.view.populateApps(categoryDTO.getApplications()));
        this.view.setOnSelectScript(scriptDTO -> scriptInterpreter.runScript(
                scriptDTO.getScript(),
                Throwable::printStackTrace // FIXME
        ));
    }

    public ViewApps getView() {
        return view;
    }
}
