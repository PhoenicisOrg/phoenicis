package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.WinePrefixContainerController;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.TextWithStyle;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

import java.util.ArrayList;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 27.05.17.
 */
public class WinePrefixContainerWineToolsTab extends Tab {
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";

    private final WinePrefixContainerDTO container;
    private final WinePrefixContainerController winePrefixContainerController;
    private ScriptInterpreter scriptInterpreter;

    private final List<Node> lockableElements = new ArrayList<>();

    public WinePrefixContainerWineToolsTab(WinePrefixContainerDTO container,
            WinePrefixContainerController winePrefixContainerController, ScriptInterpreter scriptInterpreter,
            ApplicationDTO engineTools) {
        super(tr("Wine tools"));

        this.container = container;
        this.winePrefixContainerController = winePrefixContainerController;
        this.scriptInterpreter = scriptInterpreter;

        this.setClosable(false);

        this.populate(engineTools);
    }

    private void populate(ApplicationDTO engineTools) {
        final VBox toolsPane = new VBox();
        final Text title = new TextWithStyle(tr("Wine tools"), TITLE_CSS_CLASS);

        toolsPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        toolsPane.getChildren().add(title);

        final TilePane toolsContentPane = new TilePane();
        toolsContentPane.setPrefColumns(3);
        toolsContentPane.getStyleClass().add("grid");

        for (ScriptDTO tool : engineTools.getScripts()) {
            Button toolButton = new Button(tool.getScriptName());
            toolButton.getStyleClass().addAll("toolButton");
            //toolButton.setStyle("-fx-background-image: url('" + tool.getIcon() + "');");
            toolButton.setOnMouseClicked(event -> {
                this.lockAll();
                this.scriptInterpreter.runScript(tool.getScript(),
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
                //TODO: if finished this::unlockAll
            });
            this.lockableElements.add(toolButton);
            toolsContentPane.getChildren().add(toolButton);
        }

        toolsPane.getChildren().add(toolsContentPane);

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
