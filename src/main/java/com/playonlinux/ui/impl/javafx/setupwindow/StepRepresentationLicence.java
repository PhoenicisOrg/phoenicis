/*
 * Copyright (C) 2015 SLAGMOLEN Raphaël
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

import static com.playonlinux.domain.Localisation.translate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
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

import com.playonlinux.common.messages.CancelerMessage;
import com.playonlinux.common.messages.CancelerSynchroneousMessage;
import com.playonlinux.domain.CancelException;

public class StepRepresentationLicence extends AbstractStepRepresentation {
    String textToShow;
    String licenceText;

    public StepRepresentationLicence(SetupWindowJavaFXImplementation parent, CancelerMessage message, String textToShow, String licenceText) {
        super(parent, message);
        this.textToShow = textToShow;
        this.licenceText = licenceText;
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
        
        TextArea licenceWidget = new TextArea(licenceText);
        licenceWidget.setLayoutX(10);
        licenceWidget.setLayoutY(100);
        licenceWidget.setMaxWidth(350);
        licenceWidget.setEditable(false);
        
        CheckBox confirmWidget = new CheckBox(translate("I agree"));
        confirmWidget.setOnAction((event) -> {
            confirmWidget.setSelected(true);
            setNextButtonEnabled(true);
        });
        confirmWidget.setLayoutX(10);
        confirmWidget.setLayoutY(300);
        setNextButtonEnabled(false);

        contentPane.getChildren().addAll(titleWidget, textWidget, licenceWidget, confirmWidget);
        this.addToStep(leftImage);
        this.addToStep(contentPane);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event ->
            ((CancelerSynchroneousMessage) this.getMessageAwaitingForResponse()).setResponse(null)
        );
    }

}
