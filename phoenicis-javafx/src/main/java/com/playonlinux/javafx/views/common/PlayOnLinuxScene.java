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

package com.playonlinux.javafx.views.common;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.net.URL;

public class PlayOnLinuxScene extends Scene {
    private String theme;

    public PlayOnLinuxScene(Parent parent, String theme) {
        super(parent);
        this.theme = theme;
        applyTheme();
    }

    private void applyTheme() {
        URL style = PlayOnLinuxScene.class.getResource(themePath());

        if(style != null) {
            this.getStylesheets().add(style.toExternalForm());
        } else {
            final String failedTheme = theme;
            theme = "default";
            style = PlayOnLinuxScene.class.getResource(themePath());
            this.getStylesheets().add(style.toExternalForm());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Theme not found");
            alert.setHeaderText(String.format("Theme %s is not found!", failedTheme));
            alert.setContentText("The default theme will be used.");
            alert.showAndWait();
        }
    }

    private String themePath() {
        return String.format("/com/playonlinux/javafx/themes/%s/main.css", theme);
    }
}
