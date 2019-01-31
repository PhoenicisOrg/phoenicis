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

package org.phoenicis.javafx.views.mainwindow.settings;

import javafx.animation.PauseTransition;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import org.phoenicis.javafx.components.setting.control.RepositoriesPanel;
import org.phoenicis.javafx.components.setting.control.*;
import org.phoenicis.javafx.components.setting.utils.ApplicationBuildInformation;
import org.phoenicis.javafx.components.setting.utils.SettingsSidebarItem;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.themes.Theme;
import org.phoenicis.javafx.views.common.themes.Themes;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;
import org.phoenicis.repository.RepositoryLocationLoader;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.types.Repository;
import org.phoenicis.settings.SettingsManager;
import org.phoenicis.tools.system.opener.Opener;

import java.util.stream.IntStream;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class SettingsView extends MainWindowView<SettingsSidebar> {
    private final PauseTransition pause = new PauseTransition(Duration.seconds(0.5));

    private final String applicationName;
    private final String applicationVersion;
    private final String applicationGitRevision;
    private final String applicationBuildTimestamp;
    private final Opener opener;

    private final RepositoryLocationLoader repositoryLocationLoader;

    private SettingsManager settingsManager;
    private JavaFxSettingsManager javaFxSettingsManager;
    private RepositoryManager repositoryManager;

    private ObservableList<SettingsSidebarItem> settingsItems;

    public SettingsView(ThemeManager themeManager, String applicationName, String applicationVersion,
            String applicationGitRevision, String applicationBuildTimestamp, Opener opener,
            SettingsManager settingsManager, RepositoryLocationLoader repositoryLocationLoader,
            JavaFxSettingsManager javaFxSettingsManager, RepositoryManager repositoryManager) {
        super(tr("Settings"), themeManager);
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.applicationGitRevision = applicationGitRevision;
        this.applicationBuildTimestamp = applicationBuildTimestamp;
        this.opener = opener;
        this.settingsManager = settingsManager;
        this.repositoryLocationLoader = repositoryLocationLoader;
        this.javaFxSettingsManager = javaFxSettingsManager;
        this.repositoryManager = repositoryManager;

        this.initializeSettingsItems();

        this.sidebar = createSidebar();

        this.setSidebar(sidebar);

    }

    private SettingsSidebar createSidebar() {
        final SettingsSidebar sidebar = new SettingsSidebar(this.settingsItems);

        sidebar.selectedItemProperty()
                .addListener((Observable invalidation) -> setCenter(sidebar.getSelectedItem().getPanel()));

        return sidebar;
    }

    private void initializeSettingsItems() {
        final ApplicationBuildInformation buildInformation = new ApplicationBuildInformation(
                this.applicationName, this.applicationVersion, this.applicationGitRevision,
                this.applicationBuildTimestamp);

        this.settingsItems = FXCollections.observableArrayList(
                new SettingsSidebarItem(createUserInterfacePanel(),
                        "userInterfaceButton", tr("User Interface")),
                new SettingsSidebarItem(
                        createRepositoriesPanel(),
                        "repositoriesButton", tr("Repositories")),
                new SettingsSidebarItem(new FileAssociationsPanel(), "settingsButton",
                        tr("File Associations")),
                new SettingsSidebarItem(new NetworkPanel(), "networkButton", tr("Network")),
                new SettingsSidebarItem(new AboutPanel(this.opener, buildInformation), "aboutButton",
                        tr("About")));
    }

    private RepositoriesPanel createRepositoriesPanel() {
        ObservableList<RepositoryLocation<? extends Repository>> repositoryLocations = FXCollections
                .observableArrayList(settingsManager.loadRepositoryLocations());

        final RepositoriesPanel repositoriesPanel = new RepositoriesPanel(repositoryLocations);

        // set the initial values
        repositoriesPanel.setOnRepositoryRefresh(repositoryManager::triggerRepositoryChange);

        repositoriesPanel.setRepositoryLocationLoader(repositoryLocationLoader);

        // react on changes
        repositoriesPanel.getRepositoryLocations()
                .addListener((ListChangeListener.Change<? extends RepositoryLocation<? extends Repository>> change) -> {
                    while (change.next()) {
                        final int from = change.getFrom();

                        if (change.wasRemoved()) {
                            change.getRemoved().forEach(repositoryManager::removeRepositories);
                        }

                        if (change.wasAdded()) {
                            IntStream.range(0, change.getAddedSize()).forEach(index -> repositoryManager
                                    .addRepositories(from + index, change.getAddedSubList().get(index)));
                        }

                        settingsManager.saveRepositories(repositoryLocations);
                    }
                });

        return repositoriesPanel;
    }

    private UserInterfacePanel createUserInterfacePanel() {
        final UserInterfacePanel userInterfacePanel = new UserInterfacePanel(
                FXCollections.observableArrayList(Themes.all()));

        // set the initial values
        userInterfacePanel.setScaling(javaFxSettingsManager.getScale());
        userInterfacePanel.setSelectedTheme(
                Themes.fromShortName(javaFxSettingsManager.getTheme()).orElse(Themes.STANDARD));
        userInterfacePanel.setShowScriptSource(javaFxSettingsManager.isViewScriptSource());

        userInterfacePanel.setOnRestoreSettings(javaFxSettingsManager::restoreDefault);

        // react on changes
        userInterfacePanel.scalingProperty().addListener((Observable invalidation) -> {
            final double scaling = userInterfacePanel.getScaling();

            this.pause.setOnFinished(event -> {
                getTabPane().getScene().getRoot().setStyle(String.format("-fx-font-size: %.2fpt;", scaling));

                javaFxSettingsManager.setScale(userInterfacePanel.getScaling());
                javaFxSettingsManager.save();
            });

            this.pause.playFromStart();
        });

        userInterfacePanel.selectedThemeProperty().addListener((Observable invalidation) -> {
            final Theme selectedTheme = userInterfacePanel.getSelectedTheme();

            themeManager.setCurrentTheme(selectedTheme);

            javaFxSettingsManager.setTheme(selectedTheme.getShortName());
            javaFxSettingsManager.save();
        });

        userInterfacePanel.showScriptSourceProperty().addListener((Observable invalidation) -> {
            final boolean showScriptSource = userInterfacePanel.isShowScriptSource();

            javaFxSettingsManager.setViewScriptSource(showScriptSource);
            javaFxSettingsManager.save();
        });

        return userInterfacePanel;
    }
}
