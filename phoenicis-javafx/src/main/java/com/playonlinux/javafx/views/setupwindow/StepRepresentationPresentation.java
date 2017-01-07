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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class StepRepresentationPresentation extends AbstractStepRepresentation {
    private final String textToShow;

    public StepRepresentationPresentation(SetupWindowJavaFXImplementation parent, Message<?> message, String textToShow) {
        super(parent, message);
        this.textToShow = textToShow;
    }

    @Override
    protected void drawStepContent() {
        final String title = this.getParentWizardTitle();

        VBox contentPane = new VBox();
        contentPane.setPadding(new Insets(10));
        contentPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        TextFlow flow = new TextFlow();

        Text titleWidget = new Text(title + "\n\n");
        titleWidget.setFont(Font.font(null, FontWeight.BOLD, 16));

        Text textWidget = new Text(textToShow);

        flow.getChildren().addAll(titleWidget, textWidget);
        contentPane.getChildren().addAll(flow);
        getParent().getRoot().setCenter(contentPane);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event -> getMessageAwaitingForResponse().send(null));
    }

}
