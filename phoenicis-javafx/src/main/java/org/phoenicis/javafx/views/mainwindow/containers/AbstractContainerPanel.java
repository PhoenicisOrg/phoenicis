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
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.common.widgets.lists.DetailsView;

import java.util.List;

abstract class AbstractContainerPanel<T extends ContainerDTO> extends DetailsView {
    protected final ThemeManager themeManager;
    protected final TabPane tabPane;

    AbstractContainerPanel(T containerEntity, ThemeManager themeManager, List<EngineVersionDTO> engineVersions) {
        this.themeManager = themeManager;
        this.tabPane = new TabPane();

        this.setTitle(containerEntity.getName());
        this.setCenter(tabPane);
    }
}
