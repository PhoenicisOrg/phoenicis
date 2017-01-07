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

import com.playonlinux.scripts.ui.Message;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;

// TODO: Fix this class using CSS and containers instead of static sizes
abstract class AbstractStepRepresentation {
    private final Message<?> messageWaitingForResponse;
    private final SetupWindowJavaFXImplementation parent;
    private Button nextButton;

    AbstractStepRepresentation(SetupWindowJavaFXImplementation parent, Message<?> messageWaitingForResponse) {
        this.parent = parent;
        this.messageWaitingForResponse = messageWaitingForResponse;
    }

    protected SetupWindowJavaFXImplementation getParent() {
        return this.parent;
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

    protected Message<?> getMessageAwaitingForResponse() {
        return messageWaitingForResponse;
    }


    private Image createLeftImage() {
        return new Image(this.getParentLeftImage().toExternalForm());
    }

    protected void drawLeftImage() {
        final ImageView leftImage = new ImageView(this.createLeftImage());
        leftImage.setLayoutX(0);
        leftImage.setLayoutY(0);
        leftImage.setFitHeight(444);
        leftImage.setFitWidth(187);

        getParent().getRoot().setLeft(leftImage);
    }

    protected void drawFooter() {
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(8));
        footer.setSpacing(10);
        footer.setPrefHeight(45);
        footer.setId("footer");
        getParent().getRoot().setBottom(footer);

        nextButton = new Button("Next");
        nextButton.setPrefSize(70, 28);

        Button cancelButton = new Button("Cancel");
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
        this.drawLeftImage();
        this.drawFooter();
        this.setStepEvents();
        this.drawStepContent();
    }


}
