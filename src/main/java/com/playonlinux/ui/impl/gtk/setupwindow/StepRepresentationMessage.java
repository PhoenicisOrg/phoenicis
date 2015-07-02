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

import com.playonlinux.messages.CancelerMessage;
import com.playonlinux.messages.CancelerSynchroneousMessage;
import org.gnome.gtk.Align;
import org.gnome.gtk.Label;

public class StepRepresentationMessage extends AbstractStepRepresentationWithHeader {
    String textToShow;

    public StepRepresentationMessage(SetupWindowGTKImplementation parent, CancelerMessage message, String textToShow) {
        super(parent, message);
        this.textToShow = textToShow;
    }

    @Override
    protected void drawStepContent() {
        Label textWidget = new Label(textToShow);
        textWidget.setLineWrap(true);
        textWidget.setSizeRequest(500, 100);
        textWidget.setAlignHorizontal(Align.START);
        textWidget.setAlignVertical(Align.FILL);

        this.addToContentPanel(textWidget, 10, 20);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event ->
            ((CancelerSynchroneousMessage) this.getMessageAwaitingForResponse()).setResponse(null)
        );
    }

}
