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

import static com.playonlinux.lang.Localisation.translate;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import com.playonlinux.messages.CancelerMessage;
import com.playonlinux.messages.CancelerSynchronousMessage;

public class StepRepresentationLicence extends AbstractStepRepresentationWithHeader {
    private String textToShow;
    private String licenceText;
    private boolean isAgree;

    public StepRepresentationLicence(SetupWindowJavaFXImplementation parent, CancelerMessage message, String textToShow, String licenceText) {
        super(parent, message);
        this.textToShow = textToShow;
        this.licenceText = licenceText;
    }

    @Override
    protected void drawStepContent() {

        Text textWidget = new Text(textToShow);
        textWidget.setLayoutX(10);
        textWidget.setLayoutY(40);
        textWidget.setWrappingWidth(350);
        textWidget.prefWidth(350);
        
        TextArea licenceWidget = new TextArea(licenceText);
        licenceWidget.setLayoutX(10);
        licenceWidget.setLayoutY(80);
        licenceWidget.setMinWidth(500);
        licenceWidget.setMaxWidth(500);
        licenceWidget.setMinHeight(230);
        licenceWidget.setMaxHeight(230);
        licenceWidget.setEditable(false);
        
        CheckBox confirmWidget = new CheckBox(translate("I agree"));
        confirmWidget.setOnAction((event) -> {
            isAgree = !isAgree;
            confirmWidget.setSelected(isAgree);
            setNextButtonEnabled(isAgree);
        });
        confirmWidget.setLayoutX(10);
        confirmWidget.setLayoutY(330);
        setNextButtonEnabled(false);
        
        this.addToStep(textWidget);
        this.addToStep(licenceWidget);
        this.addToStep(confirmWidget);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event ->
            ((CancelerSynchronousMessage) this.getMessageAwaitingForResponse()).setResponse(null)
        );
    }

}
