package org.phoenicis.javafx.components.container.skin;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.container.control.ContainerEngineToolsPanel;
import org.phoenicis.javafx.dialogs.ErrorDialog;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link ContainerEngineToolsPanel} component
 */
public class ContainerEngineToolsPanelSkin extends SkinBase<ContainerEngineToolsPanel, ContainerEngineToolsPanelSkin> {
    /**
     * A list containing all available tool buttons
     */
    private final ObservableList<Button> toolButtons;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ContainerEngineToolsPanelSkin(ContainerEngineToolsPanel control) {
        super(control);

        // map the tool scripts to a tool button
        this.toolButtons = new MappedList<>(control.getEngineToolScripts(), tool -> {
            final Button toolButton = new Button(tool.getScriptName());
            toolButton.getStyleClass().addAll("toolButton");
            toolButton.setStyle("-fx-background-image: url('" + tool.getIcon() + "');");

            toolButton.disableProperty().bind(getControl().lockToolsProperty());

            toolButton.setOnMouseClicked(event -> {
                getControl().setLockTools(true);

                final ContainerDTO container = getControl().getContainer();

                // TODO: find a better way to get the engine ID
                getControl().getEngineToolsManager().runTool(container.getEngine().toLowerCase(), container.getName(),
                        tool.getId(), () -> getControl().setLockTools(false), e -> Platform.runLater(() -> {
                            final ErrorDialog errorDialog = ErrorDialog.builder()
                                    .withMessage(tr("Error"))
                                    .withException(e)
                                    .build();

                            errorDialog.showAndWait();
                        }));
            });

            return toolButton;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final Text title = new Text(tr("Engine tools"));
        title.getStyleClass().add("title");

        final FlowPane toolButtonContainer = createToolButtonContainer();

        final VBox toolsPane = new VBox(title, toolButtonContainer);
        toolsPane.getStyleClass().addAll("containerConfigurationPane");

        final ScrollPane scrollPane = new ScrollPane(toolsPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);

        getChildren().addAll(scrollPane);
    }

    /**
     * Creates a container for all the tool buttons
     *
     * @return A container containing all the tool buttons
     */
    private FlowPane createToolButtonContainer() {
        final FlowPane toolsContentPane = new FlowPane();
        toolsContentPane.getStyleClass().add("grid");

        Bindings.bindContent(toolsContentPane.getChildren(), toolButtons);

        return toolsContentPane;
    }
}
