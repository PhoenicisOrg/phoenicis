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

package org.phoenicis.javafx.views.scriptui;

import org.phoenicis.javafx.UiMessageSenderJavaFXImplementation;
import org.phoenicis.javafx.views.ViewsConfiguration;
import org.phoenicis.javafx.views.common.ThemeConfiguration;
import org.phoenicis.javafx.views.mainwindow.library.ViewsConfigurationLibrary;
import org.phoenicis.scripts.ui.*;
import org.phoenicis.tools.ToolsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.phoenicis.configuration.localisation.Localisation.tr;

@Configuration
public class JavaFxUiConfiguration implements UiConfiguration {
    @Autowired
    private ViewsConfiguration viewsConfiguration;

    @Autowired
    private ViewsConfigurationLibrary viewsConfigurationLibrary;

    @Autowired
    private ToolsConfiguration toolsConfiguration;

    @Autowired
    private ThemeConfiguration themeConfiguration;

    @Override
    @Bean
    public SetupUiFactory setupUiFactory() {
        return new SetupUiFactoryJavaFX(toolsConfiguration.operatingSystemFetcher(), themeConfiguration.themeManager(),
                viewsConfiguration.viewInstallations());
    }

    @Override
    @Bean
    public UiMessageSender uiMessageSender() {
        return new UiMessageSenderJavaFXImplementation();
    }

    @Override
    @Bean
    public UiQuestionFactory uiQuestionFactory() {
        return new UiQuestionFactoryJavaFX(tr("Question"));
    }
}
