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

package com.phoenicis.javafx.controller;

import com.phoenicis.library.LibraryConfiguration;
import com.phoenicis.settings.SettingsConfiguration;
import com.phoenicis.apps.AppsConfiguration;
import com.phoenicis.containers.ContainersConfiguration;
import com.phoenicis.engines.EnginesConfiguration;
import com.phoenicis.javafx.controller.apps.AppsController;
import com.phoenicis.javafx.controller.containers.ContainersController;
import com.phoenicis.javafx.controller.engines.EnginesController;
import com.phoenicis.javafx.controller.library.LibraryController;
import com.phoenicis.javafx.controller.library.console.ConsoleController;
import com.phoenicis.javafx.controller.settings.SettingsController;
import com.phoenicis.javafx.views.ViewsConfiguration;
import com.phoenicis.scripts.ScriptsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfiguration {

    @Value("${application.name}")
    private String applicationName;

    @Value("${application.theme:defaultTheme.css}")
    private String theme;

    @Autowired
    private ViewsConfiguration viewsConfiguration;

    @Autowired
    private ScriptsConfiguration scriptsConfiguration;

    @Autowired
    private AppsConfiguration appsConfiguration;

    @Autowired
    private LibraryConfiguration libraryConfiguration;

    @Autowired
    private EnginesConfiguration enginesConfiguration;

    @Autowired
    private ContainersConfiguration containersConfiguration;

    @Autowired
    private SettingsConfiguration settingsConfiguration;

    @Bean
    public MainController mainController() {
        return new MainController(
                theme,
                applicationName,
                libraryController(),
                appsController(),
                enginesController(),
                containersController(),
                settingsController(),
                viewsConfiguration.phoenicisLogo());
    }

    @Bean
    public ContainersController containersController() {
        return new ContainersController(
                viewsConfiguration.viewContainers(),
                containersConfiguration.backgroundContainersManager(),
                viewsConfiguration.wineContainerPanelFactory(),
                containersConfiguration.wineContainerController()
        );
    }


    @Bean
    public EnginesController enginesController() {
        return new EnginesController(viewsConfiguration.viewEngines(), enginesConfiguration.wineVersionsFetcher());
    }

    @Bean
    public LibraryController libraryController() {
        return new LibraryController(
                viewsConfiguration.viewLibrary(),
                consoleController(),
                libraryConfiguration.libraryManager(),
                libraryConfiguration.shortcutRunner(),
                libraryConfiguration.shortcutManager(),
                scriptsConfiguration.scriptInterpreter()
        );
    }

    @Bean
    public AppsController appsController() {
        return new AppsController(
                viewsConfiguration.viewApps(),
                appsConfiguration.backgroundAppsSource(),
                scriptsConfiguration.scriptInterpreter()
        );
    }

    @Bean
    public ConsoleController consoleController() {
        return new ConsoleController(
                viewsConfiguration.consoleTabFactory(),
                scriptsConfiguration.scriptInterpreter()
        );
    }

    @Bean
    public SettingsController settingsController() {
        return new SettingsController(
                viewsConfiguration.viewSettings(),
                settingsConfiguration.settingsManager()
        );
    }
}
