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

import com.playonlinux.core.messages.InterrupterAsynchroneousMessage;

import javafx.scene.control.ProgressIndicator;

public class StepRepresentationSpin extends StepRepresentationMessage {
    public StepRepresentationSpin(SetupWindowJavaFXImplementation parent, InterrupterAsynchroneousMessage messageWaitingForResponse,
                                  String textToShow) {
        super(parent, messageWaitingForResponse, textToShow);
    }


    @Override
    protected void drawStepContent() {
        super.drawStepContent();

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setLayoutX(330);
        progressIndicator.setLayoutY(100);

        this.addToContentPanel(progressIndicator);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonEnabled(false);
    }

}
