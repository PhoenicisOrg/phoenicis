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

import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.json.JSONException;
import org.json.JSONObject;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.library.dto.ShortcutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

final class LibraryPanel extends VBox {
    private final Logger LOGGER = LoggerFactory.getLogger(LibraryPanel.class);
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";

    // consumers called when a shortcut should be run, stopped or uninstalled
    private Consumer<ShortcutDTO> onShortcutRun;
    private Consumer<ShortcutDTO> onShortcutStop;
    private Consumer<ShortcutDTO> onShortcutUninstall;

    public LibraryPanel() {
        super();
        this.setPadding(new Insets(10));
    }

    public void setShortcutDTO(ShortcutDTO shortcutDTO) {
        this.getChildren().clear();

        final VBox vBox = new VBox();
        Label name = new Label(shortcutDTO.getName());
        name.getStyleClass().add("descriptionTitle");
        name.setWrapText(true);

        Label description = new Label(shortcutDTO.getDescription());
        description.setWrapText(true);

        final GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid");

        try {
            JSONObject jsonObject = new JSONObject(shortcutDTO.getScript());

            for (int i = 0; i < jsonObject.names().length(); i++) {
                String key = jsonObject.names().getString(i);
                Label keyLabel = new Label(key + ":");
                keyLabel.getStyleClass().add(CAPTION_TITLE_CSS_CLASS);
                GridPane.setValignment(keyLabel, VPos.TOP);
                gridPane.add(keyLabel, 0, i);
                Label valueLabel = new Label(jsonObject.getString(key));
                valueLabel.setWrapText(true);
                gridPane.add(valueLabel, 1, i);
            }

        } catch (JSONException e) {
            LOGGER.warn("Could not parse shortcut script JSON", e);
        }

        gridPane.getColumnConstraints().addAll(new ColumnConstraintsWithPercentage(30),
                new ColumnConstraintsWithPercentage(70));

        Button runButton = new Button(translate("Run"));
        runButton.getStyleClass().addAll("buttonWithIcon", "runButton");
        runButton.setOnMouseClicked(event -> onShortcutRun.accept(shortcutDTO));

        Button stopButton = new Button(translate("Close"));
        stopButton.getStyleClass().addAll("buttonWithIcon", "stopButton");
        stopButton.setOnMouseClicked(event -> onShortcutStop.accept(shortcutDTO));

        Button uninstallButton = new Button(translate("Uninstall"));
        uninstallButton.getStyleClass().addAll("buttonWithIcon", "uninstallButton");
        uninstallButton.setOnMouseClicked(event -> onShortcutUninstall.accept(shortcutDTO));

        Region spacer = new Region();
        spacer.setPrefHeight(40);

        vBox.getChildren().addAll(name, description, gridPane, spacer, runButton, stopButton, uninstallButton);

        this.getChildren().addAll(vBox);
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
