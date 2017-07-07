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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.javafx.views.common.widgets.lists.DetailsView;
import org.phoenicis.library.dto.ShortcutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

final class LibraryPanel extends DetailsView {
    private final Logger LOGGER = LoggerFactory.getLogger(LibraryPanel.class);
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";

    private final ObjectMapper objectMapper;

    private Label description;
    private GridPane gridPane;
    private Button runButton;
    private Button stopButton;
    private Button uninstallButton;

    // consumers called when a shortcut should be run, stopped or uninstalled
    private Consumer<ShortcutDTO> onShortcutRun;
    private Consumer<ShortcutDTO> onShortcutStop;
    private Consumer<ShortcutDTO> onShortcutUninstall;

    public LibraryPanel(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;

        final VBox vBox = new VBox();

        this.description = new Label();
        this.description.setWrapText(true);

        this.gridPane = new GridPane();
        this.gridPane.getStyleClass().add("grid");
        this.gridPane.getColumnConstraints().addAll(new ColumnConstraintsWithPercentage(30),
                new ColumnConstraintsWithPercentage(70));

        Region spacer = new Region();
        spacer.setPrefHeight(40);

        GridPane buttons = new GridPane();
        buttons.setAlignment(Pos.CENTER);

        ColumnConstraints runColumn = new ColumnConstraints();
        runColumn.setPercentWidth(30);
        ColumnConstraints stopColumn = new ColumnConstraints();
        stopColumn.setPercentWidth(30);
        ColumnConstraints uninstallColumn = new ColumnConstraints();
        uninstallColumn.setPercentWidth(30);
        buttons.getColumnConstraints().addAll(runColumn, stopColumn, uninstallColumn);

        this.runButton = new Button(tr("Run"));
        this.runButton.getStyleClass().addAll("shortcutButton", "runButton");
        buttons.add(this.runButton, 0, 0);

        this.stopButton = new Button(tr("Close"));
        this.stopButton.getStyleClass().addAll("shortcutButton", "stopButton");
        buttons.add(this.stopButton, 1, 0);

        this.uninstallButton = new Button(tr("Uninstall"));
        this.uninstallButton.getStyleClass().addAll("shortcutButton", "uninstallButton");
        buttons.add(this.uninstallButton, 2, 0);

        vBox.getChildren().addAll(this.description, this.gridPane, spacer, buttons);

        this.setCenter(vBox);
    }

    public void setShortcutDTO(ShortcutDTO shortcutDTO) {
        this.setTitle(shortcutDTO.getName());

        this.description.setText(shortcutDTO.getDescription());

        this.gridPane.getChildren().clear();

        try {
            LOGGER.info("Reading shortcut: {}", shortcutDTO.getScript());

            final Map<String, Object> shortcutProperties = objectMapper.readValue(shortcutDTO.getScript(),
                    new TypeReference<Map<String, Object>>() {
                    });

            int i = 0;
            for (Map.Entry<String, Object> entry : shortcutProperties.entrySet()) {
                final Label keyLabel = new Label(tr(unCamelize(entry.getKey())) + ":");
                keyLabel.getStyleClass().add(CAPTION_TITLE_CSS_CLASS);
                GridPane.setValignment(keyLabel, VPos.TOP);
                this.gridPane.add(keyLabel, 0, i);

                final Label valueLabel = new Label(entry.getValue().toString());
                valueLabel.setWrapText(true);
                this.gridPane.add(valueLabel, 1, i);

                i++;
            }

        } catch (IOException e) {
            LOGGER.warn("Could not parse shortcut script JSON", e);
        }

        this.runButton.setOnMouseClicked(event -> onShortcutRun.accept(shortcutDTO));
        this.stopButton.setOnMouseClicked(event -> onShortcutStop.accept(shortcutDTO));
        this.uninstallButton.setOnMouseClicked(event -> onShortcutUninstall.accept(shortcutDTO));
    }

    private String unCamelize(String s) {
        return StringUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(s), ' '));
    }

    /**
     * This method updates the consumer, that is called when the "Run" button for the currently selected shortcut has been clicked.
     *
     * @param onShortcutRun The new consumer to be called
     */
    public void setOnShortcutRun(Consumer<ShortcutDTO> onShortcutRun) {
        this.onShortcutRun = onShortcutRun;
    }

    /**
     * This method updates the consumer, that is called when the "Stop" button for the currently selected shortcut has been clicked.
     *
     * @param onShortcutStop The new consumer to be called
     */
    public void setOnShortcutStop(Consumer<ShortcutDTO> onShortcutStop) {
        this.onShortcutStop = onShortcutStop;
    }

    /**
     * This method updates the consumer, that is called when the "Uninstall" button for the currently selected shortcut has been clicked.
     *
     * @param onShortcutUninstall The new consumer to be called
     */
    public void setOnShortcutUninstall(Consumer<ShortcutDTO> onShortcutUninstall) {
        this.onShortcutUninstall = onShortcutUninstall;
    }

}
