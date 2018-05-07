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

import javafx.scene.control.TabPane;
import org.phoenicis.containers.ContainerEngineController;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.engines.EngineSetting;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.javafx.views.common.widgets.lists.DetailsView;
import org.phoenicis.repository.dto.ApplicationDTO;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ContainerPanel extends DetailsView {
    private ContainerInformationTab informationTab;
    private final TabPane tabPane;

    public ContainerPanel(WinePrefixContainerDTO containerEntity,
            EngineToolsManager engineToolsManager,
            Optional<List<EngineSetting>> engineSettings,
            Optional<ApplicationDTO> engineTools,
            ContainerEngineController containerEngineController) {
        this.tabPane = new TabPane();
        this.setTitle(containerEntity.getName());
        this.setCenter(tabPane);

        this.informationTab = new ContainerInformationTab(containerEntity);
        this.tabPane.getTabs().add(this.informationTab);
        if (engineSettings.isPresent()) {
            ContainerEngineSettingsTab settingsTab = new ContainerEngineSettingsTab(containerEntity,
                    engineSettings.get());
            this.tabPane.getTabs().add(settingsTab);
        }
        if (engineTools.isPresent()) {
            ContainerEngineToolsTab engineToolsTab = new ContainerEngineToolsTab(containerEntity, engineToolsManager,
                    engineTools.get());
            this.tabPane.getTabs().add(engineToolsTab);
        }
        ContainerToolsTab toolsTab = new ContainerToolsTab(containerEntity, containerEngineController);
        this.tabPane.getTabs().add(toolsTab);
    }

    public void setOnDeletePrefix(Consumer<ContainerDTO> onDeletePrefix) {
        this.informationTab.setOnDeletePrefix(onDeletePrefix);
    }
}
