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

import org.phoenicis.containers.dto.WinePrefixDTO;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.Theme;
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

    @Value("${application.theme:defaultTheme.css}")
    private String theme;

    @Autowired
    private ViewsConfigurationLibrary viewsConfigurationLibrary;

    @Bean
    public ThemeManager themeManager() {
        switch (theme) {
            case "breezeDark":
                return new ThemeManager(Theme.BREEZE_DARK);
            case "dark":
                return new ThemeManager(Theme.DARK);
            case "hidpi":
                return new ThemeManager(Theme.HIDPI);
            case "unity":
                return new ThemeManager(Theme.UNITY);
            default:
                return new ThemeManager(Theme.DEFAULT);
        }
    }

    @Bean
    public ViewApps viewApps() {
        return new ViewApps(themeManager());
    }

    @Bean
    public ViewEngines viewEngines() {
        return new ViewEngines(themeManager());
    }

    @Bean
    public ViewContainers viewContainers() {
        return new ViewContainers(themeManager());
    }

    @Bean
    public ViewSettings viewSettings() {
        return new ViewSettings(themeManager());
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
    public ContainerPanelFactory<WinePrefixContainerPanel, WinePrefixDTO> wineContainerPanelFactory() {
        return new ContainerPanelFactory<>(WinePrefixContainerPanel.class, WinePrefixDTO.class);
    }
}
