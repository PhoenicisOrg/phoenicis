package com.playonlinux.javafx.controller.apps;

import com.playonlinux.apps.ApplicationsSource;
import com.playonlinux.javafx.views.mainwindow.apps.ViewApps;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;

public class AppsController {
    private final ViewApps view;
    private final ApplicationsSource localAppsManager;
    private final ScriptInterpreter scriptInterpreter;

    private Runnable onAppLoaded = () -> {};

    public AppsController(ViewApps view,
                          ApplicationsSource localAppsManager,
                          ScriptInterpreter scriptInterpreter) {
        this.view = view;
        this.localAppsManager = localAppsManager;
        this.scriptInterpreter = scriptInterpreter;
    }

    public void loadApps() {
        this.view.showWait();
        localAppsManager.fetchInstallableApplications(
                this.view::populate,
                e -> this.view.showFailure()
        );

        this.view.setOnRetryButtonClicked(event -> {
            this.view.showWait();
            localAppsManager.fetchInstallableApplications(
                    this.view::populate,
                    e -> this.view.showFailure()
            );
        });
        this.view.setOnSelectCategory(categoryDTO -> this.view.populateApps(categoryDTO.getApplications()));
        this.view.setOnSelectScript(scriptDTO -> scriptInterpreter.runScript(
                scriptDTO.getScript(),
                Throwable::printStackTrace // FIXME
        ));

        onAppLoaded.run();
    }

    public void setOnAppLoaded(Runnable onAppLoaded) {
        this.onAppLoaded = onAppLoaded;
    }

    public ViewApps getView() {
        return view;
    }
}
