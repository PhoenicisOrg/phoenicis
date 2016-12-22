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
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

import static com.playonlinux.configuration.localisation.Localisation.translate;

public class StepRepresentationBrowse extends StepRepresentationMessage {
    private final File browseDirectory;
    private final Message<String> message;
    private List<String> extensions;
    private File selectedFile;

    public StepRepresentationBrowse(SetupWindowJavaFXImplementation parent, Message<String> message,
                                    String textToShow, File browseDirectory, List<String> extensions) {
        super(parent, message, textToShow);
        this.browseDirectory = browseDirectory;
        this.extensions = extensions;
        this.message = message;
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

            File dialogResult = fileChooser.showOpenDialog(null);
            if(dialogResult != null){
                selectedFile = dialogResult;
                setNextButtonEnabled(true);
            }
        });

        this.addToStep(browseButton);
        this.setNextButtonEnabled(false);
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event ->
                        message.send(selectedFile.toString())
        );
    }

}
