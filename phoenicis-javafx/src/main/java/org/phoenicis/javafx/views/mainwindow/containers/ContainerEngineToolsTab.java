package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.TextWithStyle;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.ScriptDTO;

import java.util.ArrayList;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 27.05.17.
 */
public class ContainerEngineToolsTab extends Tab {
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";

    private final ContainerDTO container;
    private EngineToolsManager engineToolsManager;

    private final List<Node> lockableElements = new ArrayList<>();

    public ContainerEngineToolsTab(ContainerDTO container, EngineToolsManager engineToolsManager,
            ApplicationDTO engineTools) {
        super(tr("Engine tools"));

        this.container = container;
        this.engineToolsManager = engineToolsManager;

        this.setClosable(false);

        this.populate(engineTools);
    }

    private void populate(ApplicationDTO engineTools) {
        final VBox toolsPane = new VBox();
        final Text title = new TextWithStyle(tr("Engine tools"), TITLE_CSS_CLASS);

        toolsPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        toolsPane.getChildren().add(title);

        final TilePane toolsContentPane = new TilePane();
        toolsContentPane.setPrefColumns(3);
        toolsContentPane.getStyleClass().add("grid");

        for (ScriptDTO tool : engineTools.getScripts()) {
            Button toolButton = new Button(tool.getScriptName());
            toolButton.getStyleClass().addAll("toolButton");
            toolButton.setStyle("-fx-background-image: url('" + tool.getIcon() + "');");
            toolButton.setOnMouseClicked(event -> {
                this.lockAll();
                // TODO: find a better way to get the engine ID
                this.engineToolsManager.runTool(container.getEngine().toLowerCase(), container.getName(), tool.getId(),
                        this::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
            });
            this.lockableElements.add(toolButton);
            toolsContentPane.getChildren().add(toolButton);
        }

        toolsPane.getChildren().add(toolsContentPane);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // the TilePane adjusts the number of columns
                                                                    // already
        toolsContentPane.prefWidthProperty().bind(scrollPane.widthProperty());
        toolsContentPane.prefHeightProperty().bind(scrollPane.heightProperty());
        scrollPane.setBackground(toolsContentPane.getBackground());
        scrollPane.setContent(toolsPane);

        this.setContent(scrollPane);
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
