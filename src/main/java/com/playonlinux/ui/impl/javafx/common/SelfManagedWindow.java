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

import com.playonlinux.ui.api.PlayOnLinuxWindow;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class SelfManagedWindow extends Stage implements PlayOnLinuxWindow {

    private double resizeDx;
    private double resizeDy;
    private boolean resizebottom;
    private double resizeXOffset;
    private double resizeYOffset;

    public SelfManagedWindow() {
        this.initStyle(StageStyle.UNDECORATED);
    }


    public void setMouseEvents(Node dragger) {
        this.getScene().setOnMousePressed(event -> {
            if (event.getX() > getWidth() - 10
                    && event.getX() < getWidth() + 10
                    && event.getY() > getHeight() - 10
                    && event.getY() < getHeight() + 10) {
                resizebottom = true;
                resizeDx = getWidth() - event.getX();
                resizeDy = getHeight() - event.getY();
            } else {
                resizebottom = false;
                resizeXOffset = event.getSceneX();
                resizeYOffset = event.getSceneY();
            }
        });

        this.getScene().setOnMouseDragged(event -> {
            if (!resizebottom) {
                if(resizeXOffset > dragger.getLayoutX() &&
                        resizeXOffset < dragger.getLayoutX() + dragger.getBoundsInParent().getWidth() &&
                        resizeYOffset > dragger.getLayoutY() &&
                        resizeYOffset < dragger.getLayoutY() + dragger.getBoundsInParent().getHeight() ) {
                    this.setX(event.getScreenX() - resizeXOffset);
                    this.setY(event.getScreenY() - resizeYOffset);
                }

            } else {
                    this.setWidth(event.getX() + resizeDx);
                    this.setHeight(event.getY() + resizeDy);
            }
        });
    }

}
