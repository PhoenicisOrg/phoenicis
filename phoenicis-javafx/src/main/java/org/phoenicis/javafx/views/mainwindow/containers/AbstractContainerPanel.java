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

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.util.List;

abstract class AbstractContainerPanel<T> extends VBox {
    private final TabPane tabPane;

    AbstractContainerPanel(T containerEntity) {
        this.tabPane = new TabPane();

        this.getChildren().add(tabPane);

        this.getStyleClass().add("rightPane");
        this.tabPane.getTabs().add(drawInformationTab(containerEntity));
    }

    abstract Tab drawInformationTab(T container);

    public List<Tab> getTabs() {
        return tabPane.getTabs();
    }


}
