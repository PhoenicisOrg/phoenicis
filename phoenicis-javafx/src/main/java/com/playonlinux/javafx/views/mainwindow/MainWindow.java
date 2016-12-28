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

    private TabPane rootPane;


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

        rootPane = new TabPane();
        rootPane.setTabMinHeight(50);
        rootPane.setId("menuPane");
        rootPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab logoTab = new Tab();
        playOnLinuxLogo.setMinWidth(200);
        logoTab.setGraphic(playOnLinuxLogo);
        logoTab.setDisable(true);

        Tab libraryTab = new Tab("Library");
        libraryTab.setContent(library);

        Tab appsTab = new Tab("Apps");
        appsTab.setContent(apps);

        Tab containersTab = new Tab("Containers");
        containersTab.setContent(containers);

        Tab enginesTab = new Tab("Engines");
        enginesTab.setContent(engines);

        Tab settingsTab = new Tab("Settings");
        settingsTab.setContent(settings);

        rootPane.getTabs().addAll(logoTab, libraryTab, appsTab, containersTab, enginesTab, settingsTab);

        scene = new PlayOnLinuxScene(rootPane, theme);

        this.setResizable(true);
        this.setScene(scene);
        this.setTitle(applicationName);
        this.getIcons().add(new Image(JavaFXApplication.class.getResourceAsStream("/com/playonlinux/javafx/views/common/playonlinux.png")));
        this.show();

        this.setUpEvents();
    }

    private void setUpEvents() {
        library.setUpEvents();
        engines.setUpEvents();
    }
}
