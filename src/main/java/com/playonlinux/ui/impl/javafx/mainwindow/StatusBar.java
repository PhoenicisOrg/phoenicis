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

import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.lang.Integer.max;

class StatusBar extends javafx.scene.control.ToolBar {
    public StatusBar(Stage stage, Scene scene) {
        ProgressBar progressBar = new ProgressBar();

        Text text = new Text("A new version of PlayOnLinux is available (5.1)");

        text.setWrappingWidth(max(50, (int)scene.getWidth() - 150));
        progressBar.setPrefWidth(130);

        this.getItems().addAll(text, progressBar);

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            text.setWrappingWidth(max(50, newValue.intValue() - 150));
        });
    }
}
