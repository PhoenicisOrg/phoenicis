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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 27.05.17.
 */
public class WinePrefixContainerWineToolsTab extends Tab {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";

    private final WinePrefixContainerDTO container;
    private final WinePrefixContainerController winePrefixContainerController;

    private final List<Node> lockableElements = new ArrayList<>();

    public WinePrefixContainerWineToolsTab(WinePrefixContainerDTO container,
            WinePrefixContainerController winePrefixContainerController) {
        super(tr("Wine tools"));

        this.container = container;
        this.winePrefixContainerController = winePrefixContainerController;

        this.setClosable(false);

        this.populate();
    }

    private void populate() {
        final VBox toolsPane = new VBox();
        final Text title = new TextWithStyle(tr("Wine tools"), TITLE_CSS_CLASS);

        toolsPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        toolsPane.getChildren().add(title);

        final TilePane toolsContentPane = new TilePane();
        toolsContentPane.setPrefColumns(3);
        toolsContentPane.getStyleClass().add("grid");

        Button configureWine = new Button(tr("Configure Wine"));
        configureWine.getStyleClass().addAll("wineToolButton", "configureWine");
        configureWine.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.runInPrefix(container, "winecfg", this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        Button registryEditor = new Button(tr("Registry Editor"));
        registryEditor.getStyleClass().addAll("wineToolButton", "registryEditor");
        registryEditor.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.runInPrefix(container, "regedit", this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        Button rebootWindows = new Button(tr("Windows reboot"));
        rebootWindows.getStyleClass().addAll("wineToolButton", "rebootWindows");
        rebootWindows.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.runInPrefix(container, "wineboot", this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        Button repairVirtualDrive = new Button(tr("Repair virtual drive"));
        repairVirtualDrive.getStyleClass().addAll("wineToolButton", "repairVirtualDrive");
        repairVirtualDrive.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.repairPrefix(container, this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        Button commandPrompt = new Button(tr("Command prompt"));
        commandPrompt.getStyleClass().addAll("wineToolButton", "commandPrompt");
        commandPrompt.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.runInPrefix(container, "wineconsole", this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        Button taskManager = new Button(tr("Task manager"));
        taskManager.getStyleClass().addAll("wineToolButton", "taskManager");
        taskManager.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.runInPrefix(container, "taskmgr", this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        Button killProcesses = new Button(tr("Kill processes"));
        killProcesses.getStyleClass().addAll("wineToolButton", "killProcesses");
        killProcesses.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.killProcesses(container, this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        Button uninstallWine = new Button(tr("Wine uninstaller"));
        uninstallWine.getStyleClass().addAll("wineToolButton", "uninstallWine");
        uninstallWine.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.runInPrefix(container, "uninstaller", this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        this.lockableElements.addAll(Arrays.asList(configureWine, registryEditor, rebootWindows, repairVirtualDrive,
                commandPrompt, taskManager, uninstallWine));

        toolsContentPane.getChildren().addAll(configureWine, registryEditor, rebootWindows, repairVirtualDrive,
                commandPrompt, taskManager, killProcesses, uninstallWine);

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
