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

import static org.phoenicis.configuration.localisation.Localisation.translate;

final class LibraryPanel extends VBox {
    private final Logger LOGGER = LoggerFactory.getLogger(LibraryPanel.class);

    public LibraryPanel(ShortcutDTO shortcutDTO) {
        super();
        this.setPadding(new Insets(10));

        final GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid");

        gridPane.add(new Label(translate("Name:")), 0, 0);
        Label name = new Label(shortcutDTO.getName());
        name.setWrapText(true);
        gridPane.add(name, 1, 0);

        Label descriptionLabel = new Label(translate("Description:"));
        GridPane.setValignment(descriptionLabel, VPos.TOP);
        gridPane.add(descriptionLabel, 0, 1);
        Label description = new Label(shortcutDTO.getDescription());
        description.setWrapText(true);
        gridPane.add(description, 1, 1);

        try {
            JSONObject jsonObject = new JSONObject(shortcutDTO.getScript());

            for (int i = 0; i < jsonObject.names().length(); i++) {
                String key = (String) jsonObject.names().getString(i);
                Label keyLabel = new Label(key);
                GridPane.setValignment(keyLabel, VPos.TOP);
                gridPane.add(keyLabel, 0, 3 + i);
                Label valueLabel = new Label(jsonObject.getString(key));
                valueLabel.setWrapText(true);
                gridPane.add(valueLabel, 1, 3 + i);
            }

        } catch (JSONException e) {
            LOGGER.warn("Could not parse shortcut script JSON", e);
        }

        gridPane.getColumnConstraints().addAll(new ColumnConstraintsWithPercentage(30),
                new ColumnConstraintsWithPercentage(70));

        getChildren().addAll(gridPane);
    }
}
