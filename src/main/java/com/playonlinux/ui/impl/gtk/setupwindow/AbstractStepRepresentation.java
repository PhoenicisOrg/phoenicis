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
import org.gnome.gtk.*;


import java.net.URL;

abstract class AbstractStepRepresentation {
    private final CancelerMessage messageWaitingForResponse;
    private final SetupWindowGTKImplementation parent;
    private Button nextButton;

    AbstractStepRepresentation(SetupWindowGTKImplementation parent, CancelerMessage messageWaitingForResponse) {
        this.parent = parent;
        this.messageWaitingForResponse = messageWaitingForResponse;
    }

    protected SetupWindowGTKImplementation getParent() {
        return this.parent;
    }

    protected Fixed getParentRoot() {
        return this.parent.getRoot();
    }

    protected URL getParentTopImage() {
        return this.parent.getTopImage();
    }

    protected URL getParentLeftImage() {
        return this.parent.getLeftImage();
    }

    protected void addToStep(Widget widgetToAdd, int x, int y) {
        this.getParentRoot().put(widgetToAdd, x, y);
    }

    public String getParentWizardTitle() {
        return this.parent.getWizardTitle();
    }


    protected CancelerMessage getMessageAwaitingForResponse() {
        return messageWaitingForResponse;
    }

    protected void drawFooter() {
        Fixed footer = new Fixed();
        footer.setSizeRequest(522, 45);

        this.addToStep(footer, 0, 356);

        nextButton = new Button("Next");
        nextButton.setSizeRequest(70, 28);

        Button cancelButton = new Button("Cancel");

        cancelButton.setSizeRequest(70, 28);

        footer.put(nextButton, 435, 9);
        footer.put(cancelButton, 335, 9);

        cancelButton.connect((Button.Clicked) (Button button) -> {
            cancelButton.setSensitive(false);
            messageWaitingForResponse.sendCancelSignal();
        });
    }

    protected void setNextButtonAction(Button.Clicked nextButtonAction) {
        nextButton.connect((Button.Clicked) (Button button) -> {
            nextButton.setSensitive(false);
            nextButtonAction.onClicked(button);
        });

        this.getParent().connect((Window.DeleteEvent) (widget, event) -> {
            if (messageWaitingForResponse != null) {
                messageWaitingForResponse.sendCancelSignal();
            }
            return false;
        });
    }

    protected void setNextButtonEnabled(Boolean nextEnabled) {
        nextButton.setSensitive(nextEnabled);
    }

    protected abstract void drawStepContent();

    protected abstract void setStepEvents();

    public void installStep() {
        this.clearAllOnParent();
        this.drawFooter();
        this.setStepEvents();
        this.drawStepContent();
    }

    protected void clearAllOnParent() {
        for(Widget child: getParentRoot().getChildren()) {
            child.destroy();
        }
    }


}
