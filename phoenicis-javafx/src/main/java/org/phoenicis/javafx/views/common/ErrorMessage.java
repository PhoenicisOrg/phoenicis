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

package org.phoenicis.javafx.views.common;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.phoenicis.javafx.views.mainwindow.ui.MainWindowView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * error message dialog
 */
public class ErrorMessage {
    private final Logger LOGGER = LoggerFactory.getLogger(ErrorMessage.class);
    private final Alert alert;

    /**
     * constructor
     * @param message error message
     * @param exception exception which caused the error
     */
    public ErrorMessage(String message, Exception exception) {
        this(message, exception, (Window) null);
    }

    /**
     * constructor
     * @param message error message
     * @param exception exception which caused the error
     * @param mainWindowView MainWindowView which shows the error message
     */
    public ErrorMessage(String message, Exception exception, MainWindowView mainWindowView) {
        this(message, exception, mainWindowView.getContent().getScene().getWindow());
    }

    /**
     * constructor
     * @param message error message
     * @param exception exception which caused the error
     * @param owner owning window
     */
    public ErrorMessage(String message, Exception exception, Window owner) {
        this.alert = new Alert(Alert.AlertType.ERROR);
        this.alert.setResizable(true);
        this.alert.setTitle(tr("Error"));
        this.alert.setHeaderText(message);

        if (exception != null) {
            LOGGER.error(ExceptionUtils.getStackTrace(exception));

            this.alert.setContentText(exception.getMessage());

            final String exceptionText = ExceptionUtils.getFullStackTrace(exception);

            final Label label = new Label(tr("Stack trace:"));

            final TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            final GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);
            this.alert.getDialogPane().setExpandableContent(expContent);

            // fix content not resized if expanded content hidden again
            this.alert.getDialogPane().expandedProperty().addListener(
                    observable -> Platform.runLater(() -> {
                        this.alert.getDialogPane().requestLayout();
                        final Stage stage = (Stage) this.alert.getDialogPane().getScene().getWindow();
                        stage.sizeToScene();
                    }));
        }
        this.alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        this.alert.initOwner(owner);
        this.alert.showAndWait();
    }

    /**
     * shows the error message
     */
    public void show() {
        this.alert.show();
    }
}
