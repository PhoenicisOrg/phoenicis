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

package com.playonlinux.javafx.views.mainwindow;

import com.playonlinux.javafx.views.mainwindow.ui.LeftSideBar;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class MainWindowView extends HBox {
    private final LeftSideBar leftContent;

    private Node visiblePane;

    public MainWindowView() {
        this.getStyleClass().add("mainWindowScene");
        leftContent = new LeftSideBar();
    }

    protected void drawSideBar() {
        this.getChildren().add(leftContent);
    }

    protected void clearSideBar() {
        leftContent.getChildren().clear();
    }

    protected void addToSideBar(Node... nodes) {
        leftContent.getChildren().addAll(nodes);
    }

    public void showRightView(Node nodeToShow) {
        if(visiblePane != null) {
            this.getChildren().remove(visiblePane);
        }
        this.visiblePane = nodeToShow;
        this.getChildren().add(visiblePane);
    }
}
