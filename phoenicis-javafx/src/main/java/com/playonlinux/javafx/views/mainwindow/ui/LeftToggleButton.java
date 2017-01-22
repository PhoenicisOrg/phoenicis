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

package com.playonlinux.javafx.views.mainwindow.ui;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class LeftToggleButton extends ToggleButton {
    private final String name;

    public LeftToggleButton(byte[] icon, String name) {
        this(icon == null ? null : new ByteArrayInputStream(icon), name);
    }

    public LeftToggleButton(String iconName, String name) {
        this(LeftToggleButton.class.getResourceAsStream(iconName), name);
    }

    private LeftToggleButton(InputStream icon, String name) {
        super(name);
        this.name = name;
        this.getStyleClass().add("leftButton");
        this.setPrefWidth(Double.MAX_VALUE);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(2));

        if(icon != null) {
            final ImageView iconView = new ImageView(new Image(icon));
            iconView.setFitWidth(24);
            iconView.setFitHeight(24);
            this.setGraphic(iconView);
        }
    }

    public String getName() {
        return name;
    }
}
