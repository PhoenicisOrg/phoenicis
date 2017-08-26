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
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.phoenicis.engines.EnginesSource;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.javafx.controller.apps.AppsController;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.engines.ViewEngines;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.repository.dto.TypeDTO;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class EnginesController {
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AppsController.class);
    private final ViewEngines viewEngines;
    private final RepositoryManager repositoryManager;
    private final EnginesSource enginesSource;
    private final ScriptInterpreter scriptInterpreter;
    private ThemeManager themeManager;

    public EnginesController(ViewEngines viewEngines, RepositoryManager repositoryManager, EnginesSource enginesSource,
            ScriptInterpreter scriptInterpreter,
            ThemeManager themeManager) {
        this.viewEngines = viewEngines;
        this.repositoryManager = repositoryManager;
        this.enginesSource = enginesSource;
        this.scriptInterpreter = scriptInterpreter;
        this.themeManager = themeManager;

        this.repositoryManager.addCallbacks(this::populateView,
                e -> Platform.runLater(() -> viewEngines.showFailure(tr("Loading engines failed."), Optional.of(e))));

        this.viewEngines.setOnInstallEngine(engineDTO -> {
            new ConfirmMessage(tr("Install {0}", engineDTO.getVersion()),
                    tr("Are you sure you want to install {0}?", engineDTO.getVersion())).ask(() -> {
                        installEngine(engineDTO, e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
                    });
        });

        this.viewEngines.setOnDeleteEngine(engineDTO -> {
            new ConfirmMessage(tr("Delete {0}", engineDTO.getVersion()),
                    tr("Are you sure you want to delete {0}", engineDTO.getVersion())).ask(() -> {
                        deleteEngine(engineDTO, e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
                    });
        });
    }

    public ViewEngines getView() {
        return viewEngines;
    }

    private void populateView(RepositoryDTO repositoryDTO) {
        Platform.runLater(() -> {
            List<CategoryDTO> categoryDTOS = new ArrayList<>();
            for (TypeDTO typeDTO : repositoryDTO.getTypes()) {
                if (typeDTO.getName().equals("Engines")) {
                    categoryDTOS = typeDTO.getCategories();
                }
            }
            setDefaultEngineIcons(categoryDTOS);
            enginesSource.fetchAvailableEngines(categoryDTOS,
                    versions -> Platform.runLater(() -> this.viewEngines.populate(versions)));
        });
    }

    private void installEngine(EngineDTO engineDTO, Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval(
                "include([\"Engines\", \"" + engineDTO.getCategory() + "\", \"Engine\", \"Object\"]);",
                ignored -> interactiveScriptSession.eval("new Wine()", output -> {
                    final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                    wine.callMember("install", engineDTO.getCategory(), engineDTO.getSubCategory(),
                            engineDTO.getVersion(), engineDTO.getUserData());
                }, errorCallback), errorCallback);
    }

    private void deleteEngine(EngineDTO engineDTO, Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval(
                "include([\"Engines\", \"" + engineDTO.getCategory() + "\", \"Engine\", \"Object\"]);",
                ignored -> interactiveScriptSession.eval("new Wine()", output -> {
                    final ScriptObjectMirror wine = (ScriptObjectMirror) output;
                    wine.callMember("delete", engineDTO.getCategory(), engineDTO.getSubCategory(),
                            engineDTO.getVersion(), engineDTO.getUserData());
                }, errorCallback), errorCallback);
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
