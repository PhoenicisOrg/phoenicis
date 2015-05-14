/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.ui.impl.javafx.mainwindow;

import javafx.application.Platform;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;

class MenuLeft extends Accordion {
    public MenuLeft() {
        super();

        TitledPane mainTitledPane = new TitledPane();
        mainTitledPane.setText("Titre");
        mainTitledPane.setContent(new Button());

        TitledPane mainTitledPane2 = new TitledPane();
        mainTitledPane2.setText("Titre");
        mainTitledPane2.setContent(new Button());

        this.getPanes().add(mainTitledPane);
        this.getPanes().add(mainTitledPane2);

        this.setExpandedPane(mainTitledPane);

        this.expandedPaneProperty().addListener((property, oldPane, newPane) -> {
            if (oldPane != null) oldPane.setCollapsible(true);
            if (newPane != null) Platform.runLater(() -> newPane.setCollapsible(false));
        });
    }
}
