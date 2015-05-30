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

import com.playonlinux.domain.PlayOnLinuxException;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.apache.log4j.Logger;

import static com.playonlinux.domain.Localisation.translate;

class ToolBar extends javafx.scene.control.ToolBar {
    private final MainWindow parent;
    private final Button configureButton;
    private final Button installButton;
    private final Logger logger = Logger.getLogger(ToolBar.class);

    public ToolBar(MainWindow parent) {
        this.parent = parent;
        ImageView runImage = new ImageView(this.getClass().getResource("play.png").toExternalForm());
        runImage.setFitWidth(16);
        runImage.setFitHeight(16);

        ImageView stopImage = new ImageView(this.getClass().getResource("stop.png").toExternalForm());
        stopImage.setFitWidth(16);
        stopImage.setFitHeight(16);

        ImageView installImage = new ImageView(this.getClass().getResource("install.png").toExternalForm());
        installImage.setFitWidth(16);
        installImage.setFitHeight(16);

        ImageView removeImage = new ImageView(this.getClass().getResource("delete.png").toExternalForm());
        removeImage.setFitWidth(16);
        removeImage.setFitHeight(16);

        ImageView configureImage = new ImageView(this.getClass().getResource("configure.png").toExternalForm());
        configureImage.setFitWidth(16);
        configureImage.setFitHeight(16);


        Button run = new Button(translate("Run"), runImage);
        run.setContentDisplay(ContentDisplay.LEFT);

        Button stop = new Button(translate("Stop"), stopImage);
        stop.setContentDisplay(ContentDisplay.LEFT);

        installButton = new Button(translate("Install"), installImage);
        installButton.setContentDisplay(ContentDisplay.LEFT);

        Button remove = new Button(translate("Remove"), removeImage);
        remove.setContentDisplay(ContentDisplay.LEFT);

        configureButton = new Button(translate("Configure"), configureImage);
        configureButton.setContentDisplay(ContentDisplay.LEFT);

        TextField searchField = new TextField();


        PlayOnLinuxLogo playonlinuxLogo = new PlayOnLinuxLogo();
        this.getItems().addAll(
                playonlinuxLogo,
                run,
                stop,
                new Separator(),
                installButton,
                remove,
                new Separator(),
                configureButton,
                new Separator(),
                searchField
        );
    }

    public void setUpEvents() {
        configureButton.setOnMouseClicked(event -> {
            try {
                this.parent.getMainEventHandler().openConfigureWindow(this.parent, this.parent.getSelectedApplication());
            } catch (PlayOnLinuxException playOnLinuxException) {
                logger.warn(playOnLinuxException);
            }
        });

        installButton.setOnMouseClicked(event -> {
            try {
                this.parent.getMainEventHandler().openInstallWindow(this.parent);
            } catch (PlayOnLinuxException playOnLinuxException) {
                logger.warn(playOnLinuxException);
            }
        });
    }
}
