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

import static com.playonlinux.core.lang.Localisation.translate;

import java.io.File;
import java.util.List;

import com.playonlinux.core.messages.CancelerSynchronousMessage;

import javafx.scene.control.Button;
import javafx.stage.FileChooser;

public class StepRepresentationBrowse extends StepRepresentationMessage {
    private final File browseDirectory;
    private List<String> extensions;
    private File selectedFile;

    public StepRepresentationBrowse(SetupWindowJavaFXImplementation parent, CancelerSynchronousMessage message,
                                    String textToShow, File browseDirectory, List<String> extensions) {
        super(parent, message, textToShow);
        this.browseDirectory = browseDirectory;
        this.extensions = extensions;
    }

    @Override
    public void drawStepContent() {
        super.drawStepContent();

        final  Button browseButton = new Button(translate("Browse"));
        browseButton.setLayoutX(200);
        browseButton.setLayoutY(100);
        browseButton.setOnMouseClicked(event -> {
            final FileChooser fileChooser = new FileChooser();
            if(extensions != null) {
                fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(translate("Allowed files"), extensions));
            }
            fileChooser.setInitialDirectory(browseDirectory);

            selectedFile = fileChooser.showOpenDialog(this.getParentWindow());
        });


        this.addToStep(browseButton);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event ->
                        ((CancelerSynchronousMessage) this.getMessageAwaitingForResponse()).setResponse(selectedFile.toString())
        );
    }

}
