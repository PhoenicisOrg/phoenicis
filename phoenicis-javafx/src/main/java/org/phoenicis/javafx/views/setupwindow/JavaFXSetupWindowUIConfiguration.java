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
import org.phoenicis.javafx.UIMessageSenderJavaFXImplementation;
import org.phoenicis.javafx.views.ViewsConfiguration;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ThemeConfiguration;
import org.phoenicis.javafx.views.mainwindow.library.ViewsConfigurationLibrary;
import org.phoenicis.scripts.ui.SetupWindowFactory;
import org.phoenicis.scripts.ui.SetupWindowUIConfiguration;
import org.phoenicis.scripts.ui.UIMessageSender;
import org.phoenicis.scripts.ui.UIQuestionFactory;
import org.phoenicis.tools.ToolsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaFXSetupWindowUIConfiguration implements SetupWindowUIConfiguration {
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
    public SetupWindowFactory setupWindowFactory() {
        return (title, type) -> {
            final SetupWindowJavaFXImplementation setupWindow = new SetupWindowJavaFXImplementation(title, toolsConfiguration.operatingSystemFetcher(), themeConfiguration.themeManager());
            switch (type) {
                case ENGINE:
                    viewsConfiguration.viewEngines().showWizard(setupWindow);
                    setupWindow.setOnShouldClose(() -> viewsConfiguration.viewEngines().showWineVersions());
                    break;
                case APP:
                default:
                    viewsConfigurationLibrary.viewLibrary().createNewTab(setupWindow);
                    setupWindow.setOnShouldClose(() -> viewsConfigurationLibrary.viewLibrary().closeTab(setupWindow));
                    break;
            }
            return setupWindow;
        };
    }

    @Override
    @Bean
    public UIMessageSender uiMessageSender() {
        return new UIMessageSenderJavaFXImplementation();
    }

    @Override
    @Bean
    public UIQuestionFactory uiQuestionFactory() {
        return (text, yesCallback, noCallback) -> Platform.runLater(() -> new ConfirmMessage("Question", text).ask(yesCallback, noCallback));
    }
}
