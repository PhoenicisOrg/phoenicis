package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.WinePrefixContainerController;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.common.TextWithStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.translate;

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

    public WinePrefixContainerWineToolsTab(WinePrefixContainerDTO container, WinePrefixContainerController winePrefixContainerController) {
        super(translate("Wine tools"));

        this.container = container;
        this.winePrefixContainerController = winePrefixContainerController;

        this.setClosable(false);

        this.populate();
    }

    private void populate() {
        final VBox toolsPane = new VBox();
        final Text title = new TextWithStyle(translate("Wine tools"), TITLE_CSS_CLASS);

        toolsPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        toolsPane.getChildren().add(title);

        final GridPane toolsContentPane = new GridPane();
        toolsContentPane.getStyleClass().add("grid");

        Button configureWine = new Button(translate("Configure Wine"));
        configureWine.getStyleClass().addAll("wineToolButton", "configureWine");
        configureWine.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.runInPrefix(container, "winecfg", this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        GridPane.setHalignment(configureWine, HPos.CENTER);

        Button registryEditor = new Button(translate("Registry Editor"));
        registryEditor.getStyleClass().addAll("wineToolButton", "registryEditor");
        registryEditor.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.runInPrefix(container, "regedit", this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        GridPane.setHalignment(registryEditor, HPos.CENTER);

        Button rebootWindows = new Button(translate("Windows reboot"));
        rebootWindows.getStyleClass().addAll("wineToolButton", "rebootWindows");
        rebootWindows.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.runInPrefix(container, "wineboot", this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        GridPane.setHalignment(rebootWindows, HPos.CENTER);

        Button repairVirtualDrive = new Button(translate("Repair virtual drive"));
        repairVirtualDrive.getStyleClass().addAll("wineToolButton", "repairVirtualDrive");
        repairVirtualDrive.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.repairPrefix(container, this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        GridPane.setHalignment(repairVirtualDrive, HPos.CENTER);

        Button commandPrompt = new Button(translate("Command prompt"));
        commandPrompt.getStyleClass().addAll("wineToolButton", "commandPrompt");
        commandPrompt.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.runInPrefix(container, "wineconsole", this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        GridPane.setHalignment(commandPrompt, HPos.CENTER);

        Button taskManager = new Button(translate("Task manager"));
        taskManager.getStyleClass().addAll("wineToolButton", "taskManager");
        taskManager.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.runInPrefix(container, "taskmgr", this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        GridPane.setHalignment(taskManager, HPos.CENTER);

        Button killProcesses = new Button(translate("Kill processes"));
        killProcesses.getStyleClass().addAll("wineToolButton", "killProcesses");
        killProcesses.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.killProcesses(container, this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        GridPane.setHalignment(killProcesses, HPos.CENTER);

        Button uninstallWine = new Button(translate("Wine uninstaller"));
        uninstallWine.getStyleClass().addAll("wineToolButton", "uninstallWine");
        uninstallWine.setOnMouseClicked(event -> {
            this.lockAll();
            winePrefixContainerController.runInPrefix(container, "uninstaller", this::unlockAll,
                    e -> Platform.runLater(() -> new ErrorMessage("Error", e).show()));
        });

        GridPane.setHalignment(uninstallWine, HPos.CENTER);

        this.lockableElements.addAll(Arrays.asList(configureWine, registryEditor, rebootWindows, repairVirtualDrive,
                commandPrompt, taskManager, uninstallWine));

        toolsContentPane.add(configureWine, 0, 0);
        toolsContentPane.add(wineToolCaption(translate("Configure Wine")), 0, 1);

        toolsContentPane.add(registryEditor, 1, 0);
        toolsContentPane.add(wineToolCaption(translate("Registry Editor")), 1, 1);

        toolsContentPane.add(rebootWindows, 2, 0);
        toolsContentPane.add(wineToolCaption(translate("Windows reboot")), 2, 1);

        toolsContentPane.add(repairVirtualDrive, 3, 0);
        toolsContentPane.add(wineToolCaption(translate("Repair virtual drive")), 3, 1);

        toolsContentPane.add(commandPrompt, 0, 3);
        toolsContentPane.add(wineToolCaption(translate("Command prompt")), 0, 4);

        toolsContentPane.add(taskManager, 1, 3);
        toolsContentPane.add(wineToolCaption(translate("Task manager")), 1, 4);

        toolsContentPane.add(killProcesses, 2, 3);
        toolsContentPane.add(wineToolCaption(translate("Kill processes")), 2, 4);

        toolsContentPane.add(uninstallWine, 3, 3);
        toolsContentPane.add(wineToolCaption(translate("Wine uninstaller")), 3, 4);

        toolsPane.getChildren().addAll(toolsContentPane);

        toolsContentPane.getColumnConstraints().addAll(new ColumnConstraintsWithPercentage(25),
                new ColumnConstraintsWithPercentage(25), new ColumnConstraintsWithPercentage(25),
                new ColumnConstraintsWithPercentage(25));

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

    private Text wineToolCaption(String caption) {
        final Text text = new TextWithStyle(caption, "wineToolCaption");
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);
        lockableElements.add(text);
        return text;
    }
}
