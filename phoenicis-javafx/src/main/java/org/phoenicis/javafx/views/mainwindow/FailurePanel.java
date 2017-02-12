/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.javafx.views.mainwindow;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.phoenicis.javafx.views.common.ThemeManager;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class FailurePanel extends VBox {
    private final Button retryButton;

    public FailurePanel(ThemeManager themeManager) {
        this.getStyleClass().add("rightPane");

        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);

        Label failureNotificationLbl = new Label();
        failureNotificationLbl.setText(translate("Connecting to ${application.name} failed.\n" +
                "Please check your connection and try again."));
        failureNotificationLbl.setTextAlignment(TextAlignment.CENTER);

        final String iconPath = "/icons/mainwindow/refresh.png";
        retryButton = new Button(translate("Retry"));
        retryButton.getStyleClass().add("retryButton");
        retryButton.setStyle("-fx-background-image: url('" + themeManager.getResourceUrl(iconPath) + "');");

        this.getChildren().addAll(failureNotificationLbl, retryButton);
    }

    public Button getRetryButton() {
        return retryButton;
    }
}
