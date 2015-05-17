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

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.ui.api.RemoteAvailableInstallers;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

import static com.playonlinux.domain.Localisation.translate;
import static java.lang.Integer.max;

class StatusBar extends javafx.scene.control.ToolBar implements Observer {
    private final MainWindow parent;
    private final Text text;
    private final ProgressBar progressBar = new ProgressBar();

    public StatusBar(MainWindow parent, Scene scene) {
        this.parent = parent;

        text = new Text();
        text.setWrappingWidth(max(50, (int)scene.getWidth() - 150));
        progressBar.setPrefWidth(130);
        this.getItems().addAll(text);

        parent.widthProperty().addListener((observable, oldValue, newValue) -> {
            text.setWrappingWidth(max(50, newValue.intValue() - 150));
        });

        this.setPrefHeight(25);
    }

    public void updateStatus(String newText) {
        text.setText(newText);
    }

    public void showStatusBar(boolean showStatusBar) {
        if(showStatusBar) {
            this.getItems().add(progressBar);
        } else {
            this.getItems().remove(progressBar);
        }
    }

    public void setUpEvents() throws PlayOnLinuxError {
        this.parent.getMainEventHandler().getRemoteAvailableInstallers().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        RemoteAvailableInstallers remoteAvailableInstallers = (RemoteAvailableInstallers) o;

        if(remoteAvailableInstallers.isUpdating()) {
            updateStatus(translate("Please wait while $APPLICATION_TITLE is refreshing itself"));
            Platform.runLater(() -> showStatusBar(true));
        } else if(remoteAvailableInstallers.hasFailed()) {
            updateStatus(translate("$APPLICATION_TITLE website seems to be unavailable!"));
            Platform.runLater(() -> showStatusBar(false));
        } else {
            updateStatus("");
            Platform.runLater(() -> showStatusBar(false));
        }
    }
}
