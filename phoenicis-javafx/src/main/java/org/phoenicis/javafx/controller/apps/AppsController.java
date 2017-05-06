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

package org.phoenicis.javafx.controller.apps;

import javafx.application.Platform;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.apps.RepositoryManager;
import org.phoenicis.apps.dto.CategoryDTO;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.apps.ViewApps;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AppsController {
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AppsController.class);
    private final ViewApps view;
    private final RepositoryManager repositoryManager;
    private final ScriptInterpreter scriptInterpreter;
    private ThemeManager themeManager;

    private Runnable onAppLoaded = () -> {
    };

    public AppsController(ViewApps view, RepositoryManager repositoryManager, ScriptInterpreter scriptInterpreter,
            ThemeManager themeManager) {
        this.view = view;
        this.repositoryManager = repositoryManager;
        this.scriptInterpreter = scriptInterpreter;
        this.themeManager = themeManager;

        this.repositoryManager.addCallbacks(this::populateView, e -> view.showFailure());
    }

    public void loadApps() {
        this.view.showWait();
        this.repositoryManager.triggerRepositoryChange();

        this.view.setOnRetryButtonClicked(event -> {
            this.view.showWait();
            this.repositoryManager.triggerRepositoryChange();
        });

        this.view.setOnSelectScript(
                scriptDTO -> scriptInterpreter.runScript(scriptDTO.getScript(), e -> Platform.runLater(() -> {
                    // no exception if installation is cancelled
                    if (!(e.getCause() instanceof InterruptedException)) {
                        new ErrorMessage("The script ended unexpectedly", e);
                    }
                })));

        onAppLoaded.run();
    }

    public void setOnAppLoaded(Runnable onAppLoaded) {
        this.onAppLoaded = onAppLoaded;
    }

    public ViewApps getView() {
        return view;
    }

    private void populateView(List<CategoryDTO> categoryDTOS) {
        Platform.runLater(() -> {
            setDefaultCategoryIcons(categoryDTOS);
            this.view.populate(categoryDTOS);
        });
    }

    private void setDefaultCategoryIcons(List<CategoryDTO> categoryDTOS) {
        try {
            StringBuilder cssBuilder = new StringBuilder();
            for (CategoryDTO category : categoryDTOS) {
                cssBuilder.append("#" + category.getName().toLowerCase() + "Button{\n");
                String categoryIcon = category.getIcon();
                if (StringUtils.isEmpty(categoryIcon)) {
                    cssBuilder
                            .append("-fx-background-image: url('/org/phoenicis/javafx/views/common/phoenicis.png');\n");
                } else {
                    cssBuilder.append("-fx-background-image: url('" + categoryIcon + "');\n");
                }
                cssBuilder.append("}\n");
            }
            String css = cssBuilder.toString();
            Path temp = Files.createTempFile("defaultCategoryIcons", ".css").toAbsolutePath();
            File tempFile = temp.toFile();
            tempFile.deleteOnExit();
            Files.write(temp, css.getBytes());
            String defaultCategoryIconsCss = temp.toUri().toString();
            themeManager.setDefaultCategoryIconsCss(defaultCategoryIconsCss);

            // apply current theme again to fix hierarchy
            final String shortName = themeManager.getCurrentTheme().getShortName();
            final String url = String.format("/org/phoenicis/javafx/themes/%s/main.css", shortName);
            final URL style = this.getClass().getResource(url);
            this.view.getTabPane().getScene().getStylesheets().clear();
            this.view.getTabPane().getScene().getStylesheets().addAll(defaultCategoryIconsCss,
                    themeManager.getDefaultEngineIconsCss(), style.toExternalForm());
        } catch (IOException e) {
            LOGGER.warn("Could not set default category icons.", e);
        }
    }
}
