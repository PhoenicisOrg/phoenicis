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

package com.playonlinux.ui.impl.javafx;

import com.playonlinux.domain.PlayOnLinuxException;
import javafx.application.Application;

import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.playonlinux.ui.impl.javafx.mainwindow.MainWindow;

public class JavaFXApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("common/playonlinux.png")));
        primaryStage.setTitle("PlayOnLinux");
        loadFonts();

        MainWindow mainWindow = new MainWindow();

        mainWindow.setUpWindow();
        try {
            mainWindow.setUpEvents();
        } catch (PlayOnLinuxException e) {
            // FIXME
            e.printStackTrace();
        }
        mainWindow.show();
    }

    private void loadFonts() {
        Font.loadFont(getClass().getResource("common/mavenpro/MavenPro-Medium.ttf").toExternalForm(), 12);
    }

}
