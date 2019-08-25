package org.phoenicis.javafx.components.engine.skin;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.collections.ObservableMap;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.engines.Engine;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.engine.control.EngineInformationPanel;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.javafx.utils.CollectionBindings;
import org.phoenicis.javafx.utils.StringBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation class used inside the {@link EngineInformationPanel}
 */
public class EngineInformationPanelSkin extends SkinBase<EngineInformationPanel, EngineInformationPanelSkin> {
    private final Logger LOGGER = LoggerFactory.getLogger(EngineInformationPanelSkin.class);

    /**
     * The engine version of the shown engine
     */
    private final StringBinding engineVersionName;

    /**
     * The user data of the shown engine
     */
    private final ObservableMap<String, String> engineUserData;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public EngineInformationPanelSkin(EngineInformationPanel control) {
        super(control);

        this.engineVersionName = StringBindings.map(getControl().engineDTOProperty(), EngineDTO::getVersion);
        this.engineUserData = CollectionBindings.mapToMap(getControl().engineDTOProperty(), EngineDTO::getUserData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final GridPane informationContentPane = new GridPane();
        informationContentPane.getStyleClass().add("grid");

        engineUserData.addListener((Observable invalidation) -> updateUserData(informationContentPane));
        updateUserData(informationContentPane);

        final HBox buttonBox = createEngineButtons();

        final Region informationContentSpacer = new Region();
        informationContentSpacer.getStyleClass().add("engineSpacer");

        final Region buttonBoxSpacer = new Region();
        buttonBoxSpacer.getStyleClass().add("engineSpacer");

        final VBox container = new VBox(informationContentPane, informationContentSpacer, buttonBox, buttonBoxSpacer);

        getChildren().add(container);
    }

    /**
     * Creates a new {@link HBox} containing the interaction buttons for selected engine.
     * The interaction buttons consist of:
     * <ul>
     * <li>An install button</li>
     * <li>An uninstall button</li>
     * </ul>
     *
     * @return A new {@link HBox} containing the interaction buttons for the selected engine
     */
    private HBox createEngineButtons() {
        // binding to check whether the currently selected engine is installed
        final BooleanBinding engineInstalledProperty = Bindings.createBooleanBinding(() -> {
            final Engine engine = getControl().getEngine();
            final EngineDTO engineDTO = getControl().getEngineDTO();

            if (engine == null || engineDTO == null) {
                return true;
            }

            return engine.isInstalled(engineDTO.getSubCategory(), engineDTO.getVersion());
        }, getControl().engineProperty(), getControl().engineDTOProperty());

        // the engine install button
        final Button installButton = new Button(tr("Install"));
        installButton.disableProperty().bind(engineInstalledProperty);
        installButton.setOnMouseClicked(evt -> {
            try {
                Optional.ofNullable(getControl().getOnEngineInstall())
                        .ifPresent(onEngineInstall -> onEngineInstall.accept(getControl().getEngineDTO()));
            } catch (IllegalArgumentException e) {
                LOGGER.error("Failed to get engine", e);

                final ErrorDialog errorDialog = ErrorDialog.builder()
                        .withMessage(tr("An error occurred while installing the engine"))
                        .withOwner(getControl().getScene().getWindow())
                        .withException(e)
                        .build();

                errorDialog.showAndWait();
            }
        });

        // the engine delete button
        final Button deleteButton = new Button(tr("Delete"));
        deleteButton.disableProperty().bind(Bindings.not(engineInstalledProperty));
        deleteButton.setOnMouseClicked(evt -> {
            try {
                Optional.ofNullable(getControl().getOnEngineDelete())
                        .ifPresent(onEngineDelete -> onEngineDelete.accept(getControl().getEngineDTO()));
            } catch (IllegalArgumentException e) {
                LOGGER.error("Failed to get engine", e);

                final ErrorDialog errorDialog = ErrorDialog.builder()
                        .withMessage(tr("An error occurred while deleting the engine"))
                        .withOwner(getControl().getScene().getWindow())
                        .withException(e)
                        .build();

                errorDialog.showAndWait();
            }
        });

        final HBox buttonBox = new HBox(installButton, deleteButton);
        buttonBox.getStyleClass().add("engineButtons");

        return buttonBox;
    }

    /**
     * Updates the user data of the selected engine in the given {@link GridPane userDataGrid}
     *
     * @param userDataGrid The user data grid
     */
    private void updateUserData(final GridPane userDataGrid) {
        userDataGrid.getChildren().clear();

        final Text versionLabel = new Text(tr("Version:"));
        versionLabel.getStyleClass().add("captionTitle");

        final Label name = new Label();
        name.textProperty().bind(engineVersionName);
        name.setWrapText(true);

        userDataGrid.addRow(0, versionLabel, name);

        for (Map.Entry<String, String> userData : engineUserData.entrySet()) {
            final int row = userDataGrid.getRowCount();

            final Text userDataLabel = new Text(tr(userData.getKey()));
            userDataLabel.getStyleClass().add("captionTitle");

            final Label path = new Label(userData.getValue());
            path.setWrapText(true);

            userDataGrid.addRow(row, userDataLabel, path);
        }
    }
}
