/*
 * Copyright (C) 2015 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.playonlinux.ui.impl.javafx.mainwindow;

import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

import static com.playonlinux.domain.Localisation.translate;

class MenuBar extends javafx.scene.control.MenuBar {
    private final MainWindow parent;

    private MenuItem openScript;

    private final static Logger logger = Logger.getLogger(MenuBar.class);

    public MenuBar(MainWindow parent) {
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

            try {
                parent.getMainEventHandler().runLocalScript(scriptToRun);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(translate("Error while trying to run the script."));
                alert.setContentText("The file was not found");
                logger.warn("Error while trying to run the script", e);
            }
        });
    }
}
