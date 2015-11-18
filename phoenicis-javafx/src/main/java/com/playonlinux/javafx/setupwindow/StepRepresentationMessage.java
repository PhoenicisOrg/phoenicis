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

package com.playonlinux.javafx.setupwindow;

import com.playonlinux.core.messages.CancelerMessage;
import com.playonlinux.core.messages.CancelerSynchronousMessage;

import javafx.scene.text.Text;

public class StepRepresentationMessage extends AbstractStepRepresentationWithHeader {
    private final String textToShow;

    public StepRepresentationMessage(SetupWindowJavaFXImplementation parent, CancelerMessage message, String textToShow) {
        super(parent, message);
        this.textToShow = textToShow;
    }

    @Override
    protected void drawStepContent() {
        Text textWidget = new Text(textToShow);
        textWidget.setLayoutX(10);
        textWidget.setLayoutY(20);
        textWidget.setWrappingWidth(700);
        textWidget.prefWidth(700);

        this.addToContentPanel(textWidget);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event ->
            ((CancelerSynchronousMessage) this.getMessageAwaitingForResponse()).setResponse(null)
        );
    }

}
