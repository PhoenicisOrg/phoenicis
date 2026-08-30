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

import org.phoenicis.containers.ContainersConfiguration;
import org.phoenicis.engines.EnginesConfiguration;
import org.phoenicis.javafx.controller.apps.AppsController;
import org.phoenicis.javafx.controller.containers.ContainersController;
import org.phoenicis.javafx.controller.engines.EnginesController;
import org.phoenicis.javafx.controller.library.LibraryController;
import org.phoenicis.javafx.controller.settings.SettingsController;
import org.phoenicis.javafx.settings.JavaFxSettingsConfiguration;
import org.phoenicis.javafx.themes.ThemeConfiguration;
import org.phoenicis.javafx.views.ViewsConfiguration;
import org.phoenicis.library.LibraryConfiguration;
import org.phoenicis.repository.RepositoryConfiguration;
import org.phoenicis.tools.archive.Extractor;
import org.phoenicis.tools.http.Downloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfiguration {

    @Value("${application.name}")
    private String applicationName;

    @Autowired
    private ThemeConfiguration themeConfiguration;

    @Autowired
    private ViewsConfiguration viewsConfiguration;

    @Autowired
    private RepositoryConfiguration repositoryConfiguration;

    @Autowired
    private LibraryConfiguration libraryConfiguration;

    @Autowired
    private EnginesConfiguration enginesConfiguration;

    @Autowired
    private ContainersConfiguration containersConfiguration;

    @Autowired
    private JavaFxSettingsConfiguration javaFxSettingsConfiguration;

    @Autowired
    private Downloader downloader;

    @Autowired
    private Extractor extractor;

    @Bean
    public MainController mainController() {
        return new MainController(applicationName,
                libraryController(),
                appsController(),
                enginesController(),
                containersController(),
                viewsConfiguration.viewInstallations(),
                settingsController(),
                repositoryConfiguration.repositoryManager(),
                themeConfiguration.themeManager(),
                javaFxSettingsConfiguration.javaFxSettingsManager());
    }

    @Bean
    public ContainersController containersController() {
        return new ContainersController(viewsConfiguration.viewContainers(),
                containersConfiguration.backgroundContainersManager(),
                repositoryConfiguration.repositoryManager(),
                enginesConfiguration.engineSettingsManager(),
                enginesConfiguration.enginesSource(),
                enginesConfiguration.verbsManager(),
                enginesConfiguration.engineToolsManager());
    }

    @Bean
    public EnginesController enginesController() {
        return new EnginesController(viewsConfiguration.viewEngines(),
                repositoryConfiguration.repositoryManager(),
                enginesConfiguration.enginesSource(),
                themeConfiguration.themeManager(),
                downloader,
                extractor);
    }

    @Bean
    public LibraryController libraryController() {
        return new LibraryController(
                viewsConfiguration.viewLibrary(),
                libraryConfiguration.libraryManager(),
                themeConfiguration.themeManager());
    }

    @Bean
    public AppsController appsController() {
        return new AppsController(
                viewsConfiguration.viewApps(),
                repositoryConfiguration.repositoryManager(),
                themeConfiguration.themeManager());
    }

    @Bean
    public SettingsController settingsController() {
        return new SettingsController(viewsConfiguration.viewSettings());
    }

}
