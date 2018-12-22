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
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class MainController {
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private final MainWindow mainWindow;
    private final ThemeManager themeManager;
    private final JavaFxSettingsManager javaFxSettingsManager;

    private String applicationName;

    public MainController(String applicationName,
            LibraryController libraryController,
            AppsController appsController,
            EnginesController enginesController,
            ContainersController containersController,
            InstallationsController installationsController,
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
                installationsController.getView(),
                settingsController.getView(),
                themeManager,
                javaFxSettingsManager);

        this.themeManager = themeManager;
        this.javaFxSettingsManager = javaFxSettingsManager;

        repositoryManager.addCallbacks(this::setDefaultCategoryIcons, e -> {
        });

        installationsController.setOnInstallationAdded(this.mainWindow::showInstallations);

        appsController.setOnAppLoaded(containersController::loadContainers);
    }

    public void show() {
        this.mainWindow.show();
    }

    public void setOnClose(Runnable onClose) {
        this.mainWindow.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setResizable(true);
            alert.initOwner(this.mainWindow);
            alert.setTitle(this.applicationName);
            alert.setHeaderText(tr("Are you sure you want to close all {0} windows?", this.applicationName));
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

    private void setDefaultCategoryIcons(RepositoryDTO repositoryDTO) {
        Platform.runLater(() -> {
            try {
                List<CategoryDTO> categoryDTOS = repositoryDTO.getTypes().get(0).getCategories();
                StringBuilder cssBuilder = new StringBuilder();
                for (CategoryDTO category : categoryDTOS) {
                    cssBuilder.append("#" + category.getId().toLowerCase() + "Button{\n");
                    URI categoryIcon = category.getIcon();
                    if (categoryIcon == null) {
                        cssBuilder
                                .append("-fx-background-image: url('/org/phoenicis/javafx/views/common/phoenicis.png');\n");
                    } else {
                        cssBuilder.append("-fx-background-image: url('" + categoryIcon + "');\n");
                    }
                    cssBuilder.append("}\n");
                }
                String css = cssBuilder.toString();
                Path temp = Files.createTempFile("defaultCategoryIcons", ".css").toAbsolutePath();
                File tempFile = temp.toFile();
                tempFile.deleteOnExit();
                Files.write(temp, css.getBytes());
                String defaultCategoryIconsCss = temp.toUri().toString();
                this.themeManager.setDefaultCategoryIconsCss(defaultCategoryIconsCss);
            } catch (IOException e) {
                LOGGER.warn("Could not set default category icons.", e);
            }
        });
    }
}
