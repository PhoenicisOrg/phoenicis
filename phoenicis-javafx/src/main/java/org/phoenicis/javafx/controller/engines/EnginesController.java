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

import com.google.common.collect.ImmutableMap;
import javafx.application.Platform;
import org.phoenicis.engines.Engine;
import org.phoenicis.engines.EnginesManager;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.javafx.controller.apps.AppsController;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.engines.EnginesView;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class EnginesController {
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AppsController.class);
    private final EnginesView enginesView;
    private final RepositoryManager repositoryManager;
    private final EnginesManager enginesManager;

    private ThemeManager themeManager;
    private RepositoryDTO repositoryCache;
    private Map<String, Engine> enginesCache = new HashMap<>();
    private Map<String, List<EngineSubCategoryDTO>> versionsCache = new HashMap<>();

    public EnginesController(EnginesView enginesView, RepositoryManager repositoryManager,
            EnginesManager enginesManager, ThemeManager themeManager) {
        super();

        this.enginesView = enginesView;
        this.repositoryManager = repositoryManager;
        this.enginesManager = enginesManager;
        this.themeManager = themeManager;

        this.repositoryManager.addCallbacks(
                repositoryDTO -> {
                    this.enginesManager.fetchAvailableEngines(
                            repositoryDTO,
                            engines -> this.populateView(repositoryDTO, engines),
                            e -> Platform.runLater(
                                    () -> enginesView.showFailure(tr("Loading engines failed."), Optional.of(e))));
                },
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

        this.enginesView.setOnInstallEngine(engineDTO -> {
            ConfirmMessage confirmMessage = new ConfirmMessage(
                    tr("Install {0}", engineDTO.getVersion()),
                    tr("Are you sure you want to install {0}?", engineDTO.getVersion()),
                    this.enginesView.getContent().getScene().getWindow());
            confirmMessage.setResizable(true);
            confirmMessage.ask(() -> this.enginesManager.getEngine(engineDTO.getId(),
                    engine -> {
                        engine.install(engineDTO.getSubCategory(), engineDTO.getVersion());
                        // invalidate cache and force view update to show installed version correctly
                        this.versionsCache.remove(engineDTO.getId());
                        this.forceViewUpdate();
                    },
                    e -> Platform.runLater(
                            () -> new ErrorMessage("Error", e, this.enginesView).show())));
        });

        this.enginesView.setOnDeleteEngine(engineDTO -> {
            ConfirmMessage confirmMessage = new ConfirmMessage(
                    tr("Delete {0}", engineDTO.getVersion()),
                    tr("Are you sure you want to delete {0}?", engineDTO.getVersion()),
                    this.enginesView.getContent().getScene().getWindow());
            confirmMessage.setResizable(true);
            confirmMessage.ask(() -> this.enginesManager.getEngine(engineDTO.getId(),
                    engine -> {
                        engine.delete(engineDTO.getSubCategory(), engineDTO.getVersion());
                        // invalidate cache and force view update to show deleted version correctly
                        this.versionsCache.remove(engineDTO.getId());
                        this.forceViewUpdate();
                    },
                    e -> Platform.runLater(
                            () -> new ErrorMessage("Error", e, this.enginesView).show())));
        });
    }

    public EnginesView getView() {
        return enginesView;
    }

    private void populateView(RepositoryDTO repositoryDTO, Map<String, Engine> engines) {
        this.repositoryCache = repositoryDTO;
        this.enginesCache = engines;

        // show a waiting screen until the engines are loaded
        enginesView.showWait();

        // fetch all categories consisting of engines that are contained in the repository
        final List<CategoryDTO> categoryDTOS = repositoryDTO.getTypes().stream()
                .filter(type -> type.getId().equals("engines"))
                .flatMap(type -> type.getCategories().stream())
                .collect(Collectors.toList());

        // generate the necessary css for the engine categories
        setDefaultEngineIcons(categoryDTOS);

        // fetch the engine categories objects contained in the engine categories
        final Queue<EngineCategoryDTO> engineCategories = new ArrayDeque<>(
                enginesManager.getAvailableEngines(categoryDTOS));

        // insert the missing engine subcategories into the engine categories
        fetchEngineSubcategories(engineCategories, ImmutableMap.of(), subcategoryMap -> {
            final List<EngineCategoryDTO> categories = subcategoryMap.entrySet().stream()
                    .map(entry -> new EngineCategoryDTO.Builder(entry.getKey())
                            .withSubCategories(entry.getValue())
                            .build())
                    .collect(Collectors.toList());

            Platform.runLater(() -> {
                // update the view
                enginesView.populate(categories, engines);
            });
        });
    }

    private void fetchEngineSubcategories(Queue<EngineCategoryDTO> engineCategories,
            Map<EngineCategoryDTO, List<EngineSubCategoryDTO>> result,
            Consumer<Map<EngineCategoryDTO, List<EngineSubCategoryDTO>>> callback) {
        final Queue<EngineCategoryDTO> queue = new ArrayDeque<>(engineCategories);

        if (queue.isEmpty()) {
            // recursion anchor
            callback.accept(result);
        } else {
            final EngineCategoryDTO engineCategory = queue.poll();
            final String engineId = engineCategory.getName().toLowerCase();

            enginesManager.fetchAvailableVersions(
                    engineId,
                    versions -> {
                        // recursively process the remaining engine categories
                        fetchEngineSubcategories(queue,
                                ImmutableMap.<EngineCategoryDTO, List<EngineSubCategoryDTO>> builder()
                                        .putAll(result)
                                        .put(engineCategory, versions)
                                        .build(),
                                callback);
                    },
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e, enginesView).show()));
        }
    }

    /**
     * Fetches all engine subcategories that belong to a given list of engine categories
     *
     * @param engineCategories The engine categories
     * @param result The temporary transport variable
     * @param callback A callback method, which is called after all engine subcategories have been fetched
     */
    private void fetchEngineSubcategories(Queue<EngineCategoryDTO> engineCategories,
            Map<EngineCategoryDTO, List<EngineSubCategoryDTO>> result,
            Consumer<Map<EngineCategoryDTO, List<EngineSubCategoryDTO>>> callback) {
        final Queue<EngineCategoryDTO> queue = new ArrayDeque<>(engineCategories);

        if (queue.isEmpty()) {
            // recursion anchor
            callback.accept(result);
        } else {
            final EngineCategoryDTO engineCategory = queue.poll();
            final String engineId = engineCategory.getName().toLowerCase();

            enginesManager.fetchAvailableVersions(
                    engineId,
                    versions -> {
                        // recursively process the remaining engine categories
                        fetchEngineSubcategories(queue,
                                ImmutableMap.<EngineCategoryDTO, List<EngineSubCategoryDTO>> builder()
                                        .putAll(result)
                                        .put(engineCategory, versions)
                                        .build(),
                                callback);
                    },
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e, enginesView).show()));
        }
    }

    /**
     * Forces an update of the view
     */
    private void forceViewUpdate() {
        this.populateView(this.repositoryCache, this.enginesCache);
    }

    /**
     * Generates css for the button design associated with the given categories
     *
     * @param categoryDTOS The categories
     */
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