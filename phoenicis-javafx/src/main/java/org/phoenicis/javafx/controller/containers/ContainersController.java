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

package org.phoenicis.javafx.controller.containers;

import javafx.application.Platform;
import javafx.collections.ObservableMap;
import org.phoenicis.containers.ContainersManager;
import org.phoenicis.engines.*;
import org.phoenicis.javafx.components.container.control.ContainersFeaturePanel;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.RepositoryDTO;

import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class ContainersController {
    private final ContainersFeaturePanel containersView;
    private final EngineSettingsManager engineSettingsManager;
    private final EngineToolsManager engineToolsManager;
    private final VerbsManager verbsManager;

    public ContainersController(ContainersFeaturePanel containersView,
            ContainersManager containersManager,
            RepositoryManager repositoryManager,
            EngineSettingsManager engineSettingsManager,
            EnginesManager enginesManager,
            VerbsManager verbsManager,
            EngineToolsManager engineToolsManager) {
        super();

        this.containersView = containersView;
        this.engineSettingsManager = engineSettingsManager;
        this.verbsManager = verbsManager;
        this.engineToolsManager = engineToolsManager;

        this.containersView.setEnginesManager(enginesManager);
        this.containersView.setVerbsManager(verbsManager);
        this.containersView.setEngineToolsManager(engineToolsManager);
        this.containersView.setContainersManager(containersManager);

        repositoryManager.addCallbacks(this::updateEngineSettings,
                e -> Platform.runLater(() -> {
                    final ErrorDialog errorDialog = ErrorDialog.builder()
                            .withMessage(tr("Loading engine settings failed."))
                            .withException(e)
                            .withOwner(this.containersView.getScene().getWindow())
                            .build();

                    errorDialog.showAndWait();
                }));

        repositoryManager.addCallbacks(this::updateVerbs,
                e -> Platform.runLater(() -> {
                    final ErrorDialog errorDialog = ErrorDialog.builder()
                            .withMessage(tr("Loading Verbs failed."))
                            .withException(e)
                            .withOwner(this.containersView.getScene().getWindow())
                            .build();

                    errorDialog.showAndWait();
                }));

        repositoryManager.addCallbacks(this::updateEngineTools,
                e -> Platform.runLater(() -> {
                    final ErrorDialog errorDialog = ErrorDialog.builder()
                            .withMessage(tr("Loading engine tools failed."))
                            .withException(e)
                            .withOwner(this.containersView.getScene().getWindow())
                            .build();

                    errorDialog.showAndWait();
                }));

        repositoryManager.triggerCallbacks();
    }

    public ContainersFeaturePanel getView() {
        return containersView;
    }

    private void updateEngineSettings(RepositoryDTO repositoryDTO) {
        this.engineSettingsManager.fetchAvailableEngineSettings(repositoryDTO,
                engineSettings -> Platform.runLater(() -> {
                    final ObservableMap<String, List<EngineSetting>> engineSettingsMap = this.containersView
                            .getEngineSettings();

                    engineSettingsMap.clear();
                    engineSettingsMap.putAll(engineSettings);
                }),
                e -> Platform.runLater(() -> {
                    final ErrorDialog errorDialog = ErrorDialog.builder()
                            .withMessage(tr("Loading engine tools failed."))
                            .withException(e)
                            .withOwner(this.containersView.getScene().getWindow())
                            .build();

                    errorDialog.showAndWait();
                }));
    }

    private void updateVerbs(RepositoryDTO repositoryDTO) {
        this.verbsManager.fetchAvailableVerbs(repositoryDTO, verbs -> Platform.runLater(() -> {
            final ObservableMap<String, ApplicationDTO> verbsMap = this.containersView.getVerbs();

            verbsMap.clear();
            verbsMap.putAll(verbs);
        }));
    }

    private void updateEngineTools(RepositoryDTO repositoryDTO) {
        this.engineToolsManager.fetchAvailableEngineTools(repositoryDTO, engineTools -> Platform.runLater(() -> {
            final ObservableMap<String, ApplicationDTO> engineToolsMap = this.containersView.getEngineTools();

            engineToolsMap.clear();
            engineToolsMap.putAll(engineTools);
        }));
    }
}
