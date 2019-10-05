package org.phoenicis.javafx.components.library.skin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.Observable;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.javafx.components.common.control.KeyAttributeList;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.library.control.ShortcutEditingPanel;
import org.phoenicis.javafx.utils.CollectionBindings;
import org.phoenicis.javafx.utils.StringBindings;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.library.dto.ShortcutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link ShortcutEditingPanel} component
 */
public class ShortcutEditingPanelSkin extends SkinBase<ShortcutEditingPanel, ShortcutEditingPanelSkin> {
    private final Logger LOGGER = LoggerFactory.getLogger(ShortcutEditingPanelSkin.class);

    /**
     * The description of the currently selected {@link ShortcutDTO}
     */
    private final StringBinding description;

    /**
     * The properties of the currently selected {@link ShortcutDTO}
     */
    private final ObservableMap<String, Object> shortcutProperties;

    private final ObservableMap<String, String> environmentAttributes;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ShortcutEditingPanelSkin(ShortcutEditingPanel control) {
        super(control);

        this.description = StringBindings.map(getControl().shortcutProperty(),
                shortcut -> shortcut.getInfo().getDescription());

        this.shortcutProperties = CollectionBindings.mapToMap(getControl().shortcutProperty(), shortcut -> {
            try {
                return getControl().getObjectMapper()
                        .readValue(shortcut.getScript(), new TypeReference<Map<String, Object>>() {
                            // nothing
                        });
            } catch (IOException e) {
                LOGGER.error("An error occurred during a shortcut update", e);

                return Map.of();
            }
        });

        this.environmentAttributes = FXCollections.observableHashMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final Label descriptionLabel = new Label();
        descriptionLabel.textProperty().bind(description);
        descriptionLabel.setWrapText(true);

        final GridPane propertiesGrid = createPropertiesGrid();

        final Label environmentLabel = new Label(tr("Environment"));
        environmentLabel.getStyleClass().add("sectionTitle");
        environmentLabel.setWrapText(true);

        final KeyAttributeList environmentAttributeList = createKeyAttributeList();

        final Region spacer = new Region();
        spacer.getStyleClass().add("detailsButtonSpacer");

        final Button saveButton = new Button(tr("Save"));
        saveButton.setOnMouseClicked(event -> Optional.ofNullable(getControl().getOnShortcutChanged()).ifPresent(
                onShortcutChanged -> onShortcutChanged.accept(getControl().getShortcut())));

        final VBox container = new VBox(descriptionLabel, propertiesGrid, environmentLabel, environmentAttributeList,
                spacer, saveButton);

        getChildren().setAll(container);
    }

    /**
     * Creates a new {@link GridPane} containing the properties of the selected shortcut
     *
     * @return a new {@link GridPane} containing the properties of the selected shortcut
     */
    private GridPane createPropertiesGrid() {
        final GridPane propertiesGrid = new GridPane();
        propertiesGrid.getStyleClass().add("grid");

        ColumnConstraints titleColumn = new ColumnConstraintsWithPercentage(30);
        ColumnConstraints valueColumn = new ColumnConstraintsWithPercentage(70);

        propertiesGrid.getColumnConstraints().addAll(titleColumn, valueColumn);

        // ensure that changes to the shortcutProperties map result in updates to the GridPane
        shortcutProperties.addListener((Observable invalidation) -> updateProperties(propertiesGrid));
        // initialize the properties grid correctly
        updateProperties(propertiesGrid);

        return propertiesGrid;
    }

    private KeyAttributeList createKeyAttributeList() {
        final KeyAttributeList keyAttributeList = new KeyAttributeList();

        keyAttributeList.setEditable(true);

        keyAttributeList.setOnChange(environment -> {
            // update shortcut if a part of the environment list has changed
            shortcutProperties.replace("environment", environment);

            try {
                final ShortcutDTO shortcut = getControl().getShortcut();
                final String json = new ObjectMapper().writeValueAsString(shortcutProperties);

                getControl().setShortcut(new ShortcutDTO.Builder(shortcut)
                        .withScript(json).build());
            } catch (JsonProcessingException e) {
                LOGGER.error("Creating new shortcut String failed.", e);
            }
        });

        this.environmentAttributes.addListener(
                (Observable invalidated) -> keyAttributeList.setAttributeMap(this.environmentAttributes));

        keyAttributeList.setAttributeMap(this.environmentAttributes);

        return keyAttributeList;
    }

