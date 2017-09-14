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
import org.phoenicis.containers.wine.WinePrefixContainerController;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.repository.dto.ApplicationDTO;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class WinePrefixContainerPanel extends AbstractContainerPanel<WinePrefixContainerDTO> {
    private WinePrefixContainerInformationTab informationTab;
    private WinePrefixContainerDisplayTab displayTab;
    private WinePrefixContainerInputTab inputTab;
    private WinePrefixContainerWineToolsTab wineToolsTab;
    private WinePrefixContainerToolsTab toolsTab;

    public WinePrefixContainerPanel(WinePrefixContainerDTO containerEntity,
            List<EngineVersionDTO> engineVersions,
            EngineToolsManager engineToolsManager,
            Optional<ApplicationDTO> engineTools,
            WinePrefixContainerController winePrefixContainerController) {
        super(containerEntity, engineVersions);

        this.informationTab = new WinePrefixContainerInformationTab(containerEntity, engineVersions);
        this.tabPane.getTabs().add(this.informationTab);
        this.displayTab = new WinePrefixContainerDisplayTab(containerEntity, winePrefixContainerController);
        this.tabPane.getTabs().add(this.displayTab);
        this.inputTab = new WinePrefixContainerInputTab(containerEntity);
        this.tabPane.getTabs().add(this.inputTab);
        if (engineTools.isPresent()) {
            this.wineToolsTab = new WinePrefixContainerWineToolsTab(containerEntity, winePrefixContainerController,
                    engineToolsManager, engineTools.get());
            this.tabPane.getTabs().add(this.wineToolsTab);
        }
        this.toolsTab = new WinePrefixContainerToolsTab(containerEntity, winePrefixContainerController,
                engineToolsManager);
        this.tabPane.getTabs().add(this.toolsTab);
    }

    public void setOnDeletePrefix(Consumer<WinePrefixContainerDTO> onDeletePrefix) {
        this.informationTab.setOnDeletePrefix(onDeletePrefix);
    }
}
