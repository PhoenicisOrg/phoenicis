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

import com.playonlinux.messages.CancelerSynchronousMessage;
import javafx.scene.control.TextField;

public class StepRepresentationTextBox extends StepRepresentationMessage {
    private final String defaultValue;
    private TextField textField;

    public StepRepresentationTextBox(SetupWindowJavaFXImplementation parent, CancelerSynchronousMessage messageWaitingForResponse, String textToShow,
                                     String defaultValue) {
        super(parent, messageWaitingForResponse, textToShow);

        this.defaultValue = defaultValue;
    }

    @Override
    protected void drawStepContent() {
        super.drawStepContent();

        textField = new TextField();
        textField.setText(defaultValue);
        textField.setLayoutX(10);
        textField.setLayoutY(40);

        this.addToContentPanel(textField);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event ->
            ((CancelerSynchronousMessage) this.getMessageAwaitingForResponse()).setResponse(textField.getText())
        );
    }

}
