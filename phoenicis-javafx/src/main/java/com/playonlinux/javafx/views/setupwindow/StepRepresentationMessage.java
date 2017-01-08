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
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class StepRepresentationMessage extends AbstractStepRepresentationWithHeader {
    private final String textToShow;

    public StepRepresentationMessage(SetupWindowJavaFXImplementation parent, Message<?> message, String textToShow) {
        super(parent, message);
        this.textToShow = textToShow;
    }

    @Override
    protected void drawStepContent() {
        Text textWidget = new Text(textToShow);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        scrollPane.setStyle("-fx-background: #F4F4F4; -fx-border-color: #F4F4F4;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(new TextFlow(textWidget));

        this.addToContentPane(scrollPane);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event ->
            getMessageAwaitingForResponse().send(null)
        );
    }

}
