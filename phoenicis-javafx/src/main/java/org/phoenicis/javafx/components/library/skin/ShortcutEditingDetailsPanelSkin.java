package org.phoenicis.javafx.components.library.skin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.javafx.components.common.skin.DetailsPanelBaseSkin;
import org.phoenicis.javafx.components.library.control.ShortcutEditingDetailsPanel;
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
 * A skin implementation for the {@link ShortcutEditingDetailsPanel} component
 */
public class ShortcutEditingDetailsPanelSkin
        extends DetailsPanelBaseSkin<ShortcutEditingDetailsPanel, ShortcutEditingDetailsPanelSkin> {
    private final Logger LOGGER = LoggerFactory.getLogger(ShortcutEditingDetailsPanelSkin.class);

    /**
     * The properties of the currently selected {@link ShortcutDTO}
     */
    private final ObservableMap<String, Object> shortcutProperties;

    /**
     * The description of the currently selected {@link ShortcutDTO}
     */
    private final StringProperty description;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ShortcutEditingDetailsPanelSkin(ShortcutEditingDetailsPanel control) {
        super(control);

        this.shortcutProperties = FXCollections.observableHashMap();
        this.description = new SimpleStringProperty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        super.initialise();

        // ensure that the content of the details panel changes when the to be shown shortcut changes
        getControl().shortcutProperty().addListener((Observable invalidation) -> updateShortcut());
        // initialize the content of the details panel correctly
        updateShortcut();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Node createContent() {
        final Label descriptionLabel = new Label();
        descriptionLabel.textProperty().bind(description);
        descriptionLabel.setWrapText(true);

        final GridPane propertiesGrid = createPropertiesGrid();

        final Region spacer = new Region();
        spacer.getStyleClass().add("detailsButtonSpacer");

        final Button saveButton = new Button(tr("Save"));
        saveButton.setOnMouseClicked(event -> Optional.ofNullable(getControl().getOnShortcutChanged()).ifPresent(
                onShortcutChanged -> onShortcutChanged.accept(getControl().getShortcut())));

        return new VBox(descriptionLabel, propertiesGrid, spacer, saveButton);
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

    /**
     * Updates the shortcut of this {@link LibraryDetailsPanelSkin} instance
     */
    private void updateShortcut() {
        final ShortcutDTO shortcut = getControl().getShortcut();

        if (shortcut != null) {
            title.setValue(shortcut.getInfo().getName());
            description.setValue(shortcut.getInfo().getDescription());

            try {
                final Map<String, Object> shortcutProperties = getControl().getObjectMapper()
                        .readValue(shortcut.getScript(), new TypeReference<Map<String, Object>>() {
                            // nothing
                        });

                this.shortcutProperties.clear();
                this.shortcutProperties.putAll(shortcutProperties);
            } catch (IOException e) {
                LOGGER.error("An error occurred during a shortcut update", e);
            }
        }
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

            final File newMiniature = chooser.showOpenDialog(getControl().getScene().getWindow());
            miniaturePathField.setText(newMiniature.toString());

            getControl().setShortcut(new ShortcutDTO.Builder(getControl().getShortcut())
                    .withMiniature(newMiniature.toURI()).build());
        });

        final HBox miniatureContainer = new HBox(miniaturePathField, openBrowser);

        propertiesGrid.addRow(0, miniatureLabel, miniatureContainer);

        for (Map.Entry<String, Object> entry : shortcutProperties.entrySet()) {
            final int row = propertiesGrid.getRowCount();

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
