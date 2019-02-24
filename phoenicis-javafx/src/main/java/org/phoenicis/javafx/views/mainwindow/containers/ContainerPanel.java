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

package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.phoenicis.containers.ContainerEngineController;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.engines.EngineSetting;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.engines.VerbsManager;
import org.phoenicis.javafx.components.container.control.*;
import org.phoenicis.javafx.views.common.widgets.lists.DetailsView;
import org.phoenicis.repository.dto.ApplicationDTO;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class ContainerPanel extends DetailsView {
    private final ObjectProperty<WinePrefixContainerDTO> container;

    private final ObjectProperty<ContainerEngineController> containerEngineController;

    private final ObjectProperty<Consumer<ContainerDTO>> onDeleteContainer;

    private final ObjectProperty<Consumer<ContainerDTO>> onOpenFileBrowser;

    public ContainerPanel(ContainerDTO containerEntity, VerbsManager verbsManager,
            EngineToolsManager engineToolsManager, Optional<List<EngineSetting>> engineSettings,
            Optional<ApplicationDTO> verbs, Optional<ApplicationDTO> engineTools,
            ContainerEngineController containerEngineController) {
        super();

        this.setTitle(containerEntity.getName());
        this.container = new SimpleObjectProperty<>((WinePrefixContainerDTO) containerEntity);
        this.containerEngineController = new SimpleObjectProperty<>(containerEngineController);
        this.onDeleteContainer = new SimpleObjectProperty<>();
        this.onOpenFileBrowser = new SimpleObjectProperty<>();

        final TabPane tabPane = new TabPane();

        tabPane.getTabs().add(createContainerOverviewTab());

        if (engineSettings.isPresent()) {
            final ContainerEngineSettingsPanel containerEngineSettingsPanel = new ContainerEngineSettingsPanel();

            containerEngineSettingsPanel.containerProperty().bind(this.container);
            containerEngineSettingsPanel.getEngineSettings().setAll(engineSettings.get());

            final Tab engineSettingsTab = new Tab(tr(tr("Engine Settings")), containerEngineSettingsPanel);

            engineSettingsTab.setClosable(false);

            tabPane.getTabs().add(engineSettingsTab);
        }

        if (verbs.isPresent()) {
            final ContainerVerbsPanel containerVerbsPanel = new ContainerVerbsPanel();

            containerVerbsPanel.containerProperty().bind(this.container);
            containerVerbsPanel.setVerbs(verbs.get());
            containerVerbsPanel.setVerbsManager(verbsManager);

            final Tab verbsTab = new Tab(tr(tr("Verbs")), containerVerbsPanel);

            verbsTab.setClosable(false);

            tabPane.getTabs().add(verbsTab);
        }

        if (engineTools.isPresent()) {
            final ContainerEngineToolsPanel containerEngineToolsPanel = new ContainerEngineToolsPanel();

            containerEngineToolsPanel.containerProperty().bind(this.container);
            containerEngineToolsPanel.setEngineTools(engineTools.get());
            containerEngineToolsPanel.setEngineToolsManager(engineToolsManager);

            final Tab engineToolsTab = new Tab(tr("Engine tools"), containerEngineToolsPanel);

            engineToolsTab.setClosable(false);

            tabPane.getTabs().add(engineToolsTab);
        }

        tabPane.getTabs().add(createContainerToolsTab());

        this.setCenter(tabPane);
    }

    private Tab createContainerOverviewTab() {
        final ContainerOverviewPanel containerOverviewPanel = new ContainerOverviewPanel();

        containerOverviewPanel.containerProperty().bind(this.container);
        containerOverviewPanel.onDeleteContainerProperty().bind(this.onDeleteContainer);
        containerOverviewPanel.onOpenFileBrowserProperty().bind(this.onOpenFileBrowser);

        final Tab containerOverviewTab = new Tab(tr("Information"), containerOverviewPanel);

        containerOverviewTab.setClosable(false);

        return containerOverviewTab;
    }

    private Tab createContainerToolsTab() {
        final ContainerToolsPanel containerToolsPanel = new ContainerToolsPanel();

        containerToolsPanel.containerProperty().bind(this.container);
        containerToolsPanel.containerEngineControllerProperty().bind(this.containerEngineController);

        final Tab containerToolsTab = new Tab(tr("Tools"), containerToolsPanel);

        containerToolsTab.setClosable(false);

        return containerToolsTab;
    }

    public void setOnDeleteContainer(Consumer<ContainerDTO> onDeleteContainer) {
        this.onDeleteContainer.setValue(onDeleteContainer);
    }

    public void setOnOpenFileBrowser(Consumer<ContainerDTO> onOpenFileBrowser) {
        this.onOpenFileBrowser.setValue(onOpenFileBrowser);
    }
}
