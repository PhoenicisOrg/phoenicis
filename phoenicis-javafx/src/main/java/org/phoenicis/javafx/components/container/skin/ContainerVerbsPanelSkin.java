package org.phoenicis.javafx.components.container.skin;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.container.control.ContainerVerbsPanel;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.repository.dto.ScriptDTO;

import java.util.List;
import java.util.stream.Collectors;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link ContainerVerbsPanel} component
 */
public class ContainerVerbsPanelSkin extends SkinBase<ContainerVerbsPanel, ContainerVerbsPanelSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ContainerVerbsPanelSkin(ContainerVerbsPanel control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final Text title = new Text(tr("Verbs"));
        title.getStyleClass().add("title");

        final GridPane verbs = new GridPane();
        verbs.getStyleClass().add("verb-grid");
        // ensure that the shown verbs are always up to date
        getControl().getVerbScripts().addListener((Observable invalidation) -> updateVerbs(verbs));
        // ensure that the shown verbs are correctly initialized
        updateVerbs(verbs);

        final ScrollPane verbScrollPanel = new ScrollPane(verbs);
        verbScrollPanel.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        verbScrollPanel.setFitToWidth(true);

        VBox.setVgrow(verbScrollPanel, Priority.ALWAYS);

        final HBox verbManagementButtons = createVerbManagementButtons(verbs);
        verbManagementButtons.getStyleClass().add("verb-management-button-container");

        final VBox container = new VBox(title, verbScrollPanel, verbManagementButtons);
        container.getStyleClass().addAll("container-details-panel", "container-verbs-panel");

        getChildren().setAll(container);
    }

    /**
     * Creates a container with the buttons for the verb selection management. These buttons consist of:
     * - a button to install all selected verbs
     * - a button to clear/reset the selection
     *
     * @param verbs The {@link GridPane} containing the visual verb installation components
     * @return A container with the buttons for the verb selection management
     */
    private HBox createVerbManagementButtons(final GridPane verbs) {
        final Button installButton = new Button(tr("Install selected"));
        installButton.disableProperty().bind(getControl().lockVerbsProperty());

        installButton.setOnAction(event -> {
            getControl().setLockVerbs(true);

            final ContainerDTO container = getControl().getContainer();

            // find the ids of all selected verbs
            final List<String> verbIds = verbs.getChildren().stream()
                    .filter(element -> element instanceof CheckBox && ((CheckBox) element).isSelected())
                    .map(GridPane::getRowIndex)
                    .map(getControl().getVerbScripts()::get)
                    .map(ScriptDTO::getId)
                    .collect(Collectors.toList());

            // install the selected verbs
            getControl().getVerbsManager().installVerbs(container.getEngine().toLowerCase(), container.getName(),
                    verbIds, () -> getControl().setLockVerbs(false), e -> Platform.runLater(() -> {
                        final ErrorDialog errorDialog = ErrorDialog.builder()
                                .withMessage(tr("Error installing Verbs"))
                                .withException(e)
                                .build();

                        errorDialog.showAndWait();
                    }));
        });

        final Button clearButton = new Button(tr("Clear selection"));
        clearButton.disableProperty().bind(getControl().lockVerbsProperty());

        clearButton.setOnAction(event -> verbs.getChildren().stream()
                // filter all checkboxes
                .filter(element -> element instanceof CheckBox)
                .map(element -> (CheckBox) element)
                // deselect the checkboxes
                .forEach(verbCheckBox -> verbCheckBox.setSelected(false)));

        return new HBox(installButton, clearButton);
    }

    /**
     * Updates the verbs in the given {@link GridPane verbs}
     *
     * @param verbs The GridPane containing the visual verb installation components
     */
    private void updateVerbs(final GridPane verbs) {
        verbs.getChildren().clear();

        for (ScriptDTO verb : getControl().getVerbScripts()) {
            final int row = verbs.getRowCount();

            final CheckBox verbCheck = new CheckBox();

            final Label verbName = new Label(verb.getScriptName());

            GridPane.setHgrow(verbName, Priority.ALWAYS);

            final Button verbInstall = new Button(tr("Install"));
            verbInstall.disableProperty().bind(getControl().lockVerbsProperty());

            verbInstall.setOnAction(event -> {
                getControl().setLockVerbs(true);

                final ContainerDTO container = getControl().getContainer();

                // TODO: find a better way to get the engine ID
                getControl().getVerbsManager().installVerb(container.getEngine().toLowerCase(), container.getName(),
                        verb.getId(), () -> getControl().setLockVerbs(false), e -> Platform.runLater(() -> {
                            final ErrorDialog errorDialog = ErrorDialog.builder()
                                    .withMessage(tr("Error installing Verb {0}", verb.getScriptName()))
                                    .withException(e)
                                    .build();

                            errorDialog.showAndWait();
                        }));
            });

            verbs.addRow(row, verbCheck, verbName, verbInstall);
        }
    }
}
