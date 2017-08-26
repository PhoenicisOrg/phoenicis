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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

/**
 * a details view which allows to edit a shortcut
 */
final class EditShortcutPanel extends DetailsView {
    private final Logger LOGGER = LoggerFactory.getLogger(EditShortcutPanel.class);
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";

    private ShortcutDTO editedShortcut;

    private final ObjectMapper objectMapper;

    private Label description;
    private GridPane gridPane;
    private Button saveButton;

    // consumer called when a shortcut should changed
    private Consumer<ShortcutDTO> onShortcutChanged;

    /**
     * constructor
     * @param objectMapper mapper to show the content of the shortcut script
     */
    public EditShortcutPanel(ObjectMapper objectMapper) {
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
        spacer.getStyleClass().add("detailsButtonSpacer");

        this.saveButton = new Button(tr("Save"));

        vBox.getChildren().addAll(this.description, this.gridPane, spacer, this.saveButton);

        this.setCenter(vBox);
    }

    /**
    * sets the shortcut which can be edited in this view
    * @param shortcutDTO
    */
    public void setShortcutDTO(ShortcutDTO shortcutDTO) {
        this.editedShortcut = shortcutDTO;

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

                final TextArea valueLabel = new TextArea(entry.getValue().toString());
                valueLabel.setWrapText(true);
                valueLabel.setPrefRowCount(entry.getValue().toString().length() / 25);
                valueLabel.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    // update shortcut if TextArea looses focus (doesn't save yet)
                    if (!newValue) {
                        shortcutProperties.replace(entry.getKey(), valueLabel.getText());
                        try {
                            String json = new ObjectMapper().writeValueAsString(shortcutProperties);
                            this.editedShortcut = new ShortcutDTO.Builder(shortcutDTO).withScript(json).build();
                        } catch (JsonProcessingException e) {
                            LOGGER.error("Creating new shortcut String failed.", e);
                        }
                    }
                });
                this.gridPane.add(valueLabel, 1, i);

                i++;
            }

        } catch (IOException e) {
            LOGGER.warn("Could not parse shortcut script JSON", e);
        }

        this.saveButton.setOnMouseClicked(event -> {
            this.onShortcutChanged.accept(this.editedShortcut);
        });
    }

    private String unCamelize(String s) {
        return StringUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(s), ' '));
    }

    /**
     * updates the consumer that is called when the shortcut has been changed
     * @param onShortcutChanged The new consumer to be called
     */
    public void setOnShortcutChanged(Consumer<ShortcutDTO> onShortcutChanged) {
        this.onShortcutChanged = onShortcutChanged;
    }

}
