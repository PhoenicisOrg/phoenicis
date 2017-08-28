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

package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class FailurePanel extends VBox {
    private final Button retryButton;
    final Label failureNotification;
    final Label failureReason;

    public FailurePanel() {
        this.getStyleClass().add("rightPane");

        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);

        this.failureNotification = new Label();
        this.failureNotification.setTextAlignment(TextAlignment.CENTER);

        this.failureReason = new Label();
        this.failureReason.setTextAlignment(TextAlignment.CENTER);

        this.retryButton = new Button(tr("Retry"));
        this.retryButton.getStyleClass().addAll("retryButton", "refreshIcon");

        this.getChildren().addAll(this.failureNotification, this.failureReason, this.retryButton);
    }

    /**
     * sets the failure which is displayed
     * @param notification notification text
     * @param reason exception which caused the failure
     */
    public void setFailure(String notification, Optional<Exception> reason) {
        this.failureNotification.setText(notification);
        if (reason.isPresent()) {
            this.failureReason.setText(tr("Reason: ") + reason.get().getLocalizedMessage());
        } else {
            // reset
            this.failureReason.setText("");
        }
    }

    public Button getRetryButton() {
        return this.retryButton;
    }
}
