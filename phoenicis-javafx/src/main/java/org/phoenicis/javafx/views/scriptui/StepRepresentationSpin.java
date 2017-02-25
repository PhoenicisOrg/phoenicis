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

import org.phoenicis.scripts.ui.Message;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class StepRepresentationSpin extends StepRepresentationMessage {
    public StepRepresentationSpin(SetupUiJavaFXImplementation parent, Message<?> messageWaitingForResponse,
                                  String textToShow) {
        super(parent, messageWaitingForResponse, textToShow);
    }


    @Override
    protected void drawStepContent() {
        super.drawStepContent();

        Region spacerAbove = new Region();
        VBox.setVgrow(spacerAbove, Priority.ALWAYS);
        this.addToContentPane(spacerAbove);

        ProgressIndicator progressIndicator = new ProgressIndicator();
        this.addToContentPane(progressIndicator);

        Region spacerBelow = new Region();
        VBox.setVgrow(spacerBelow, Priority.ALWAYS);
        this.addToContentPane(spacerBelow);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonEnabled(false);
    }

}
