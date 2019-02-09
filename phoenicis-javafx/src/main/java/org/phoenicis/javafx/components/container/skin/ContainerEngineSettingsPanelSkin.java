package org.phoenicis.javafx.components.container.skin;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.EngineSetting;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.container.control.ContainerEngineSettingsPanel;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link ContainerEngineSettingsPanel} component
 */
public class ContainerEngineSettingsPanelSkin
        extends SkinBase<ContainerEngineSettingsPanel, ContainerEngineSettingsPanelSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ContainerEngineSettingsPanelSkin(ContainerEngineSettingsPanel control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final Text title = new Text(tr("Engine Settings"));
        title.getStyleClass().add("title");

        final GridPane engineSettingsGrid = new GridPane();
        engineSettingsGrid.getStyleClass().add("engine-settings-grid");
        // ensure that the shown engine settings are always up to date
        getControl().containerProperty()
                .addListener((Observable invalidation) -> updateEngineSettingsGrid(engineSettingsGrid));
        getControl().getEngineSettings()
                .addListener((Observable invalidation) -> updateEngineSettingsGrid(engineSettingsGrid));
        // ensure that the shown engine settings are correctly initialized
        updateEngineSettingsGrid(engineSettingsGrid);

        final ScrollPane engineSettingsScrollPane = new ScrollPane(engineSettingsGrid);
        engineSettingsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        engineSettingsScrollPane.setFitToWidth(true);

        VBox.setVgrow(engineSettingsScrollPane, Priority.ALWAYS);

        final VBox container = new VBox(title, engineSettingsScrollPane);
        container.getStyleClass().addAll("container-details-panel", "container-engine-settings-panel");

        getChildren().setAll(container);
    }

    /**
     * Updates the engine settings in the given {@link GridPane engineSettingsGrid}
     *
     * @param engineSettingsGrid The GridPane containing the shown engine settings
     */
    private void updateEngineSettingsGrid(final GridPane engineSettingsGrid) {
        engineSettingsGrid.getChildren().clear();

        final ContainerDTO container = getControl().getContainer();

        for (EngineSetting engineSetting : getControl().getEngineSettings()) {
            final int row = engineSettingsGrid.getRowCount();

            final Text engineSettingDescription = new Text(engineSetting.getText());
            engineSettingDescription.getStyleClass().add("captionTitle");

            final ObservableList<String> items = FXCollections.observableArrayList(engineSetting.getOptions());

            final ComboBox<String> engineSettingComboBox = new ComboBox<>(items);
            engineSettingComboBox.getStyleClass().add("engine-setting-combo-box");
            engineSettingComboBox.disableProperty().bind(getControl().lockEngineSettingsProperty());
            engineSettingComboBox.setValue(engineSetting.getCurrentOption(container.getName()));
            engineSettingComboBox.valueProperty().addListener((Observable invalidation) -> Platform.runLater(() -> {
                getControl().setLockEngineSettings(true);

                engineSetting.setOption(container.getName(), items.indexOf(engineSettingComboBox.getValue()));

                getControl().setLockEngineSettings(false);
            }));

            engineSettingsGrid.addRow(row, engineSettingDescription, engineSettingComboBox);
        }
    }
}
