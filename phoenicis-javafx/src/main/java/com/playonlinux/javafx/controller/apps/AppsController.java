package com.playonlinux.javafx.controller.apps;

import com.playonlinux.apps.ApplicationsSource;
import com.playonlinux.apps.dto.ApplicationDTO;
import com.playonlinux.javafx.views.common.ErrorMessage;
import com.playonlinux.javafx.views.mainwindow.apps.ViewApps;
import com.playonlinux.scripts.interpreter.ScriptInterpreter;
import javafx.application.Platform;

import java.util.ArrayList;

public class AppsController {
    private final ViewApps view;
    private final ApplicationsSource appsSource;
    private final ScriptInterpreter scriptInterpreter;

    private Runnable onAppLoaded = () -> {};

    public AppsController(ViewApps view,
                          ApplicationsSource appsSource,
                          ScriptInterpreter scriptInterpreter) {
        this.view = view;
        this.appsSource = appsSource;
        this.scriptInterpreter = scriptInterpreter;
    }

    public void loadApps() {
        this.view.showWait();
        appsSource.fetchInstallableApplications(
                this.view::populate,
                e -> this.view.showFailure()
        );

        this.view.setOnRetryButtonClicked(event -> {
            this.view.showWait();
            appsSource.fetchInstallableApplications(
                    this.view::populate,
                    e -> this.view.showFailure()
            );
        });
        this.view.setOnSelectCategory(categoryDTO -> this.view.populateApps(
                new ArrayList<>(categoryDTO.getApplications().values())));
        this.view.setOnSelectScript(scriptDTO -> scriptInterpreter.runScript(
                scriptDTO.getScript(),
                e -> Platform.runLater(() -> new ErrorMessage("The script ended unexpectedly", e))
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
