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

package com.phoenicis.javafx.views;

import com.phoenicis.containers.dto.WinePrefixDTO;
import com.phoenicis.javafx.views.common.widget.PhoenicisLogo;
import com.phoenicis.javafx.views.mainwindow.apps.ViewApps;
import com.phoenicis.javafx.views.mainwindow.console.ConsoleTabFactory;
import com.phoenicis.javafx.views.mainwindow.containers.ContainerPanelFactory;
import com.phoenicis.javafx.views.mainwindow.containers.ViewContainers;
import com.phoenicis.javafx.views.mainwindow.containers.WinePrefixContainerPanel;
import com.phoenicis.javafx.views.mainwindow.engines.ViewEngines;
import com.phoenicis.javafx.views.mainwindow.library.ViewLibrary;
import com.phoenicis.javafx.views.mainwindow.library.ViewsConfigurationLibrary;
import com.phoenicis.javafx.views.mainwindow.settings.ViewSettings;
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

    @Value("${application.theme:default}")
    private String themeName;

    @Autowired
    private ViewsConfigurationLibrary viewsConfigurationLibrary;

    @Bean
    public ViewApps viewApps() {
        return new ViewApps(themeName);
    }

    @Bean
    public ViewEngines viewEngines() {
        return new ViewEngines();
    }

    @Bean
    public ViewContainers viewContainers() {
        return new ViewContainers();
    }

    @Bean
    public ViewSettings viewSettings() {
        return new ViewSettings();
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
