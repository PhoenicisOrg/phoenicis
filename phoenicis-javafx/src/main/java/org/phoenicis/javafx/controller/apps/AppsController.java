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
import org.phoenicis.javafx.components.application.control.ApplicationsFeaturePanel;
import org.phoenicis.javafx.components.application.skin.ApplicationSidebarToggleGroupSkin;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.javafx.themes.ThemeManager;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.slf4j.Logger;
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
    private final Logger LOGGER = LoggerFactory.getLogger(AppsController.class);
    private final ApplicationsFeaturePanel view;
    private final ThemeManager themeManager;

    public AppsController(ApplicationsFeaturePanel view,
            RepositoryManager repositoryManager,
            ThemeManager themeManager) {
        this.view = view;
        this.themeManager = themeManager;

        repositoryManager.addCallbacks(this::populate, this::showError);
    }

    public ApplicationsFeaturePanel getView() {
        return view;
    }

    private void populate(RepositoryDTO repositoryDTO) {
        Platform.runLater(() -> {
            final List<CategoryDTO> categoryDTOS = repositoryDTO.getTypes().get(0).getCategories();
            setDefaultCategoryIcons(categoryDTOS);
            getView().getCategories().setAll(categoryDTOS);
            getView().setInitialized(true);
        });
    }

    private void showError(Exception e) {
        Platform.runLater(() -> {
            final ErrorDialog errorDialog = ErrorDialog.builder()
                    .withOwner(view.getScene().getWindow())
                    .withException(e)
                    .withMessage(
                            tr("Connecting to the repository failed.\nPlease check your connection and try again."))
                    .build();

            errorDialog.showAndWait();
        });
    }

    /**
     * sets the default category icons from the repository for the sidebar buttons
     *
     * @param categories categories in repository
     */
    private void setDefaultCategoryIcons(List<CategoryDTO> categories) {
        Platform.runLater(() -> {
            try {
                StringBuilder cssBuilder = new StringBuilder();
                for (CategoryDTO category : categories) {
                    cssBuilder.append(
                            "#" + ApplicationSidebarToggleGroupSkin.getToggleButtonId(category.getId()) + "{\n");
                    final String categoryIconPath = Optional.ofNullable(category.getIcon())
                            .map(categoryIcon -> categoryIcon.toString())
                            .orElse("/org/phoenicis/javafx/views/common/phoenicis.png");

                    cssBuilder.append(String.format("-fx-background-image: url('%s');\n", categoryIconPath));
                    cssBuilder.append("}\n");
                }

                String css = cssBuilder.toString();
                Path temp = Files.createTempFile("defaultCategoryIcons", ".css").toAbsolutePath();
                File tempFile = temp.toFile();
                tempFile.deleteOnExit();
                Files.write(temp, css.getBytes());
                String defaultCategoryIconsCss = temp.toUri().toString();
                this.themeManager.setDefaultCategoryIconsCss(defaultCategoryIconsCss);
            } catch (IOException e) {
                LOGGER.warn("Could not set default category icons.", e);
            }
        });
    }
}
