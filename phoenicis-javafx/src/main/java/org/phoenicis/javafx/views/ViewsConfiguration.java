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

package org.phoenicis.javafx.views;

import org.phoenicis.javafx.settings.JavaFxSettingsConfiguration;
import org.phoenicis.javafx.views.common.ThemeConfiguration;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationsView;
import org.phoenicis.javafx.views.mainwindow.console.ConsoleTabFactory;
import org.phoenicis.javafx.views.mainwindow.containers.ContainersView;
import org.phoenicis.javafx.views.mainwindow.engines.EnginesView;
import org.phoenicis.javafx.views.mainwindow.installations.InstallationsView;
import org.phoenicis.javafx.views.mainwindow.library.LibraryView;
import org.phoenicis.javafx.views.mainwindow.library.ViewsConfigurationLibrary;
import org.phoenicis.javafx.views.mainwindow.settings.SettingsView;
import org.phoenicis.repository.RepositoryConfiguration;
import org.phoenicis.settings.SettingsConfiguration;
import org.phoenicis.tools.ToolsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ViewsConfigurationLibrary.class)
public class ViewsConfiguration {
    @Value("${application.name}")
    private String applicationName;

    @Value("${application.version}")
    private String applicationVersion;

    @Value("${application.gitRevision}")
    private String applicationGitRevision;

    @Value("${application.buildTimestamp}")
    private String applicationBuildTimestamp;

    @Value("${application.user.engines}")
    private String enginesPath;

    @Autowired
    private ViewsConfigurationLibrary viewsConfigurationLibrary;

    @Autowired
    private ThemeConfiguration themeConfiguration;

    @Autowired
    private ToolsConfiguration toolsConfiguration;

    @Autowired
    private SettingsConfiguration settingsConfiguration;

    @Autowired
    private JavaFxSettingsConfiguration javaFxSettingsConfiguration;

    @Autowired
    private RepositoryConfiguration repositoryConfiguration;

    @Bean
    public ApplicationsView viewApps() {
        return new ApplicationsView(themeConfiguration.themeManager(),
                javaFxSettingsConfiguration.javaFxSettingsManager(),
                toolsConfiguration);
    }

    @Bean
    public EnginesView viewEngines() {
        return new EnginesView(themeConfiguration.themeManager(), enginesPath,
                javaFxSettingsConfiguration.javaFxSettingsManager());
    }

    @Bean
    public ContainersView viewContainers() {
        return new ContainersView(themeConfiguration.themeManager(),
                javaFxSettingsConfiguration.javaFxSettingsManager());
    }

    @Bean
    public InstallationsView viewInstallations() {
        return new InstallationsView(themeConfiguration.themeManager(),
                javaFxSettingsConfiguration.javaFxSettingsManager());
    }

    @Bean
    public SettingsView viewSettings() {
        return new SettingsView(themeConfiguration.themeManager(),
                applicationName, applicationVersion,
                applicationGitRevision, applicationBuildTimestamp,
                toolsConfiguration.opener(),
                settingsConfiguration.settingsManager(),
                javaFxSettingsConfiguration.javaFxSettingsManager(),
                repositoryConfiguration.repositoryManager());
    }

    @Bean
    public LibraryView viewLibrary() {
        return viewsConfigurationLibrary.viewLibrary();
    }

    @Bean
    public ConsoleTabFactory consoleTabFactory() {
        return viewsConfigurationLibrary.consoleTabFactory();
    }
}
