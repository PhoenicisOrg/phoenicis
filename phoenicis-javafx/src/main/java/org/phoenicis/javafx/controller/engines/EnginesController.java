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

package org.phoenicis.javafx.controller.engines;

import javafx.application.Platform;
import org.phoenicis.engines.Engine;
import org.phoenicis.engines.EnginesManager;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.javafx.controller.apps.AppsController;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.engines.EnginesView;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.repository.dto.TypeDTO;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class EnginesController {
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AppsController.class);
    private final EnginesView enginesView;
    private final RepositoryManager repositoryManager;
    private final EnginesManager enginesManager;
    private ThemeManager themeManager;
    private RepositoryDTO repositoryCache = null;
    private Map<String, Engine> enginesCache = new HashMap<>();
    private Map<String, List<EngineSubCategoryDTO>> versionsCache = new HashMap<>();

    public EnginesController(EnginesView enginesView, RepositoryManager repositoryManager,
            EnginesManager enginesManager,
            ThemeManager themeManager) {
        this.enginesView = enginesView;
        this.repositoryManager = repositoryManager;
        this.enginesManager = enginesManager;
        this.themeManager = themeManager;

        this.repositoryManager.addCallbacks(repositoryDTO -> this.enginesManager.fetchAvailableEngines(repositoryDTO,
                engines -> this.populateView(repositoryDTO, engines),
                e -> Platform.runLater(() -> enginesView.showFailure(tr("Loading engines failed."), Optional.of(e)))),
                e -> Platform.runLater(() -> enginesView.showFailure(tr("Loading engines failed."), Optional.of(e))));

        this.enginesView.setOnSelectEngineCategory(engineCategoryDTO -> {
            // TODO: better way to get engine ID
            final String engineId = engineCategoryDTO.getName().toLowerCase();
            // only if not chached
            if (!this.versionsCache.containsKey(engineId)) {
                this.enginesManager.fetchAvailableVersions(engineId,
                        versions -> {
                            this.versionsCache.put(engineId, versions);
                            this.enginesView.updateVersions(engineCategoryDTO, versions);
                        },
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e, this.enginesView).show()));
            }
        });

        this.enginesView.setOnInstallEngine(engineDTO -> new ConfirmMessage(
                tr("Install {0}", engineDTO.getVersion()),
                tr("Are you sure you want to install {0}?", engineDTO.getVersion()),
                this.enginesView.getContent().getScene().getWindow())
                        .ask(() -> this.enginesManager.getEngine(engineDTO.getId(),
                                engine -> {
                                    engine.install(engineDTO.getSubCategory(), engineDTO.getVersion());
                                    // invalidate cache and force view update to show installed version correctly
                                    this.versionsCache.remove(engineDTO.getId());
                                    this.forceViewUpdate();
                                },
                                e -> Platform.runLater(
                                        () -> new ErrorMessage("Error", e, this.enginesView).show()))));

        this.enginesView.setOnDeleteEngine(engineDTO -> new ConfirmMessage(
                tr("Delete {0}", engineDTO.getVersion()),
                tr("Are you sure you want to delete {0}", engineDTO.getVersion()),
                this.enginesView.getContent().getScene().getWindow())
                        .ask(() -> this.enginesManager.getEngine(engineDTO.getId(),
                                engine -> {
                                    engine.delete(engineDTO.getSubCategory(), engineDTO.getVersion());
                                    // invalidate cache and force view update to show deleted version correctly
                                    this.versionsCache.remove(engineDTO.getId());
                                    this.forceViewUpdate();
                                },
                                e -> Platform.runLater(
                                        () -> new ErrorMessage("Error", e, this.enginesView).show()))));
    }

    public EnginesView getView() {
        return enginesView;
    }

    private void populateView(RepositoryDTO repositoryDTO, Map<String, Engine> engines) {
        this.repositoryCache = repositoryDTO;
        this.enginesCache = engines;
        Platform.runLater(() -> {
            this.enginesView.showWait();
            List<CategoryDTO> categoryDTOS = new ArrayList<>();
            for (TypeDTO typeDTO : repositoryDTO.getTypes()) {
                if (typeDTO.getId().equals("engines")) {
                    categoryDTOS = typeDTO.getCategories();
                }
            }
            setDefaultEngineIcons(categoryDTOS);
            this.enginesView.populate(this.enginesManager.getAvailableEngines(categoryDTOS), engines);
        });
    }

    /**
     * forces an update of the view
     */
    private void forceViewUpdate() {
        this.populateView(this.repositoryCache, this.enginesCache);
    }

    private void setDefaultEngineIcons(List<CategoryDTO> categoryDTOS) {
        try {
            StringBuilder cssBuilder = new StringBuilder();
            for (CategoryDTO category : categoryDTOS) {
                cssBuilder.append("#" + category.getName().toLowerCase() + "Button{\n");
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
            Path temp = Files.createTempFile("defaultEngineIcons", ".css").toAbsolutePath();
            File tempFile = temp.toFile();
            tempFile.deleteOnExit();
            Files.write(temp, css.getBytes());
            String defaultEngineIconsCss = temp.toUri().toString();
            themeManager.setDefaultEngineIconsCss(defaultEngineIconsCss);
        } catch (IOException e) {
            LOGGER.warn("Could not set default engine icons.", e);
        }
    }
}
