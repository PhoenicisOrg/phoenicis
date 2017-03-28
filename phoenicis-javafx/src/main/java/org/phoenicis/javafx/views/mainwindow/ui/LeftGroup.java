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

package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import java.util.List;


public class LeftGroup extends VBox {
    private String name;
    private List<? extends Node> nodes;

    public LeftGroup(String name) {
        this.name = name;
        this.getStyleClass().add("leftPaneInside");
        this.clear();
    }

    public void setName(String name) {
        this.name = name;
        clear();
        setNodes(nodes);
    }

    private void clear() {
        this.getChildren().clear();
        this.getChildren().add(new TextFlow(new LeftBarTitle(name)));
    }

    public void setNodes(List<? extends Node> nodes) {
        this.clear();
        this.nodes = nodes;
        for(Node node: nodes) {
            this.getChildren().add(node);
        }
    }


}
