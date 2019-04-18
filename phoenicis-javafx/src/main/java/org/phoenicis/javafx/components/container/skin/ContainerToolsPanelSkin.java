package org.phoenicis.javafx.components.container.skin;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.container.control.ContainerToolsPanel;
import org.phoenicis.javafx.dialogs.ErrorDialog;

import java.io.File;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link ContainerToolsPanel} component
 */
public class ContainerToolsPanelSkin extends SkinBase<ContainerToolsPanel, ContainerToolsPanelSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ContainerToolsPanelSkin(ContainerToolsPanel control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final Text title = new Text(tr("Tools"));
        title.getStyleClass().add("title");

        final TilePane toolsContainer = createToolsContainer();
        toolsContainer.getStyleClass().add("tool-grid");

        final VBox toolsPane = new VBox(title, toolsContainer);
        toolsPane.getStyleClass().addAll("container-details-panel", "container-tools-panel");

        getChildren().setAll(toolsPane);
    }

    /**
     * Creates the container for the tool buttons
     *
     * @return The container with the tool buttons
     */
    private TilePane createToolsContainer() {
        final TilePane toolsContainer = new TilePane();

        final Button runExecutable = new Button(tr("Run executable"));
        runExecutable.getStyleClass().addAll("toolButton", "runExecutable");
        runExecutable.setOnMouseClicked(event -> {
            getControl().setLockTools(true);

            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(tr("Choose executable..."));

            // open in container directory if it exists
            final File containerDir = new File(getControl().getContainer().getPath());
            if (containerDir.canRead()) {
                fileChooser.setInitialDirectory(containerDir);
            }

            final File file = fileChooser.showOpenDialog(getControl().getScene().getWindow());
            if (file != null) {
                getControl().getContainerEngineController().runInContainer(getControl().getContainer(),
                        file.getAbsolutePath(), () -> getControl().setLockTools(false), e -> Platform.runLater(() -> {
                            final ErrorDialog errorDialog = ErrorDialog.builder()
                                    .withMessage(tr("Error"))
                                    .withOwner(getControl().getScene().getWindow())
                                    .withException(e)
                                    .build();

                            errorDialog.showAndWait();
                        }));
            } else {
                // unlock if file chooser is closed
                getControl().setLockTools(false);
            }
        });

        toolsContainer.getChildren().add(runExecutable);

        return toolsContainer;
    }
}
