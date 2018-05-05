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

import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.ContainerEngineController;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.repository.dto.ApplicationDTO;

import java.util.Optional;
import java.util.function.Consumer;

public class ContainerPanel extends AbstractContainerPanel<WinePrefixContainerDTO> {
    private ContainerInformationTab informationTab;

    public ContainerPanel(WinePrefixContainerDTO containerEntity,
            EngineToolsManager engineToolsManager,
            Optional<ApplicationDTO> engineTools,
            ContainerEngineController containerEngineController) {
        super(containerEntity);

        this.informationTab = new ContainerInformationTab(containerEntity);
        this.tabPane.getTabs().add(this.informationTab);
        ContainerDisplayTab displayTab = new ContainerDisplayTab(containerEntity, containerEngineController);
        this.tabPane.getTabs().add(displayTab);
        ContainerInputTab inputTab = new ContainerInputTab(containerEntity);
        this.tabPane.getTabs().add(inputTab);
        if (engineTools.isPresent()) {
            ContainerEngineToolsTab wineToolsTab = new ContainerEngineToolsTab(containerEntity, engineToolsManager,
                    engineTools.get());
            this.tabPane.getTabs().add(wineToolsTab);
        }
        ContainerToolsTab toolsTab = new ContainerToolsTab(containerEntity, containerEngineController);
        this.tabPane.getTabs().add(toolsTab);
    }

    public void setOnDeletePrefix(Consumer<WinePrefixContainerDTO> onDeletePrefix) {
        this.informationTab.setOnDeletePrefix(onDeletePrefix);
    }
}
