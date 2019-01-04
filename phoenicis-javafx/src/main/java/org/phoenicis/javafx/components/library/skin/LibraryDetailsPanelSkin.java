package org.phoenicis.javafx.components.library.skin;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.javafx.components.common.skin.DetailsPanelBaseSkin;
import org.phoenicis.javafx.components.library.control.LibraryDetailsPanel;
import org.phoenicis.javafx.controller.library.LibraryController;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.library.dto.ShortcutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class LibraryDetailsPanelSkin extends DetailsPanelBaseSkin<LibraryDetailsPanel, LibraryDetailsPanelSkin> {
    private final Logger LOGGER = LoggerFactory.getLogger(LibraryController.class);

    private final ObservableMap<String, Object> properties;

    private final StringProperty description;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public LibraryDetailsPanelSkin(LibraryDetailsPanel control) {
        super(control);

        this.properties = FXCollections.observableHashMap();
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
        // initialise the content of the details panel correct
        updateShortcut();
    }

    @Override
    protected Node createContent() {
        final Label descriptionLabel = new Label();
        descriptionLabel.textProperty().bind(description);
        descriptionLabel.setWrapText(true);

        final GridPane propertiesGrid = createPropertiesGrid();

        final Region spacer = new Region();
        spacer.getStyleClass().add("detailsButtonSpacer");

        final GridPane controlButtons = createControlButtons();

        return new VBox(descriptionLabel, propertiesGrid, spacer, controlButtons);
    }

    private GridPane createPropertiesGrid() {
        final GridPane propertiesGrid = new GridPane();
        propertiesGrid.getStyleClass().add("grid");

        ColumnConstraints titleColumn = new ColumnConstraintsWithPercentage(30);
        ColumnConstraints valueColumn = new ColumnConstraintsWithPercentage(70);

        propertiesGrid.getColumnConstraints().addAll(titleColumn, valueColumn);

        // ensure that changes to the properties map result in updates to the GridPane
        properties.addListener((Observable invalidation) -> updateProperties(propertiesGrid));
        updateProperties(propertiesGrid);

        return propertiesGrid;
    }

    private GridPane createControlButtons() {
        final GridPane controlButtons = new GridPane();
        controlButtons.getStyleClass().add("shortcut-control-button-group");

        ColumnConstraints runColumn = new ColumnConstraintsWithPercentage(33.3);
        ColumnConstraints stopColumn = new ColumnConstraintsWithPercentage(33.3);
        ColumnConstraints uninstallColumn = new ColumnConstraintsWithPercentage(33.3);

        controlButtons.getColumnConstraints().addAll(runColumn, stopColumn, uninstallColumn);

        final Button runButton = new Button(tr("Run"));
        runButton.getStyleClass().addAll("shortcutButton", "runButton");
        runButton.setOnMouseClicked(event -> Optional.ofNullable(getControl().getOnShortcutRun())
                .ifPresent(onShortcutRun -> onShortcutRun.accept(getControl().getShortcut())));
        GridPane.setHalignment(runButton, HPos.CENTER);

        final Button stopButton = new Button(tr("Close"));
        stopButton.getStyleClass().addAll("shortcutButton", "stopButton");
        stopButton.setOnMouseClicked(event -> Optional.ofNullable(getControl().getOnShortcutStop())
                .ifPresent(onShortcutStop -> onShortcutStop.accept(getControl().getShortcut())));
        GridPane.setHalignment(stopButton, HPos.CENTER);

        final Button uninstallButton = new Button(tr("Uninstall"));
        uninstallButton.getStyleClass().addAll("shortcutButton", "uninstallButton");
        uninstallButton.setOnMouseClicked(event -> Optional.ofNullable(getControl().getOnShortcutUninstall())
                .ifPresent(onShortcutUninstall -> onShortcutUninstall.accept(getControl().getShortcut())));
        GridPane.setHalignment(uninstallButton, HPos.CENTER);

        controlButtons.addRow(0, runButton, stopButton, uninstallButton);

        return controlButtons;
    }

    private void updateProperties(final GridPane propertiesGrid) {
        propertiesGrid.getChildren().clear();

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            final int row = propertiesGrid.getRowCount();

            final Label keyLabel = new Label(tr(decamelize(entry.getKey())) + ":");
            keyLabel.getStyleClass().add("captionTitle");
            GridPane.setValignment(keyLabel, VPos.TOP);

            final Label valueLabel = new Label(entry.getValue().toString());
            valueLabel.setWrapText(true);

            propertiesGrid.addRow(row, keyLabel, valueLabel);
        }
    }

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

                properties.clear();
                properties.putAll(shortcutProperties);
            } catch (IOException e) {
                LOGGER.error("An error occurred during a Shortcut update", e);
            }
        }
    }

    private String decamelize(String s) {
        return StringUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(s), ' '));
    }
}
