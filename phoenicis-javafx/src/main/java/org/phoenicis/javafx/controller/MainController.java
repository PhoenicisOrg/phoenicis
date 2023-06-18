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
import javafx.beans.value.ChangeListener;
import org.phoenicis.javafx.components.installation.control.InstallationsFeaturePanel;
import org.phoenicis.javafx.controller.apps.AppsController;
import org.phoenicis.javafx.controller.containers.ContainersController;
import org.phoenicis.javafx.controller.engines.EnginesController;
import org.phoenicis.javafx.controller.library.LibraryController;
import org.phoenicis.javafx.controller.settings.SettingsController;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.themes.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindow;
import org.phoenicis.repository.RepositoryManager;

public class MainController {
    private final MainWindow mainWindow;
    private final JavaFxSettingsManager javaFxSettingsManager;

    private String applicationName;

    public MainController(String applicationName,
            LibraryController libraryController,
            AppsController appsController,
            EnginesController enginesController,
            ContainersController containersController,
            InstallationsFeaturePanel installationsView,
            SettingsController settingsController,
            RepositoryManager repositoryManager,
            ThemeManager themeManager,
            JavaFxSettingsManager javaFxSettingsManager) {
        super();

        this.applicationName = applicationName;

        this.mainWindow = new MainWindow(applicationName,
                libraryController.getView(),
                appsController.getView(),
                enginesController.getView(),
                containersController.getView(),
                installationsView,
                settingsController.getView(),
                themeManager,
                javaFxSettingsManager);

        this.javaFxSettingsManager = javaFxSettingsManager;

        installationsView.setOnInstallationAdded(this.mainWindow::showInstallations);

        // load repository only if it is really required, i.e.
        // - if one of the following tabs is opened
        // and
        // - if the repository has not been loaded already (avoid reload by opening the tab several times)
        //
        // tabs:
        // - apps (apps are stored in the repository)
        // - containers (engine settings etc.)
        final ChangeListener<Boolean> tabSelectedListener = (observable, oldValue, newValue) -> {
            if (newValue && !repositoryManager.isRepositoryLoaded()) {
                repositoryManager.triggerRepositoryChange();
            }
        };
        this.mainWindow.getApplicationsTab().selectedProperty().addListener(tabSelectedListener);
        if (javaFxSettingsManager.isAdvancedMode()) {
            this.mainWindow.getContainersTab().selectedProperty().addListener(tabSelectedListener);
        }
    }

    public void show() {
        this.mainWindow.show();
    }

    public void setOnClose(Runnable onClose) {
        this.mainWindow.setOnCloseRequest(event -> {
            this.javaFxSettingsManager.setWindowHeight(this.mainWindow.getHeight());
            this.javaFxSettingsManager.setWindowWidth(this.mainWindow.getWidth());
            this.javaFxSettingsManager.setWindowMaximized(this.mainWindow.isMaximized());
            this.javaFxSettingsManager.save();

            Platform.exit();

            onClose.run();
        });
    }
}
