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
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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

    public String getParentWizardTitle() {
        return this.parent.getWizardTitle();
    }

    public void clearAllOnParent() {
        this.parent.clearAll();
    }

    protected Message<?> getMessageAwaitingForResponse() {
        return messageWaitingForResponse;
    }

    protected void drawLeftImage() {
        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(187);
        Stop[] stops = new Stop[] { new Stop(0, Color.web("#3c79b2")), new Stop(1, Color.web("#2d5d8b"))};
        RadialGradient gradient = new RadialGradient(0,0,0.5,0.5,1,true, CycleMethod.NO_CYCLE, stops);

        Background background = new Background(new BackgroundFill(gradient,null,null));
        pane.setBackground(background);

        Text text = new Text(this.parent.getLeftImageText());
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Maven Pro", 50));
        text.setRotate(-90);
        pane.setPadding(new Insets(-50));
        pane.getChildren().add(text);
        AnchorPane.setBottomAnchor(text, 160.0);
        AnchorPane.setRightAnchor(text, -40.0);

        getParent().getRoot().setLeft(pane);
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
