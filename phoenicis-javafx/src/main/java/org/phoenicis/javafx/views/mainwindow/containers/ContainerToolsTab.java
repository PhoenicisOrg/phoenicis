package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.phoenicis.containers.ContainerEngineController;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.TextWithStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 27.05.17.
 */
public class ContainerToolsTab extends Tab {
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";

    private final ContainerDTO container;
    private final ContainerEngineController containerEngineController;

    private final List<Node> lockableElements = new ArrayList<>();

    public ContainerToolsTab(ContainerDTO container,
            ContainerEngineController containerEngineController) {
        super(tr("Tools"));

        this.container = container;
        this.containerEngineController = containerEngineController;

        this.setClosable(false);

        this.populate();
    }

    private void populate() {
        final VBox toolsPane = new VBox();
        final Text title = new TextWithStyle(tr("Tools"), TITLE_CSS_CLASS);

        toolsPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        toolsPane.getChildren().add(title);

        final TilePane toolsContentPane = new TilePane();
        toolsContentPane.setPrefColumns(3);
        toolsContentPane.getStyleClass().add("grid");

        Button runExecutable = new Button(tr("Run executable"));
        runExecutable.getStyleClass().addAll("toolButton", "runExecutable");
        runExecutable.setOnMouseClicked(event -> {
            this.lockAll();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(tr("Choose executable"));

            // open in container directory if it exists
            File containerDir = new File(this.container.getPath());
            if (containerDir.canRead()) {
                fileChooser.setInitialDirectory(containerDir);
            }

            File file = fileChooser.showOpenDialog(this.getContent().getScene().getWindow());
            if (file != null) {
                containerEngineController.runInContainer(container, file.getAbsolutePath(), this::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
            } else {
                // unlock if file chooser is closed
                this.unlockAll();
            }
        });

        this.lockableElements.add(runExecutable);

        toolsContentPane.getChildren().add(runExecutable);

        toolsPane.getChildren().addAll(toolsContentPane);

        this.setContent(toolsPane);
    }

    public void unlockAll() {
        for (Node element : lockableElements) {
            element.setDisable(false);
        }
    }

    private void lockAll() {
        for (Node element : lockableElements) {
            element.setDisable(true);
        }
    }
}
