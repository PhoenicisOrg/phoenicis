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

package com.playonlinux.ui.impl.javafx.common;

import com.playonlinux.common.Progressable;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;


import java.util.Observable;
import java.util.Observer;

public class  ProgressButton extends Button implements Observer {
    private final ProgressIndicator progressIndicator;
    private boolean forceDisabled = false;
    private boolean isDisabled = false;

    public ProgressButton(String text, ImageView image) {
        super(text, image);

        progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefWidth(16.);
        progressIndicator.setPrefHeight(16.);
        progressIndicator.getStyleClass().add("smallProgressIndicator");

    }

    public void setEnableIfPosible(boolean state) {
        isDisabled = !state;
        super.setDisable(isDisabled || forceDisabled);
    }

    @Override
    public void update(Observable o, Object arg) {
        Progressable progressable = (Progressable) o;
        this.update(progressable);
    }

    private void update(Progressable progressable) {
        if(progressable.isProgressing()) {
            this.forceDisabled = true;
            Platform.runLater(() -> this.setGraphic(progressIndicator));
        } else {
            this.forceDisabled = false;
        }

        this.setEnableIfPosible(!isDisabled);
    }
}
