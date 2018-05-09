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

package org.phoenicis.javafx.views.mainwindow.library;

import org.phoenicis.configuration.PhoenicisGlobalConfiguration;
import org.phoenicis.javafx.settings.JavaFxSettingsConfiguration;
import org.phoenicis.javafx.views.common.ThemeConfiguration;
import org.phoenicis.javafx.views.mainwindow.console.ConsoleTabFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ViewsConfigurationLibrary {
    @Value("${application.name}")
    private String applicationName;

    @Value("${application.user.containers}")
    private String containersPath;

    @Autowired
    private ThemeConfiguration themeConfiguration;

    @Autowired
    private PhoenicisGlobalConfiguration phoenicisGlobalConfiguration;

    @Autowired
    private JavaFxSettingsConfiguration javaFxSettingsConfiguration;

    @Bean
    public LibraryView viewLibrary() {
        return new LibraryView(applicationName,
                containersPath,
                themeConfiguration.themeManager(),
                phoenicisGlobalConfiguration.objectMapper(),
                javaFxSettingsConfiguration.javaFxSettingsManager());
    }

    @Bean
    public ConsoleTabFactory consoleTabFactory() {
        return new ConsoleTabFactory();
    }
}
