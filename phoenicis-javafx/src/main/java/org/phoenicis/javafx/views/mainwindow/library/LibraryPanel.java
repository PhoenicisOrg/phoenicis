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
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.javafx.views.common.widgets.lists.DetailsView;
import org.phoenicis.library.dto.ShortcutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

final class LibraryPanel extends DetailsView {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private final Logger LOGGER = LoggerFactory.getLogger(LibraryPanel.class);

    private final ShortcutDTO shortcut;
    private final ObjectMapper objectMapper;

    public LibraryPanel(ShortcutDTO shortcut,
                        ObjectMapper objectMapper) {
        super();

        this.shortcut = shortcut;
        this.objectMapper = objectMapper;

        this.setTitle(shortcut.getName());

        this.populateContent();
    }

    private void populateContent() {
        final VBox vBox = new VBox();

        Label description = new Label(shortcut.getDescription());
        description.setWrapText(true);

        final GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("grid");

        try {
            System.out.println(shortcut.getScript());
            final Map<String, String> shortcutProperties =
                    objectMapper.readValue(shortcut.getScript(), new TypeReference<Map<String, String>>() {});

            int i = 0;
            for(String shortcutKey: shortcutProperties.keySet()) {
                final Label keyLabel = new Label(shortcutKey + ":");
                keyLabel.getStyleClass().add(CAPTION_TITLE_CSS_CLASS);
                GridPane.setValignment(keyLabel, VPos.TOP);
                gridPane.add(keyLabel, 0, i);

                final Label valueLabel = new Label(shortcutProperties.get(shortcutKey));
                valueLabel.setWrapText(true);
                gridPane.add(valueLabel, 1, i);

                i++;
            }

        } catch (IOException e) {
            LOGGER.warn("Could not parse shortcut script JSON", e);
        }

        gridPane.getColumnConstraints().addAll(new ColumnConstraintsWithPercentage(30),
                new ColumnConstraintsWithPercentage(70));

        vBox.getChildren().addAll(description, gridPane);

        this.setCenter(vBox);
    }
}
