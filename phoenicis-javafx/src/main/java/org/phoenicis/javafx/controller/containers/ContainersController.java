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
import org.phoenicis.containers.ContainerEngineController;
import org.phoenicis.containers.ContainersManager;
import org.phoenicis.engines.EngineSetting;
import org.phoenicis.engines.EngineSettingsManager;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.engines.VerbsManager;
import org.phoenicis.javafx.dialogs.ConfirmDialog;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.javafx.views.mainwindow.containers.ContainersView;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.RepositoryDTO;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class ContainersController {
    private final ContainersView containersView;
    private final ContainersManager containersManager;
    private EngineSettingsManager engineSettingsManager;
    private final EngineToolsManager engineToolsManager;
    private final VerbsManager verbsManager;

    private boolean firstViewSelection = true;

    public ContainersController(ContainersView containersView, ContainersManager containersManager,
            ContainerEngineController containerEngineController, RepositoryManager repositoryManager,
            EngineSettingsManager engineSettingsManager, VerbsManager verbsManager,
            EngineToolsManager engineToolsManager) {
        this.containersView = containersView;
        this.containersManager = containersManager;
        this.engineSettingsManager = engineSettingsManager;
        this.verbsManager = verbsManager;
        this.engineToolsManager = engineToolsManager;

        this.containersView.setOnSelectionChanged(event -> {
            if (this.containersView.isSelected()) {
                if (this.firstViewSelection) {
                    repositoryManager.addCallbacks(this::updateEngineSettings,
                            e -> Platform.runLater(() -> {
                                final ErrorDialog errorDialog = ErrorDialog.builder()
                                        .withMessage(tr("Loading engine settings failed."))
                                        .withException(e)
                                        .withOwner(this.containersView.getContent().getScene().getWindow())
                                        .build();

                                errorDialog.showAndWait();
                            }));

                    repositoryManager.addCallbacks(this::updateVerbs,
                            e -> Platform.runLater(() -> {
                                final ErrorDialog errorDialog = ErrorDialog.builder()
                                        .withMessage(tr("Loading Verbs failed."))
                                        .withException(e)
                                        .withOwner(this.containersView.getContent().getScene().getWindow())
                                        .build();

                                errorDialog.showAndWait();
                            }));

                    repositoryManager.addCallbacks(this::updateEngineTools,
                            e -> Platform.runLater(() -> {
                                final ErrorDialog errorDialog = ErrorDialog.builder()
                                        .withMessage(tr("Loading engine tools failed."))
                                        .withException(e)
                                        .withOwner(this.containersView.getContent().getScene().getWindow())
                                        .build();

                                errorDialog.showAndWait();
                            }));

                    repositoryManager.triggerCallbacks();
                    this.firstViewSelection = false;
                }

                loadContainers();
            }
        });

        this.containersView.setVerbsManager(verbsManager);
        this.containersView.setEngineToolsManager(engineToolsManager);
        this.containersView.setContainerEngineController(containerEngineController);

        this.containersView.setOnSelectContainer(container -> {
            // TODO: better way to get engine ID
            final String engineId = container.getEngine().toLowerCase();

            this.containersView.setContainer(container);
        });

        this.containersView.setOnDeleteContainer(container -> {
            final ConfirmDialog confirmMessage = ConfirmDialog.builder()
                    .withTitle(tr("Delete {0} container", container.getName()))
                    .withMessage(tr("Are you sure you want to delete the {0} container?",
                            container.getName()))
                    .withOwner(containersView.getContent().getScene().getWindow())
                    .withResizable(true)
                    .withYesCallback(() -> {
                        containersManager.deleteContainer(container, e -> Platform.runLater(() -> {
                            final ErrorDialog errorDialog = ErrorDialog.builder()
                                    .withMessage(tr("Error"))
                                    .withException(e)
                                    .withOwner(this.containersView.getContent().getScene().getWindow())
                                    .build();

                            errorDialog.showAndWait();
                        }));

                        loadContainers();
                    })
                    .build();

            confirmMessage.showAndCallback();
        });

        this.containersView.setOnOpenFileBrowser(container -> {
            try {
                File containerDir = new File(container.getPath());
                EventQueue.invokeLater(() -> {
                    try {
                        Desktop.getDesktop().open(containerDir);
                    } catch (IOException e) {
                        Platform.runLater(() -> {
                            final ErrorDialog errorDialog = ErrorDialog.builder()
                                    .withMessage(
                                            tr("Cannot open container {0} in file browser", container.getPath()))
                                    .withException(e)
                                    .withOwner(this.containersView.getContent().getScene().getWindow())
                                    .build();

                            errorDialog.showAndWait();
                        });
                    }
                });
            } catch (IllegalArgumentException e) {
                Platform.runLater(() -> {
                    final ErrorDialog errorDialog = ErrorDialog.builder()
                            .withMessage(tr("Cannot open container {0} in file browser", container.getPath()))
                            .withException(e)
                            .withOwner(this.containersView.getContent().getScene().getWindow())
                            .build();

                    errorDialog.showAndWait();
                });
            }
        });
    }

    public ContainersView getView() {
        return containersView;
    }

    public void loadContainers() {
        this.containersView.showWait();
        this.containersManager.fetchContainers(containersView::populate,
                e -> this.containersView.showFailure(tr("Loading containers failed."), Optional
                        .of(e)));
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
                            .withOwner(this.containersView.getContent().getScene().getWindow())
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
