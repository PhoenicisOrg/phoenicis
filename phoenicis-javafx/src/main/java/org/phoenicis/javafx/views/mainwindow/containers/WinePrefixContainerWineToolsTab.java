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
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.engines.dto.EngineToolDTO;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.TextWithStyle;

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
    private EngineToolsManager engineToolsManager;

    private final List<Node> lockableElements = new ArrayList<>();

    public WinePrefixContainerWineToolsTab(WinePrefixContainerDTO container,
            WinePrefixContainerController winePrefixContainerController,
            EngineToolsManager engineToolsManager) {
        super(tr("Wine tools"));

        this.container = container;
        this.winePrefixContainerController = winePrefixContainerController;
        this.engineToolsManager = engineToolsManager;

        this.setClosable(false);

        // TODO: fetch from repository or Javascript Wine object
        List<EngineToolDTO> tools = new ArrayList<>();
        tools.add(new EngineToolDTO.Builder()
                .withId("ConfigureWine")
                .withName(tr("Configure Wine"))
                .withMiniature("configureWine")
                .build());
        tools.add(new EngineToolDTO.Builder()
                .withId("WineRegistryEditor")
                .withName(tr("Registry Editor"))
                .withMiniature("registryEditor")
                .build());
        tools.add(new EngineToolDTO.Builder()
                .withId("RebootWine")
                .withName(tr("Windows reboot"))
                .withMiniature("rebootWindows")
                .build());
        tools.add(new EngineToolDTO.Builder()
                .withId("WineConsole")
                .withName(tr("Command prompt"))
                .withMiniature("commandPrompt")
                .build());
        tools.add(new EngineToolDTO.Builder()
                .withId("WineTaskManager")
                .withName(tr("Task manager"))
                .withMiniature("taskManager")
                .build());
        tools.add(new EngineToolDTO.Builder()
                .withId("WineUninstaller")
                .withName(tr("Wine uninstaller"))
                .withMiniature("uninstallWine")
                .build());
        tools.add(new EngineToolDTO.Builder()
                .withId("RepairWinePrefix")
                .withName(tr("Repair virtual drive"))
                .withMiniature("repairVirtualDrive")
                .build());
        tools.add(new EngineToolDTO.Builder()
                .withId("KillWineProcesses")
                .withName(tr("Kill processes"))
                .withMiniature("killProcesses")
                .build());

        this.populate(tools);
    }

    private void populate(List<EngineToolDTO> tools) {
        final VBox toolsPane = new VBox();
        final Text title = new TextWithStyle(tr("Wine tools"), TITLE_CSS_CLASS);

        toolsPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        toolsPane.getChildren().add(title);

        final TilePane toolsContentPane = new TilePane();
        toolsContentPane.setPrefColumns(3);
        toolsContentPane.getStyleClass().add("grid");

        for (EngineToolDTO tool : tools) {
            Button toolButton = new Button(tool.getName());
            toolButton.getStyleClass().addAll("toolButton", tool.getMiniature());
            toolButton.setOnMouseClicked(event -> {
                this.lockAll();
                this.engineToolsManager.runTool(container.getEngine(), container.getName(), tool.getId(),
                        this::unlockAll,
                        e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
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
