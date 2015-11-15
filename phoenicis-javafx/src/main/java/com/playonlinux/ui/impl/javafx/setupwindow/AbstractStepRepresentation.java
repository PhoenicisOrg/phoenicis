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

import java.net.URL;

import com.playonlinux.core.messages.CancelerMessage;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

// TODO: Fix this class using CSS and containers instead of static sizes
abstract class AbstractStepRepresentation {
    private final CancelerMessage messageWaitingForResponse;
    private final SetupWindowJavaFXImplementation parent;
    private Button nextButton;

    AbstractStepRepresentation(SetupWindowJavaFXImplementation parent, CancelerMessage messageWaitingForResponse) {
        this.parent = parent;
        this.messageWaitingForResponse = messageWaitingForResponse;
    }

    protected SetupWindowJavaFXImplementation getParent() {
        return this.parent;
    }

    protected Stage getParentWindow() {
        return this.parent.getParentWindow();
    }

    protected Pane getParentRoot() {
        return this.parent.getRoot();
    }

    protected URL getParentTopImage() {
        return this.parent.getTopImage();
    }

    protected URL getParentLeftImage() {
        return this.parent.getLeftImage();
    }

    protected void addToStep(Node widgetToAdd) {
        this.parent.addNode(widgetToAdd);
    }

    public String getParentWizardTitle() {
        return this.parent.getWizardTitle();
    }

    public void clearAllOnParent() {
        this.parent.clearAll();
    }

    protected CancelerMessage getMessageAwaitingForResponse() {
        return messageWaitingForResponse;
    }

    protected void drawFooter() {
        Pane footer = new Pane();
        footer.setPrefSize(722, 45);
        footer.setLayoutX(-1);
        footer.setLayoutY(444);
        footer.setId("footer");
        this.addToStep(footer);

        nextButton = new Button("Next");
        nextButton.setLayoutY(9);
        nextButton.setLayoutX(635);
        nextButton.setPrefSize(70, 28);

        Button cancelButton = new Button("Cancel");
        cancelButton.setLayoutY(9);
        cancelButton.setLayoutX(555);
        cancelButton.setPrefSize(70, 28);

        footer.getChildren().addAll(nextButton, cancelButton);

        cancelButton.setOnMouseClicked(event -> {
            cancelButton.setDisable(true);
            messageWaitingForResponse.sendCancelSignal();
        });
    }

    protected void setNextButtonAction(EventHandler<MouseEvent> nextButtonAction) {
        nextButton.setOnMouseClicked(event -> {
            nextButton.setDisable(true);
            nextButtonAction.handle(event);
        });

        this.getParent().setOnCloseRequest(event -> {
            if (this.messageWaitingForResponse != null) {
                this.messageWaitingForResponse.sendCancelSignal();
            }
            event.consume();
        });
    }

    protected void setNextButtonEnabled(Boolean nextEnabled) {
        nextButton.setDisable(!nextEnabled);
    }

    protected abstract void drawStepContent();

    protected abstract void setStepEvents();

    public void installStep() {
        this.parent.clearAll();
        this.drawFooter();
        this.setStepEvents();
        this.drawStepContent();

    }


}
