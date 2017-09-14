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
import org.phoenicis.containers.ContainersManager;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.WinePrefixContainerController;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.engines.EnginesSource;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.mainwindow.containers.ContainerPanelFactory;
import org.phoenicis.javafx.views.mainwindow.containers.ContainersView;
import org.phoenicis.javafx.views.mainwindow.containers.WinePrefixContainerPanel;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.RepositoryDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class ContainersController {
    private final ContainersView containersView;
    private final ContainersManager containersManager;
    private final ContainerPanelFactory<WinePrefixContainerPanel, WinePrefixContainerDTO> winePrefixContainerPanelFactory;
    private final WinePrefixContainerController winePrefixContainerController;
    private final RepositoryManager repositoryManager;
    private final EnginesSource enginesSource;
    private final EngineToolsManager engineToolsManager;
    private Map<String, ApplicationDTO> engineTools; // engine tools per engine

    public ContainersController(ContainersView containersView,
            ContainersManager containersManager,
            ContainerPanelFactory<WinePrefixContainerPanel, WinePrefixContainerDTO> winePrefixContainerPanelFactory,
            WinePrefixContainerController winePrefixContainerController,
            RepositoryManager repositoryManager,
            EnginesSource enginesSource,
            EngineToolsManager engineToolsManager) {
        this.containersView = containersView;
        this.containersManager = containersManager;
        this.winePrefixContainerPanelFactory = winePrefixContainerPanelFactory;
        this.winePrefixContainerController = winePrefixContainerController;
        this.enginesSource = enginesSource;
        this.repositoryManager = repositoryManager;
        this.engineToolsManager = engineToolsManager;

        this.engineTools = new HashMap<>();

        this.repositoryManager.addCallbacks(this::updateEngineTools,
                e -> Platform.runLater(() -> new ErrorMessage(tr("Loading engines failed."), e, this.containersView)));

        containersView.setOnSelectionChanged(event -> {
            if (containersView.isSelected()) {
                loadContainers();
            }
        });

        containersView.setOnSelectContainer((ContainerDTO containerDTO) -> {
            // disabled fetching of available engines
            // changing engine does not work currently
            // querying Wine webservice causes performance issues on systems with slow internet connection
            // List<CategoryDTO> categoryDTOS = Collections.singletonList(new CategoryDTO.Builder().withName("Wine").build());
            // enginesSource.fetchAvailableEngines(categoryDTOS, engineCategoryDTOS -> {
            final WinePrefixContainerPanel panel = winePrefixContainerPanelFactory.createContainerPanel(
                    (WinePrefixContainerDTO) containerDTO,
                    new ArrayList<>(),
                    /*engineCategoryDTOS.stream().flatMap(category -> category.getSubCategories().stream())
                    .flatMap(subCategory -> subCategory.getPackages().stream())
                    .collect(Collectors.toList()),*/
                    engineToolsManager,
                    Optional.ofNullable(engineTools.get("Wine")),
                    winePrefixContainerController);

            panel.setOnDeletePrefix(winePrefixDTO -> {
                new ConfirmMessage(tr("Delete {0} container", winePrefixDTO.getName()),
                        tr("Are you sure you want to delete the {0} container?", winePrefixDTO.getName()))
                                .ask(() -> {
                                    containersManager.deleteContainer(winePrefixDTO,
                                            e -> Platform.runLater(
                                                    () -> new ErrorMessage("Error", e, this.containersView).show()));
                                    loadContainers();
                                });
            });

            panel.setOnClose(containersView::closeDetailsView);

            Platform.runLater(() -> containersView.showDetailsView(panel));
            //});
        });
    }

    public ContainersView getView() {
        return containersView;
    }

    public void loadContainers() {
        this.containersView.showWait();
        containersManager.fetchContainers(containersView::populate,
                e -> this.containersView.showFailure(tr("Loading containers failed."), Optional
                        .of(e)));
    }

    private void updateEngineTools(RepositoryDTO repositoryDTO) {
        engineToolsManager.fetchAvailableEngineTools(repositoryDTO,
                engineTools -> Platform.runLater(() -> this.engineTools = engineTools));
    }
}
