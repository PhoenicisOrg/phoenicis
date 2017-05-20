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
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.json.JSONException;
import org.json.JSONObject;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.library.dto.ShortcutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class LibraryPanel extends VBox {
    private final Logger LOGGER = LoggerFactory.getLogger(LibraryPanel.class);
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";

    public LibraryPanel(ShortcutDTO shortcutDTO) {
        super();
        this.setPadding(new Insets(10));

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

        vBox.getChildren().addAll(name, description, gridPane);
        this.getChildren().addAll(vBox);
    }
}
