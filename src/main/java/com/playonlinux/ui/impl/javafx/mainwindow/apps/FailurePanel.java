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

package com.playonlinux.ui.impl.javafx.mainwindow.apps;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import static com.playonlinux.lang.Localisation.translate;

final class FailurePanel extends VBox {
    private final Button retryButton;

    public FailurePanel() {
        this.getStyleClass().add("rightPane");

        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);

        Label failureNotificationLbl = new Label();
        failureNotificationLbl.setText(translate("Connecting to ${application.name} failed.\n" +
                "Please check your connection and try again."));
        failureNotificationLbl.setTextAlignment(TextAlignment.CENTER);

        ImageView retryImage = new ImageView(new Image(getClass().getResourceAsStream("refresh.png")));
        retryImage.setFitWidth(16);
        retryImage.setFitHeight(16);
        retryButton = new Button(translate("Retry"), retryImage);

        this.getChildren().addAll(failureNotificationLbl, retryButton);
    }

    public Button getRetryButton() {
        return retryButton;
    }
}
