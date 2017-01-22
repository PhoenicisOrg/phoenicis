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

package com.playonlinux.javafx.views.setupwindow;


import com.phoenicis.entities.ProgressEntity;
import com.playonlinux.scripts.ui.Message;
import com.playonlinux.scripts.ui.ProgressControl;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

public class StepRepresentationProgressBar extends StepRepresentationMessage implements ProgressControl {
    private final ProgressBar progressBar = new ProgressBar();
    private final Text progressText = new Text("");

    public StepRepresentationProgressBar(SetupWindowJavaFXImplementation parent, Message<?> messageWaitingForResponse,
                                         String textToShow) {
        super(parent, messageWaitingForResponse, textToShow);
        progressBar.setProgress(0.0);

        progressText.setId("stepText");
    }


    @Override
    protected void drawStepContent() {
        super.drawStepContent();
        progressBar.setPrefHeight(30);
        progressBar.prefWidthProperty().bind(getContentPane().widthProperty());
        this.addToContentPane(progressBar);

        this.addToContentPane(progressText);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonEnabled(false);
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
