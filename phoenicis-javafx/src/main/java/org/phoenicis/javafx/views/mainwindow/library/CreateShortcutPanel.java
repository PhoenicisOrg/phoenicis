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

package org.phoenicis.javafx.views.mainwindow.library;

import javafx.css.PseudoClass;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.javafx.views.common.widgets.lists.DetailsView;
import org.phoenicis.library.dto.ShortcutCreationDTO;

import java.io.File;
import java.net.URI;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * a details view which allows to edit a shortcut
 */
final class CreateShortcutPanel extends DetailsView {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private final String containersPath;

    // consumer called when a shortcut shall be created
    private Consumer<ShortcutCreationDTO> onCreateShortcut;

    /**
     * constructor
     * @param containersPath path to containers (usually ~/.Phoenicis/containers)
     */
    public CreateShortcutPanel(String containersPath) {
        super();
        this.containersPath = containersPath;
        this.populate();
    }

    /**
     * populates the panel
     */
    public void populate() {
        final PseudoClass errorClass = PseudoClass.getPseudoClass("error");

        final VBox vBox = new VBox();

        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid");
        gridPane.getColumnConstraints().addAll(new ColumnConstraintsWithPercentage(30),
                new ColumnConstraintsWithPercentage(70));

        // name
        Label nameLabel = new Label(tr("Name:"));
        nameLabel.getStyleClass().add(CAPTION_TITLE_CSS_CLASS);
        GridPane.setValignment(nameLabel, VPos.TOP);
        gridPane.add(nameLabel, 0, 0);

        TextField name = new TextField();
        gridPane.add(name, 1, 0);

        Tooltip nameErrorTooltip = new Tooltip(tr("Please specify a name!"));

        // category
        Label categoryLabel = new Label(tr("Category:"));
        categoryLabel.getStyleClass().add(CAPTION_TITLE_CSS_CLASS);
        GridPane.setValignment(categoryLabel, VPos.TOP);
        gridPane.add(categoryLabel, 0, 1);

        TextField category = new TextField();
        gridPane.add(category, 1, 1);

        Tooltip categoryErrorTooltip = new Tooltip(tr("Please specify a category!"));

        // description
        Label descriptionLabel = new Label(tr("Description:"));
        descriptionLabel.getStyleClass().add(CAPTION_TITLE_CSS_CLASS);
        GridPane.setValignment(descriptionLabel, VPos.TOP);
        gridPane.add(descriptionLabel, 0, 2);

        TextArea description = new TextArea();
        gridPane.add(description, 1, 2);

        // miniature
        Label miniatureLabel = new Label(tr("Miniature:"));
        miniatureLabel.getStyleClass().add(CAPTION_TITLE_CSS_CLASS);
        GridPane.setValignment(miniatureLabel, VPos.TOP);
        gridPane.add(miniatureLabel, 0, 3);

        TextField miniature = new TextField();

        Button openMiniatureBrowser = new Button(tr("Browse..."));
        openMiniatureBrowser.setOnAction(event -> {
            FileChooser chooser = new FileChooser();

            // open in containers directory if it exists
            File containersDir = new File(this.containersPath);
            if (containersDir.canRead()) {
                chooser.setInitialDirectory(containersDir);
            }

            chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(tr("Images"), "*.miniature, *.png"));

            File newMiniature = chooser.showOpenDialog(this.getScene().getWindow());
            if (newMiniature != null) {
                miniature.setText(newMiniature.toString());
            }
        });

        HBox miniatureHbox = new HBox(miniature, openMiniatureBrowser);
        HBox.setHgrow(miniature, Priority.ALWAYS);
        gridPane.add(miniatureHbox, 1, 3);

        Tooltip miniatureErrorTooltip = new Tooltip(tr("Please specify a valid miniature!"));

        // executable
        Label executableLabel = new Label(tr("Executable:"));
        executableLabel.getStyleClass().add(CAPTION_TITLE_CSS_CLASS);
        GridPane.setValignment(executableLabel, VPos.TOP);
        gridPane.add(executableLabel, 0, 4);

        TextField executable = new TextField();

        Button openExecutableBrowser = new Button(tr("Browse..."));
        openExecutableBrowser.setOnAction(event -> {
            FileChooser chooser = new FileChooser();

            // open in containers directory if it exists
            File containersDir = new File(this.containersPath);
            if (containersDir.canRead()) {
                chooser.setInitialDirectory(containersDir);
            }

            File newExecutable = chooser.showOpenDialog(this.getScene().getWindow());
            if (newExecutable != null) {
                executable.setText(newExecutable.toString());
            }
        });

        HBox executableHbox = new HBox(executable, openExecutableBrowser);
        HBox.setHgrow(executable, Priority.ALWAYS);
        gridPane.add(executableHbox, 1, 4);

        Tooltip executableErrorTooltip = new Tooltip(tr("Please specify a valid executable!"));

        Region spacer = new Region();
        spacer.getStyleClass().add("detailsButtonSpacer");

        Button createButton = new Button(tr("Create"));
        createButton.setOnMouseClicked(event -> {
            boolean error = false;
            if (StringUtils.isEmpty(name.getText())) {
                name.pseudoClassStateChanged(errorClass, true);
                name.setTooltip(nameErrorTooltip);
                error = true;
            }
            if (StringUtils.isEmpty(category.getText())) {
                category.pseudoClassStateChanged(errorClass, true);
                category.setTooltip(categoryErrorTooltip);
                error = true;
            }
            URI miniatureUri = null;
            // miniature null is ok (will use default)
            // but if a miniature is given, it must exist
            if (StringUtils.isNotEmpty(miniature.getText())) {
                File miniatureFile = new File(miniature.getText());
                if (miniatureFile.exists()) {
                    miniatureUri = miniatureFile.toURI();
                } else {
                    miniature.pseudoClassStateChanged(errorClass, true);
                    miniature.setTooltip(miniatureErrorTooltip);
                    error = true;
                }
            }
            File executableFile = new File(executable.getText());
            if (!executableFile.exists()) {
                executable.pseudoClassStateChanged(errorClass, true);
                executable.setTooltip(executableErrorTooltip);
                error = true;
            }
            if (!error) {
                ShortcutCreationDTO newShortcut = new ShortcutCreationDTO.Builder().withName(name.getText())
                        .withCategory(category.getText()).withDescription(description.getText())
                        .withMiniature(miniatureUri).withExecutable(executableFile).build();
                this.onCreateShortcut.accept(newShortcut);
            }
        });

        vBox.getChildren().addAll(gridPane, spacer, createButton);

        this.setCenter(vBox);
    }

    /**
     * updates the consumer that is called when a shortcut shall be created
     * @param onCreateShortcut The new consumer to be called
     */
    public void setOnCreateShortcut(Consumer<ShortcutCreationDTO> onCreateShortcut) {
        this.onCreateShortcut = onCreateShortcut;
    }

}
