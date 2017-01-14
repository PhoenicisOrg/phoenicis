package com.playonlinux.javafx.views.mainwindow;

import com.playonlinux.javafx.JavaFXApplication;
import com.playonlinux.javafx.views.common.PlayOnLinuxScene;
import com.playonlinux.javafx.views.common.widget.PlayOnLinuxLogo;
import com.playonlinux.javafx.views.mainwindow.apps.ViewApps;
import com.playonlinux.javafx.views.mainwindow.containers.ViewContainers;
import com.playonlinux.javafx.views.mainwindow.engines.ViewEngines;
import com.playonlinux.javafx.views.mainwindow.library.ViewLibrary;
import com.playonlinux.javafx.views.mainwindow.settings.ViewSettings;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainWindow extends Stage {
    private final ViewLibrary library;
    private final ViewApps apps;
    private final ViewEngines engines;
    private final ViewContainers containers;
    private final ViewSettings settings;
    private final PlayOnLinuxScene scene;

    private TabPane tabPane;


    public MainWindow(String applicationName,
                      String theme,
                      ViewLibrary library,
                      ViewApps apps,
                      ViewEngines engines,
                      ViewContainers containers,
                      ViewSettings settings,
                      PlayOnLinuxLogo playOnLinuxLogo) {
        super();

        this.library = library;
        this.apps = apps;
        this.engines = engines;
        this.containers = containers;
        this.settings = settings;

        tabPane = new TabPane();
        tabPane.setTabMinHeight(50);
        tabPane.setId("menuPane");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab logoTab = new Tab();
        playOnLinuxLogo.setMinWidth(200);
        logoTab.setGraphic(playOnLinuxLogo);
        logoTab.setDisable(true);

        tabPane.getTabs().addAll(logoTab, library, apps, containers, engines, settings);

        scene = new PlayOnLinuxScene(tabPane, theme);

        this.getIcons().add(new Image(JavaFXApplication.class.getResourceAsStream("/com/playonlinux/javafx/views/common/playonlinux.png")));

        this.setResizable(true);
        this.setScene(scene);
        this.setTitle(applicationName);
        this.show();

        this.setUpEvents();
    }

    private void setUpEvents() {
        library.setUpEvents();
        engines.setUpEvents();
    }

    public void showLibrary() {
        tabPane.getSelectionModel().select(1);
    }
}
