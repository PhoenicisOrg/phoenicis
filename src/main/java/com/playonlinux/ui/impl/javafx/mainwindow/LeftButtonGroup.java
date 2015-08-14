/*
 * Copyright (C) 2015 Markus Ebner
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


import javafx.scene.layout.VBox;

import java.util.List;


public final class LeftButtonGroup extends VBox {

    private final String name;

    public LeftButtonGroup(String name) {
        this.name = name;
        this.getStyleClass().add("leftPaneInside");
        this.clear();
    }

    private void clear() {
        this.getChildren().clear();
        this.getChildren().add(new LeftBarTitle(name));
    }

    public void setButtons(List<LeftButton> buttons) {
        this.clear();

        for(LeftButton leftButton: buttons) {
            this.getChildren().addAll(leftButton);
        }

    }



}
