package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.WinePrefixContainerController;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.TextWithStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 27.05.17.
 */
public class WinePrefixContainerToolsTab extends Tab {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";

    private final WinePrefixContainerDTO container;
    private final WinePrefixContainerController winePrefixContainerController;

    private final List<Node> lockableElements = new ArrayList<>();

    public WinePrefixContainerToolsTab(WinePrefixContainerDTO container,
            WinePrefixContainerController winePrefixContainerController) {
        super(tr("Tools"));

        this.container = container;
        this.winePrefixContainerController = winePrefixContainerController;

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

        Button openTerminal = new Button(tr("Open a terminal"));
        openTerminal.getStyleClass().addAll("toolButton", "openTerminal");
        openTerminal.setOnMouseClicked(e -> {
            this.lockAll();
            winePrefixContainerController.openTerminalInPrefix(container);
            this.unlockAll();
        });

        this.lockableElements.add(openTerminal);

        toolsContentPane.getChildren().add(openTerminal);

        Button runExecutable = new Button(tr("Run executable"));
        runExecutable.getStyleClass().addAll("toolButton", "runExecutable");
        runExecutable.setOnMouseClicked(event -> {
            this.lockAll();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(tr("Choose executable"));
            File file = fileChooser.showOpenDialog(this.getContent().getScene().getWindow());
            if (file != null) {
                winePrefixContainerController.runInContainer(container, file.getAbsolutePath(), this::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
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
