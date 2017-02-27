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

package org.phoenicis.javafx.views.scriptui;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.entities.ProgressEntity;
import org.phoenicis.scripts.ui.Message;
import org.phoenicis.scripts.ui.ProgressControl;
import org.phoenicis.scripts.ui.ProgressUi;

public class ProgressUiJavaFXImplementation extends VBox implements ProgressUi, ProgressControl {
    private final ProgressBar progressBar = new ProgressBar();
    private final Text progressText = new Text("");

    private Runnable onShouldClose = () -> {};

    public ProgressUiJavaFXImplementation() {
        super();
        progressBar.setProgress(0.0);
        progressText.setId("stepText");
        progressBar.setPrefHeight(30);
        progressBar.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().addAll(progressBar, progressText);
    }

    @Override
    public void showProgressBar(Message<ProgressControl> message, String textToShow) {
        progressBar.setProgress(0.0);
        message.send(this);
    }

    @Override
    public void close() {
        onShouldClose.run();
    }


    public void setOnShouldClose(Runnable onShouldClose) {
        this.onShouldClose = onShouldClose;
    }

    @Override
    public void setProgressPercentage(double value) {
        Platform.runLater(() -> progressBar.setProgress(value / 100.));
    }

    @Override
    public void setText(String text) {
        Platform.runLater(() -> progressText.setText(text));
    }

    @Override
    public void accept(ProgressEntity progressStateEntity) {
        this.setProgressPercentage(progressStateEntity.getPercent());
        this.setText(progressStateEntity.getProgressText());
    }
}
