package com.playonlinux.javafx.views.mainwindow;

import com.playonlinux.javafx.JavaFXApplication;
import com.playonlinux.javafx.views.common.PlayOnLinuxScene;
import com.playonlinux.javafx.views.mainwindow.apps.ViewApps;
import com.playonlinux.javafx.views.mainwindow.containers.ViewContainers;
import com.playonlinux.javafx.views.mainwindow.engines.ViewEngines;
import com.playonlinux.javafx.views.mainwindow.library.ViewLibrary;
import com.playonlinux.javafx.views.mainwindow.settings.ViewSettings;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Stage {
    private final MainWindowHeader headerPane;
    private final ViewLibrary library;
    private final ViewApps apps;
    private final ViewEngines engines;
    private final ViewContainers containers;
    private final ViewSettings settings;
    private final PlayOnLinuxScene scene;

    private VBox rootPane;


    public MainWindow(String applicationName,
                      ViewLibrary library,
                      ViewApps apps,
                      ViewEngines engines,
                      ViewContainers containers,
                      ViewSettings settings,
                      MainWindowHeader headerPane) {
        super();

        this.library = library;
        this.library.setOnTabOpened(this::getLibrary);

        this.headerPane = headerPane;
        this.apps = apps;
        this.engines = engines;
        this.containers = containers;
        this.settings = settings;

        rootPane = new VBox();
        scene = new PlayOnLinuxScene(rootPane);

        getLibrary();

        this.setScene(scene);
        this.setTitle(applicationName);
        this.getIcons().add(new Image(JavaFXApplication.class.getResourceAsStream("/com/playonlinux/javafx/views/common/playonlinux.png")));
        this.show();

        this.setUpEvents();
    }

    public ViewLibrary getLibrary() {
        goTo(library);
        return library;
    }

    private void setUpEvents() {
        this.headerPane.setLibraryEvent(evt -> goTo(library));
        this.headerPane.setAppsEvent(evt -> goTo(apps));
        this.headerPane.setEnginesEvent(evt -> goTo(engines));
        this.headerPane.setContainersEvent(evt -> goTo(containers));
        this.headerPane.setSettingsEvent(evt -> goTo(settings));
        library.setUpEvents();
        engines.setUpEvents();
    }

    private void goTo(Node view) {
        rootPane.getChildren().clear();
        rootPane.getChildren().addAll(headerPane, view);
    }
}
