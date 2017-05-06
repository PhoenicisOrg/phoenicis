/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.javafx.views.scriptui;

import org.phoenicis.scripts.ui.Message;
import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class StepRepresentationBrowse extends AbstractStepRepresentationWithHeader {
    private final File browseDirectory;
    private final Message<String> message;
    private final String textToShow;
    private List<String> extensions;
    private File selectedFile;

    public StepRepresentationBrowse(SetupUiJavaFXImplementation parent, Message<String> message, String textToShow,
            File browseDirectory, List<String> extensions) {
        super(parent, message);
        this.browseDirectory = browseDirectory;
        this.extensions = extensions;
        this.message = message;
        this.textToShow = textToShow;
    }

    @Override
    public void drawStepContent() {
        this.addToContentPane(dragPane());
        this.setNextButtonEnabled(false);
    }

    private Node dragPane() {
        final Text textLabel = new Text(textToShow);
        final Text dragLabel = new Text("Please click here, or drag a file to me.");
        textLabel.getStyleClass().add("boldLabel");
        dragLabel.getStyleClass().addAll("normalLabel");

        final VBox dragTarget = new VBox();
        dragTarget.getChildren().addAll(textLabel, dragLabel);
        dragTarget.setOnDragOver(event -> {
            if (event.getGestureSource() != dragTarget && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        dragTarget.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                this.selectFile(db.getFiles().get(0));
                success = true;
            }

            event.setDropCompleted(success);

            event.consume();
        });

        dragTarget.setPrefSize(660, 308);
        dragTarget.getStyleClass().addAll("dragAndDropBox");

        dragTarget.setOnMouseClicked(event -> {
            final FileChooser fileChooser = new FileChooser();
            if (extensions != null) {
                fileChooser.setSelectedExtensionFilter(
                        new FileChooser.ExtensionFilter(translate("Allowed files"), extensions));
            }
            fileChooser.setInitialDirectory(browseDirectory);

            File dialogResult = fileChooser.showOpenDialog(null);
            if (dialogResult != null) {
                selectFile(dialogResult);
            }
        });

        return dragTarget;
    }

    private void selectFile(File selectedFile) {
        if (selectedFile != null) {
            message.send(selectedFile.getAbsolutePath());
        }
    }

    @Override
    protected void setStepEvents() {
        this.setNextButtonAction(event -> message.send(selectedFile.toString()));
    }

}
