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
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.javafx.views.mainwindow.MessagePanel;
import org.phoenicis.settings.SettingsManager;
import org.phoenicis.tools.system.opener.Opener;

public class ViewSettings extends MainWindowView<SettingsSideBar> {
    private final String applicationName;
    private final String applicationVersion;
    private final String applicationGitRevision;
    private final String applicationBuildTimestamp;
    private final Opener opener;

    private SettingsManager settingsManager;
    private RepositoryManager repositoryManager;

    private SettingsSideBar sideBar;

    private MessagePanel selectSettingsPanel;

    private ObservableList<SettingsSideBar.SettingsSideBarItem> settingsItems;

    public ViewSettings(ThemeManager themeManager, String applicationName, String applicationVersion,
            String applicationGitRevision, String applicationBuildTimestamp, Opener opener,
            SettingsManager settingsManager, RepositoryManager repositoryManager) {
        super("Settings", themeManager);
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.applicationGitRevision = applicationGitRevision;
        this.applicationBuildTimestamp = applicationBuildTimestamp;
        this.opener = opener;
        this.settingsManager = settingsManager;
        this.repositoryManager = repositoryManager;

        this.initializeSettingsItems();

        this.sideBar = new SettingsSideBar();

        this.sideBar.setOnSelectSettingsItem(this::setCenter);

        this.sideBar.bindSettingsItems(this.settingsItems);

        this.setSideBar(sideBar);
        this.sideBar.selectFirstSettingsCategory();
    }

    private void initializeSettingsItems() {
        AboutPanel.ApplicationBuildInformation buildInformation = new AboutPanel.ApplicationBuildInformation(
                applicationName, applicationVersion, applicationGitRevision, applicationBuildTimestamp);

        this.settingsItems = FXCollections.observableArrayList(
                new SettingsSideBar.SettingsSideBarItem(new UserInterfacePanel(settingsManager, themeManager),
                        "userInterfaceButton", "User Interface"),
                new SettingsSideBar.SettingsSideBarItem(new RepositoriesPanel(settingsManager, repositoryManager),
                        "repositoriesButton", "Repositories"),
                new SettingsSideBar.SettingsSideBarItem(new FileAssociationsPanel(), "settingsButton",
                        "File Associations"),
                new SettingsSideBar.SettingsSideBarItem(new NetworkPanel(), "networkButton", "Network"),
                new SettingsSideBar.SettingsSideBarItem(new AboutPanel(buildInformation, opener), "aboutButton",
                        "About"));
    }
}
