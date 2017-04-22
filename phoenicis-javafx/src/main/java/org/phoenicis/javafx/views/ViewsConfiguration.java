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

import org.phoenicis.apps.AppsConfiguration;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.javafx.controller.ControllerConfiguration;
import org.phoenicis.javafx.views.common.ThemeConfiguration;
import org.phoenicis.javafx.views.common.widget.PhoenicisLogo;
import org.phoenicis.javafx.views.mainwindow.apps.ViewApps;
import org.phoenicis.javafx.views.mainwindow.console.ConsoleTabFactory;
import org.phoenicis.javafx.views.mainwindow.containers.ContainerPanelFactory;
import org.phoenicis.javafx.views.mainwindow.containers.ViewContainers;
import org.phoenicis.javafx.views.mainwindow.containers.WinePrefixContainerPanel;
import org.phoenicis.javafx.views.mainwindow.engines.ViewEngines;
import org.phoenicis.javafx.views.mainwindow.library.ViewLibrary;
import org.phoenicis.javafx.views.mainwindow.library.ViewsConfigurationLibrary;
import org.phoenicis.javafx.views.mainwindow.settings.ViewSettings;
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

    @Autowired
    private ViewsConfigurationLibrary viewsConfigurationLibrary;

    @Autowired
    private ThemeConfiguration themeConfiguration;

    @Autowired
    private ToolsConfiguration toolsConfiguration;
    
    @Autowired
    private SettingsConfiguration settingsConfiguration;

    @Autowired
    private AppsConfiguration appsConfiguration;

    @Autowired
    private ControllerConfiguration controllerConfiguration;

    @Bean
    public ViewApps viewApps() {
        return new ViewApps(themeConfiguration.themeManager(), settingsConfiguration.settingsManager());
    }

    @Bean
    public ViewEngines viewEngines() {
        return new ViewEngines(themeConfiguration.themeManager(), controllerConfiguration.getWineEnginesPath());
    }

    @Bean
    public ViewContainers viewContainers() {
        return new ViewContainers(themeConfiguration.themeManager());
    }

    @Bean
    public ViewSettings viewSettings() {
        return new ViewSettings(themeConfiguration.themeManager(), applicationName, applicationVersion, applicationGitRevision, applicationBuildTimestamp, toolsConfiguration.opener(), settingsConfiguration.settingsManager(), appsConfiguration.repositoryManager());
    }

    @Bean
    public PhoenicisLogo phoenicisLogo() {
        return new PhoenicisLogo(applicationName);
    }

    @Bean
    public ViewLibrary viewLibrary() {
        return viewsConfigurationLibrary.viewLibrary();
    }

    @Bean
    public ConsoleTabFactory consoleTabFactory() {
        return viewsConfigurationLibrary.consoleTabFactory();
    }

    @Bean
    public ContainerPanelFactory<WinePrefixContainerPanel, WinePrefixContainerDTO> winePrefixContainerPanelFactory() {
        return new ContainerPanelFactory<>(WinePrefixContainerPanel.class, WinePrefixContainerDTO.class);
    }
}
