package com.playonlinux.ui.javafximpl.mainwindow;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;

public class MenuBar extends javafx.scene.control.MenuBar {
    private final JavaFXMainWindowImplementation parent;

    private MenuItem openScript;

    public MenuBar(JavaFXMainWindowImplementation parent) {
        this.parent = parent;

        buildFileMenu();
        buildToolsMenu();
        buildHelpMenu();

        this.setEvents();
        this.useSystemMenuBarProperty().set(true);
    }

    private void buildFileMenu() {
        final Menu fileMenu = new Menu("File");
        this.getMenus().add(fileMenu);
    }

    private void buildToolsMenu() {
        final Menu toolsMenu = new Menu("Tools");
        openScript = new MenuItem("Run a local script");

        toolsMenu.getItems().addAll(openScript);
        this.getMenus().add(toolsMenu);
    }

    private void buildHelpMenu() {
        final Menu helpMenu = new Menu("Help");

        this.getMenus().add(helpMenu);
    }

    void setEvents() {
        openScript.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open a script");
            File scriptToRun = fileChooser.showOpenDialog(parent);

            this.parent.getEventHandler().runLocalScript(scriptToRun);
        });
    }
}
