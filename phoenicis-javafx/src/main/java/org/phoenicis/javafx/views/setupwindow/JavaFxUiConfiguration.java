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

package org.phoenicis.javafx.views.setupwindow;

import javafx.application.Platform;
import org.phoenicis.javafx.UiMessageSenderJavaFXImplementation;
import org.phoenicis.javafx.views.ViewsConfiguration;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ThemeConfiguration;
import org.phoenicis.javafx.views.mainwindow.library.ViewsConfigurationLibrary;
import org.phoenicis.scripts.ui.SetupUiFactory;
import org.phoenicis.scripts.ui.UiConfiguration;
import org.phoenicis.scripts.ui.UiMessageSender;
import org.phoenicis.scripts.ui.UiQuestionFactory;
import org.phoenicis.tools.ToolsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return title -> {
            final SetupUiJavaFXImplementation setupWindow = new SetupUiJavaFXImplementation(title, toolsConfiguration.operatingSystemFetcher(), themeConfiguration.themeManager());
            viewsConfiguration.viewEngines().showWizard(setupWindow);
            setupWindow.setOnShouldClose(() -> viewsConfiguration.viewEngines().showWineVersions());
            return setupWindow;
        };
    }

    @Override
    @Bean
    public UiMessageSender uiMessageSender() {
        return new UiMessageSenderJavaFXImplementation();
    }

    @Override
    @Bean
    public UiQuestionFactory uiQuestionFactory() {
        return (text, yesCallback, noCallback) -> Platform.runLater(() -> new ConfirmMessage("Question", text).ask(yesCallback, noCallback));
    }
}
