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

package com.playonlinux.ui.impl.gtk.setupwindow;

import org.gnome.gtk.Label;

import com.playonlinux.core.messages.CancelerMessage;
import com.playonlinux.core.messages.CancelerSynchronousMessage;

public class StepRepresentationMessage extends AbstractStepRepresentationWithHeader {
    private final String textToShow;

    public StepRepresentationMessage(SetupWindowGTKImplementation parent, CancelerMessage message, String textToShow) {
        super(parent, message);
        this.textToShow = textToShow;
    }

    @Override
    protected void drawStepContent() {
        Label textWidget = new Label(textToShow);
        textWidget.setLineWrap(true);
        textWidget.setAlignment(0, 0);
        textWidget.setSizeRequest(500, 100);

        this.addToContentPanel(textWidget, 10, 20);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(
                event -> ((CancelerSynchronousMessage) this.getMessageAwaitingForResponse()).setResponse(null));
    }

}
