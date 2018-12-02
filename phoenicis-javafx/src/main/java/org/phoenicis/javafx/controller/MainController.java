/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.javafx.controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.phoenicis.javafx.controller.apps.AppsController;
import org.phoenicis.javafx.controller.containers.ContainersController;
import org.phoenicis.javafx.controller.engines.EnginesController;
import org.phoenicis.javafx.controller.installations.InstallationsController;
import org.phoenicis.javafx.controller.library.LibraryController;
import org.phoenicis.javafx.controller.settings.SettingsController;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindow;

import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class MainController {
    private final MainWindow mainWindow;
    private final JavaFxSettingsManager javaFxSettingsManager;

    private String applicationName;

    public MainController(String applicationName,
            LibraryController libraryController,
            AppsController appsController,
            EnginesController enginesController,
            ContainersController containersController,
            InstallationsController installationsController,
            SettingsController settingsController,
            ThemeManager themeManager,
            JavaFxSettingsManager javaFxSettingsManager) {
        super();

        this.applicationName = applicationName;

        this.mainWindow = new MainWindow(applicationName,
                libraryController.getView(),
                appsController.getView(),
                enginesController.getView(),
                containersController.getView(),
                installationsController.getView(),
                settingsController.getView(),
                themeManager,
                javaFxSettingsManager);

        this.javaFxSettingsManager = javaFxSettingsManager;

        installationsController.setOnInstallationAdded(mainWindow::showInstallations);

        appsController.setOnAppLoaded(() -> {
            containersController.loadContainers();
        });

        appsController.loadApps();
    }

    public void show() {
        mainWindow.show();
    }

    public void setOnClose(Runnable onClose) {
        this.mainWindow.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setResizable(true);
            alert.initOwner(this.mainWindow);
            alert.setTitle(applicationName);
            alert.setHeaderText(tr("Are you sure you want to close all {0} windows?", applicationName));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                this.javaFxSettingsManager.setWindowHeight(this.mainWindow.getHeight());
                this.javaFxSettingsManager.setWindowWidth(this.mainWindow.getWidth());
                this.javaFxSettingsManager.setWindowMaximized(this.mainWindow.isMaximized());
                this.javaFxSettingsManager.save();
                Platform.exit();
                onClose.run();
            } else {
                event.consume();
            }
        });
    }
}