    /**
     * Updates the shortcutProperties of the shortcut in the given {@link GridPane propertiesGrid}
     *
     * @param propertiesGrid The shortcutProperties grid
     */
    private void updateProperties(final GridPane propertiesGrid) {
        propertiesGrid.getChildren().clear();

        // add miniature
        final Label miniatureLabel = new Label(tr("Miniature:"));
        miniatureLabel.getStyleClass().add("captionTitle");

        GridPane.setValignment(miniatureLabel, VPos.TOP);

        final TextField miniaturePathField = new TextField(Optional.ofNullable(getControl().getShortcut())
                .map(ShortcutDTO::getMiniature).map(URI::getPath).orElse(""));

        HBox.setHgrow(miniaturePathField, Priority.ALWAYS);

        final Button openBrowser = new Button(tr("Browse..."));
        openBrowser.setOnAction(event -> {
            final URI miniatureURI = Optional.ofNullable(getControl().getShortcut()).map(ShortcutDTO::getMiniature)
                    .orElseThrow(() -> new IllegalStateException("The shortcut is null"));

            final FileChooser chooser = new FileChooser();
            chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(tr("Images"), "*.miniature, *.png"));

            final File defaultFile = new File(miniatureURI);
            chooser.setInitialDirectory(defaultFile.getParentFile());

            Optional.ofNullable(chooser.showOpenDialog(getControl().getScene().getWindow())).ifPresent(newMiniature -> {
                miniaturePathField.setText(newMiniature.toString());

                getControl().setShortcut(new ShortcutDTO.Builder(getControl().getShortcut())
                        .withMiniature(newMiniature.toURI()).build());
            });
        });

        final HBox miniatureContainer = new HBox(miniaturePathField, openBrowser);

        propertiesGrid.addRow(0, miniatureLabel, miniatureContainer);

        for (Map.Entry<String, Object> entry : shortcutProperties.entrySet()) {
            final int row = propertiesGrid.getRowCount();

            if (!"environment".equals(entry.getKey())) {
                final Label keyLabel = new Label(tr(decamelize(entry.getKey())) + ":");
                keyLabel.getStyleClass().add("captionTitle");
                GridPane.setValignment(keyLabel, VPos.TOP);

                final TextArea valueLabel = new TextArea(entry.getValue().toString());
                valueLabel.setWrapText(true);
                valueLabel.setPrefRowCount(entry.getValue().toString().length() / 25);

                valueLabel.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    // update shortcut if TextArea looses focus (doesn't save yet)
                    if (!newValue) {
                        shortcutProperties.replace(entry.getKey(), valueLabel.getText());

                        try {
                            final ShortcutDTO shortcut = getControl().getShortcut();
                            final String json = new ObjectMapper().writeValueAsString(shortcutProperties);

                            getControl().setShortcut(new ShortcutDTO.Builder(shortcut)
                                    .withScript(json).build());
                        } catch (JsonProcessingException e) {
                            LOGGER.error("Creating new shortcut String failed.", e);
                        }
                    }
                });

                propertiesGrid.addRow(row, keyLabel, valueLabel);
            }
        }

        // set the environment
        this.environmentAttributes.clear();

        if (shortcutProperties.containsKey("environment")) {
            final Map<String, String> environment = (Map<String, String>) shortcutProperties.get("environment");

            this.environmentAttributes.putAll(environment);
        }
    }

    /**
     * Decamelize the given string
     *
     * @param string The string to decamelize
     * @return The decamelized string
     */
    private String decamelize(String string) {
        return StringUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(string), ' '));
    }
}
