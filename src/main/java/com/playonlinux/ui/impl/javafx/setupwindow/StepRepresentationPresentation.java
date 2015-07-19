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

import com.playonlinux.core.messages.CancelerMessage;
import com.playonlinux.core.messages.CancelerSynchronousMessage;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class StepRepresentationPresentation extends AbstractStepRepresentation {
    private final String textToShow;

    public StepRepresentationPresentation(SetupWindowJavaFXImplementation parent, CancelerMessage message, String textToShow) {
        super(parent, message);
        this.textToShow = textToShow;
    }

    private Image createLeftImage() {
        return new Image(this.getParentLeftImage().toExternalForm());
    }

    @Override
    protected void drawStepContent() {
        String title = this.getParentWizardTitle();

        ImageView leftImage = new ImageView(this.createLeftImage());
        leftImage.setLayoutX(0);
        leftImage.setLayoutY(0);

        Pane contentPane = new Pane();
        contentPane.setPrefSize(370, 355);
        contentPane.setLayoutX(151);
        contentPane.setLayoutY(0);
        contentPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        Text titleWidget = new Text(title);
        titleWidget.setLayoutX(10);
        titleWidget.setLayoutY(30);
        titleWidget.setFont(Font.font(null, FontWeight.BOLD, 16));

        Text textWidget = new Text(textToShow);
        textWidget.setLayoutX(10);
        textWidget.setLayoutY(80);
        textWidget.setWrappingWidth(350);
        textWidget.prefWidth(350);

        contentPane.getChildren().addAll(titleWidget, textWidget);
        this.addToStep(leftImage);
        this.addToStep(contentPane);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event ->
            ((CancelerSynchronousMessage) this.getMessageAwaitingForResponse()).setResponse(null)
        );
    }

}
