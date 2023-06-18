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

package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.phoenicis.javafx.JavaFXApplication;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.application.control.ApplicationsFeaturePanel;
import org.phoenicis.javafx.components.common.control.TabIndicator;
import org.phoenicis.javafx.components.container.control.ContainersFeaturePanel;
import org.phoenicis.javafx.components.installation.control.InstallationsFeaturePanel;
import org.phoenicis.javafx.components.library.control.LibraryFeaturePanel;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.themes.ThemeManager;
import org.phoenicis.javafx.utils.StringBindings;
import org.phoenicis.javafx.views.common.PhoenicisScene;
import org.phoenicis.javafx.views.mainwindow.engines.EnginesView;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;
import org.phoenicis.javafx.views.mainwindow.settings.SettingsView;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class MainWindow extends Stage {
    private TabPane tabPane;
    private Tab libraryTab;
    private Tab applicationsTab;
    private Tab containersTab;
    private Tab enginesTab;
    private Tab installationsTab;
    private Tab settingsTab;
    private JavaFxSettingsManager javaFxSettingsManager;

    public MainWindow(String applicationName,
            LibraryFeaturePanel library,
            ApplicationsFeaturePanel apps,
            EnginesView engines,
            ContainersFeaturePanel containers,
            InstallationsFeaturePanel installations,
            SettingsView settings,
            ThemeManager themeManager,
            JavaFxSettingsManager javaFxSettingsManager) {
        super();

        this.tabPane = new TabPane();
        this.tabPane.setId("menuPane");
        this.tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        this.javaFxSettingsManager = javaFxSettingsManager;

        if (this.javaFxSettingsManager.isAdvancedMode()) {
            this.libraryTab = createLibraryTab(library);
            this.applicationsTab = createApplicationsTab(apps);
            this.containersTab = createContainersTab(containers);
            this.enginesTab = engines;
            this.installationsTab = createInstallationsTab(installations);
            this.settingsTab = settings;
            this.tabPane.getTabs().addAll(
                    this.libraryTab,
                    this.applicationsTab,
                    this.containersTab,
                    this.enginesTab,
                    this.installationsTab,
                    this.settingsTab);
        } else {
            this.libraryTab = createLibraryTab(library);
            this.applicationsTab = createApplicationsTab(apps);
            this.installationsTab = createInstallationsTab(installations);
            this.settingsTab = settings;
            this.tabPane.getTabs().addAll(
                    this.libraryTab,
                    this.applicationsTab,
                    this.installationsTab,
                    this.settingsTab);
        }

        final Scene scene = new PhoenicisScene(this.tabPane, themeManager, this.javaFxSettingsManager);

        this.getIcons().add(new Image(
                JavaFXApplication.class.getResourceAsStream("/org/phoenicis/javafx/views/common/phoenicis.png")));

        // avoid 1x1 pixel window
        this.setMinHeight(200);
        this.setMinWidth(200);
        this.setResizable(true);
        this.setHeight(this.javaFxSettingsManager.getWindowHeight());
        this.setWidth(this.javaFxSettingsManager.getWindowWidth());
        this.setMaximized(this.javaFxSettingsManager.isWindowMaximized());
        this.setScene(scene);
        this.setTitle(applicationName);
        this.show();
    }

    private Tab createLibraryTab(LibraryFeaturePanel library) {
        final Tab libraryTab = new Tab(tr("Library"), library);

        libraryTab.setClosable(false);

        return libraryTab;
    }

    private Tab createApplicationsTab(ApplicationsFeaturePanel apps) {
        final Tab applicationsTab = new Tab(tr("Apps"), apps);

        applicationsTab.setClosable(false);

        return applicationsTab;
    }

    private Tab createContainersTab(ContainersFeaturePanel containers) {
        final Tab containersTab = new Tab(tr("Containers"), containers);

        containersTab.setClosable(false);

        containersTab.setOnSelectionChanged(event -> containers.getContainersManager().fetchContainers(
                containerCategories -> Platform.runLater(() -> {
                    containers.getCategories().setAll(containerCategories);
                    containers.setInitialized(true);
                }),
                e -> Platform.runLater(() -> {
                    final ErrorDialog errorDialog = ErrorDialog.builder()
                            .withMessage(tr("Loading containers failed."))
                            .withException(e)
                            .withOwner(containers.getScene().getWindow())
                            .build();

                    errorDialog.showAndWait();
                })));

        return containersTab;
    }

    private Tab createInstallationsTab(InstallationsFeaturePanel installationsFeaturePanel) {
        final Tab installationsTab = new Tab(tr("Installations"), installationsFeaturePanel);

        installationsTab.setClosable(false);

        final ConcatenatedList<InstallationDTO> installations = ConcatenatedList.create(
                new MappedList<>(installationsFeaturePanel.getInstallationCategories(),
                        InstallationCategoryDTO::getInstallations));

        // a binding containing the number of currently active installations
        final IntegerBinding openInstallations = Bindings.createIntegerBinding(installations::size, installations);

        final TabIndicator indicator = new TabIndicator();
        indicator.textProperty().bind(StringBindings.map(openInstallations, numberOfInstallations -> {
            if (numberOfInstallations.intValue() < 10) {
                return String.valueOf(numberOfInstallations);
            } else {
                return "+";
            }
        }));

        // only show the tab indicator if at least one active installation exists
        installationsTab.graphicProperty().bind(Bindings
                .when(Bindings.notEqual(openInstallations, 0))
                .then(indicator).otherwise(new SimpleObjectProperty<>()));

        return installationsTab;
    }

    public void showInstallations() {
        if (this.javaFxSettingsManager.isAdvancedMode()) {
            this.tabPane.getSelectionModel().select(4);
        } else {
            this.tabPane.getSelectionModel().select(2);
        }
    }

    public Tab getLibraryTab() {
        return this.libraryTab;
    }

    public Tab getApplicationsTab() {
        return this.applicationsTab;
    }

    public Tab getContainersTab() {
        return this.containersTab;
    }

    public Tab getEnginesTab() {
        return this.enginesTab;
    }

    public Tab getInstallationsTab() {
        return this.installationsTab;
    }

    public Tab getSettingsTab() {
        return this.settingsTab;
    }
}
