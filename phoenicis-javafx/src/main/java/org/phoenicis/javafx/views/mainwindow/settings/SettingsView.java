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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.settings.SettingsManager;
import org.phoenicis.tools.system.opener.Opener;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class SettingsView extends MainWindowView<SettingsSidebar> {
    private final String applicationName;
    private final String applicationVersion;
    private final String applicationGitRevision;
    private final String applicationBuildTimestamp;
    private final Opener opener;

    private SettingsManager settingsManager;
    private JavaFxSettingsManager javaFxSettingsManager;
    private RepositoryManager repositoryManager;

    private ObservableList<SettingsSidebar.SettingsSidebarItem> settingsItems;

    public SettingsView(ThemeManager themeManager, String applicationName, String applicationVersion,
            String applicationGitRevision, String applicationBuildTimestamp, Opener opener,
            SettingsManager settingsManager,
            JavaFxSettingsManager javaFxSettingsManager, RepositoryManager repositoryManager) {
        super(tr("Settings"), themeManager);
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.applicationGitRevision = applicationGitRevision;
        this.applicationBuildTimestamp = applicationBuildTimestamp;
        this.opener = opener;
        this.settingsManager = settingsManager;
        this.javaFxSettingsManager = javaFxSettingsManager;
        this.repositoryManager = repositoryManager;

        this.initializeSettingsItems();

        this.sidebar = new SettingsSidebar();

        this.sidebar.setOnSelectSettingsItem(this::setCenter);

        this.sidebar.bindSettingsItems(this.settingsItems);

        this.setSidebar(this.sidebar);
    }

    private void initializeSettingsItems() {
        AboutPanel.ApplicationBuildInformation buildInformation = new AboutPanel.ApplicationBuildInformation(
                applicationName, applicationVersion, applicationGitRevision, applicationBuildTimestamp);

        this.settingsItems = FXCollections.observableArrayList(
                new SettingsSidebar.SettingsSidebarItem(new UserInterfacePanel(javaFxSettingsManager, themeManager),
                        "userInterfaceButton", tr("User Interface")),
                new SettingsSidebar.SettingsSidebarItem(new RepositoriesPanel(settingsManager, repositoryManager),
                        "repositoriesButton", tr("Repositories")),
                new SettingsSidebar.SettingsSidebarItem(new FileAssociationsPanel(), "settingsButton",
                        tr("File Associations")),
                new SettingsSidebar.SettingsSidebarItem(new NetworkPanel(), "networkButton", tr("Network")),
                new SettingsSidebar.SettingsSidebarItem(new AboutPanel(buildInformation, opener), "aboutButton",
                        tr("About")));
    }
}
