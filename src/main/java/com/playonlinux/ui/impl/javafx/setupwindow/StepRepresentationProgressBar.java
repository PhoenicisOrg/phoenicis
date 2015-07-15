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

package com.playonlinux.ui.impl.javafx.setupwindow;

import com.playonlinux.dto.ui.ProgressStateDTO;
import com.playonlinux.core.messages.AsynchroneousMessage;
import com.playonlinux.core.messages.InterrupterSynchronousMessage;
import com.playonlinux.core.messages.Message;
import com.playonlinux.ui.api.ProgressControl;
import com.playonlinux.ui.impl.javafx.UIMessageSenderJavaFXImplementation;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

public class StepRepresentationProgressBar extends StepRepresentationMessage implements ProgressControl {
    ProgressBar progressBar = new ProgressBar();
    Text progressText = new Text("");

    public StepRepresentationProgressBar(SetupWindowJavaFXImplementation parent, InterrupterSynchronousMessage messageWaitingForResponse,
                                         String textToShow) {
        super(parent, messageWaitingForResponse, textToShow);
        progressBar.setProgress(0.0);
    }


    @Override
    protected void drawStepContent() {
        super.drawStepContent();
        progressBar.setLayoutY(60);
        progressBar.setLayoutX(30);
        progressBar.setPrefSize(460, 30);
        this.addToContentPanel(progressBar);

        progressText.setLayoutX(10);
        progressText.setLayoutY(120);
        progressText.setWrappingWidth(500);
        progressText.prefWidth(500);
        this.addToContentPanel(progressText);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonEnabled(false);
    }

    @Override
    public void setProgressPercentage(double value) {
        UIMessageSenderJavaFXImplementation messageSender = new UIMessageSenderJavaFXImplementation();
        messageSender.asynchronousSend(new AsynchroneousMessage() {
                                           @Override
                                           public void execute(Message message) {
                                               progressBar.setProgress(value / 100.);
                                           }
                                       }
        );
    }

    @Override
    public void setText(String text) {
        UIMessageSenderJavaFXImplementation messageSender = new UIMessageSenderJavaFXImplementation();
        messageSender.asynchronousSend(new AsynchroneousMessage() {
                                           @Override
                                           public void execute(Message message) {
                                               progressText.setText(text);
                                           }
                                       }
        );
    }


    @Override
    public void update(com.playonlinux.utils.observer.Observable observable, ProgressStateDTO argument) {
        ProgressStateDTO progressStateDTO = (ProgressStateDTO) argument;

        this.setProgressPercentage(progressStateDTO.getPercent());
    }
}
