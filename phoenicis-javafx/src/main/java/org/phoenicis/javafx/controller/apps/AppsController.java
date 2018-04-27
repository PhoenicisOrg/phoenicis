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
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationsView;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class AppsController {
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AppsController.class);
    private final ApplicationsView view;
    private final RepositoryManager repositoryManager;
    private final ScriptInterpreter scriptInterpreter;
    private ThemeManager themeManager;

    private Runnable onAppLoaded = () -> {
    };

    public AppsController(ApplicationsView view, RepositoryManager repositoryManager,
            ScriptInterpreter scriptInterpreter,
            ThemeManager themeManager) {
        this.view = view;
        this.repositoryManager = repositoryManager;
        this.scriptInterpreter = scriptInterpreter;
        this.themeManager = themeManager;

        this.repositoryManager.addCallbacks(this::populateView,
                e -> Platform.runLater(() -> view.showFailure(
                        tr("Connecting to the repository failed.\nPlease check your connection and try again."),
                        Optional.of(e))));
    }

    public void loadApps() {
        this.view.showWait();
        this.repositoryManager.triggerRepositoryChange();

        this.view.setOnRetryButtonClicked(event -> {
            this.view.showWait();
            this.repositoryManager.triggerRepositoryChange();
        });

        this.view.setOnSelectScript(
                scriptDTO -> {
                    final StringBuilder executeBuilder = new StringBuilder();
                    executeBuilder.append("TYPE_ID=\"");
                    executeBuilder.append(scriptDTO.getTypeId());
                    executeBuilder.append("\";\n");
                    executeBuilder.append("CATEGORY_ID=\"");
                    executeBuilder.append(scriptDTO.getCategoryId());
                    executeBuilder.append("\";\n");
                    executeBuilder.append("APPLICATION_ID=\"");
                    executeBuilder.append(scriptDTO.getApplicationId());
                    executeBuilder.append("\";\n");
                    executeBuilder.append("SCRIPT_ID=\"");
                    executeBuilder.append(scriptDTO.getId());
                    executeBuilder.append("\";\n");
                    executeBuilder.append(scriptDTO.getScript());

                    // TODO: use Java interface instead of String
                    scriptInterpreter.runScript(executeBuilder.toString(), e -> Platform.runLater(() -> {
                        // no exception if installation is cancelled
                        if (!(e.getCause() instanceof InterruptedException)) {
                            new ErrorMessage(tr("The script ended unexpectedly"), e, this.view);
                        }
                    }));
                });

        onAppLoaded.run();
    }

    public void setOnAppLoaded(Runnable onAppLoaded) {
        this.onAppLoaded = onAppLoaded;
    }

    public ApplicationsView getView() {
        return view;
    }

    private void populateView(RepositoryDTO repositoryDTO) {
        Platform.runLater(() -> {
            List<CategoryDTO> categoryDTOS = repositoryDTO.getTypes().get(0).getCategories();
            setDefaultCategoryIcons(categoryDTOS);
            this.view.populate(categoryDTOS);
        });
    }

    private void setDefaultCategoryIcons(List<CategoryDTO> categoryDTOS) {
        try {
            StringBuilder cssBuilder = new StringBuilder();
            for (CategoryDTO category : categoryDTOS) {
                cssBuilder.append("#" + category.getId().toLowerCase() + "Button{\n");
                URI categoryIcon = category.getIcon();
                if (categoryIcon == null) {
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
        } catch (IOException e) {
            LOGGER.warn("Could not set default category icons.", e);
        }
    }
}
