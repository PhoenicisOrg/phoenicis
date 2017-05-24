/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.javafx.views.mainwindow.containers;

import com.sun.javafx.collections.ImmutableObservableList;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.parameters.*;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.javafx.views.common.TextWithStyle;
import org.phoenicis.javafx.views.common.ThemeManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class WinePrefixContainerPanel extends AbstractContainerPanel<WinePrefixContainerDTO> {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";
    private final List<Node> lockableElements = new ArrayList<>();
    private Consumer<WinePrefixContainerDTO> onWineCfg = winePrefix -> {
    };
    private Consumer<WinePrefixContainerDTO> onRegedit = winePrefix -> {
    };
    private Consumer<WinePrefixContainerDTO> onWineboot = winePrefix -> {
    };
    private Consumer<WinePrefixContainerDTO> onWinebootRepair = winePrefix -> {
    };
    private Consumer<WinePrefixContainerDTO> onWineConsole = winePrefix -> {
    };
    private Consumer<WinePrefixContainerDTO> onTaskMgr = winePrefix -> {
    };
    private Consumer<WinePrefixContainerDTO> onKillProcess = winePrefix -> {
    };
    private Consumer<WinePrefixContainerDTO> onUninstaller = winePrefix -> {
    };
    private Consumer<WinePrefixContainerDTO> onOpenTerminalInWinePrefix = winePrefix -> {
    };
    private Consumer<WinePrefixContainerDTO> onDeletePrefix = winePrefix -> {
    };

    private BiConsumer<WinePrefixContainerDTO, RegistryParameter> onChangeSetting = (winePrefix, value) -> {
    };

    public WinePrefixContainerPanel(WinePrefixContainerDTO containerEntity, ThemeManager themeManager,
            List<EngineVersionDTO> engineVersions) {
        super(containerEntity, themeManager, engineVersions);
        this.getTabs().add(drawDisplayTab(containerEntity));
        this.getTabs().add(drawInputTab(containerEntity));
        this.getTabs().add(drawWineToolsTab(containerEntity));
        this.getTabs().add(drawToolsTab(containerEntity));
    }

    public void setOnDeletePrefix(Consumer<WinePrefixContainerDTO> onDeletePrefix) {
        this.onDeletePrefix = onDeletePrefix;
    }

    @Override
    Tab drawInformationTab(WinePrefixContainerDTO container, List<EngineVersionDTO> engineVersions) {
        final Tab informationTab = new Tab(tr("Information"));
        final VBox informationPane = new VBox();
        final Text title = new TextWithStyle(tr("Information"), TITLE_CSS_CLASS);

        informationPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        informationPane.getChildren().add(title);

        final GridPane informationContentPane = new GridPane();
        informationContentPane.getStyleClass().add("grid");

        informationContentPane.add(new TextWithStyle(tr("Name:"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        Label name = new Label(container.getName());
        name.setWrapText(true);
        informationContentPane.add(name, 1, 0);

        informationContentPane.add(new TextWithStyle(tr("Path:"), CAPTION_TITLE_CSS_CLASS), 0, 1);
        Label path = new Label(container.getPath());
        path.setWrapText(true);
        informationContentPane.add(path, 1, 1);

        informationContentPane.add(new TextWithStyle(tr("Wine version:"), CAPTION_TITLE_CSS_CLASS), 0, 2);
        Label version = new Label(container.getVersion());
        version.setWrapText(true);
        informationContentPane.add(version, 1, 2);

        informationContentPane.add(new TextWithStyle(tr("Wine architecture:"), CAPTION_TITLE_CSS_CLASS), 0, 3);
        Label architecture = new Label(container.getArchitecture());
        architecture.setWrapText(true);
        informationContentPane.add(architecture, 1, 3);

        informationContentPane.add(new TextWithStyle(tr("Wine distribution:"), CAPTION_TITLE_CSS_CLASS), 0, 4);
        Label distribution = new Label(container.getDistribution());
        distribution.setWrapText(true);
        informationContentPane.add(distribution, 1, 4);

        Region spacer = new Region();
        spacer.setPrefHeight(20);
        VBox.setVgrow(spacer, Priority.NEVER);

        ComboBox<EngineVersionDTO> changeEngineComboBox = new ComboBox<EngineVersionDTO>(
                FXCollections.observableList(engineVersions));
        changeEngineComboBox.setConverter(new StringConverter<EngineVersionDTO>() {
            @Override
            public String toString(EngineVersionDTO object) {
                return object.getVersion();
            }

            @Override
            public EngineVersionDTO fromString(String string) {
                return engineVersions.stream().filter(engineVersion -> engineVersion.getVersion().equals(string))
                        .findFirst().get();
            }
        });
        changeEngineComboBox.getSelectionModel().select(engineVersions.stream()
                .filter(engineVersion -> engineVersion.getVersion().equals(container.getVersion())).findFirst().get());

        Button deleteButton = new Button("Delete container");
        deleteButton.setOnMouseClicked(event -> this.deletePrefix(container));

        informationPane.getChildren().addAll(informationContentPane, spacer, changeEngineComboBox, deleteButton);
        informationTab.setContent(informationPane);
        informationTab.setClosable(false);
        return informationTab;
    }

    private void deletePrefix(WinePrefixContainerDTO winePrefix) {
        this.onDeletePrefix.accept(winePrefix);
    }

    private Tab drawDisplayTab(WinePrefixContainerDTO winePrefixContainerDTO) {
        final Tab displayTab = new Tab(tr("Display"));
        final VBox displayPane = new VBox();
        final Text title = new TextWithStyle(tr("Display settings"), TITLE_CSS_CLASS);

        displayPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        displayPane.getChildren().add(title);

        final GridPane displayContentPane = new GridPane();
        displayContentPane.getStyleClass().add("grid");

        final ComboBox<UseGLSL> glslComboBox = new ComboBox<>();
        glslComboBox.setMaxWidth(Double.MAX_VALUE);
        glslComboBox.setValue(winePrefixContainerDTO.getUseGlslValue());
        glslComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(glslComboBox, UseGLSL.class);
        displayContentPane.add(new TextWithStyle(tr("GLSL support"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        displayContentPane.add(glslComboBox, 1, 0);

        final ComboBox<DirectDrawRenderer> directDrawRendererComboBox = new ComboBox<>();
        directDrawRendererComboBox.setMaxWidth(Double.MAX_VALUE);
        directDrawRendererComboBox.setValue(winePrefixContainerDTO.getDirectDrawRenderer());
        directDrawRendererComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(directDrawRendererComboBox, DirectDrawRenderer.class);
        displayContentPane.add(new TextWithStyle(tr("Direct Draw Renderer"), CAPTION_TITLE_CSS_CLASS), 0, 1);
        displayContentPane.add(directDrawRendererComboBox, 1, 1);

        final ComboBox<VideoMemorySize> videoMemorySizeComboBox = new ComboBox<>();
        videoMemorySizeComboBox.setMaxWidth(Double.MAX_VALUE);
        videoMemorySizeComboBox.setValue(winePrefixContainerDTO.getVideoMemorySize());
        videoMemorySizeComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItemsVideoMemorySize(videoMemorySizeComboBox);
        displayContentPane.add(new TextWithStyle(tr("Video memory size"), CAPTION_TITLE_CSS_CLASS), 0, 2);
        displayContentPane.add(videoMemorySizeComboBox, 1, 2);

        final ComboBox<OffscreenRenderingMode> offscreenRenderingModeComboBox = new ComboBox<>();
        offscreenRenderingModeComboBox.setMaxWidth(Double.MAX_VALUE);
        offscreenRenderingModeComboBox.setValue(winePrefixContainerDTO.getOffscreenRenderingMode());
        offscreenRenderingModeComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(offscreenRenderingModeComboBox, OffscreenRenderingMode.class);
        displayContentPane.add(new TextWithStyle(tr("Offscreen rendering mode"), CAPTION_TITLE_CSS_CLASS), 0, 3);
        displayContentPane.add(offscreenRenderingModeComboBox, 1, 3);

        final ComboBox<RenderTargetModeLock> renderTargetModeLockComboBox = new ComboBox<>();
        renderTargetModeLockComboBox.setMaxWidth(Double.MAX_VALUE);
        renderTargetModeLockComboBox.setValue(winePrefixContainerDTO.getRenderTargetModeLock());
        renderTargetModeLockComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(renderTargetModeLockComboBox, RenderTargetModeLock.class);
        displayContentPane.add(new TextWithStyle(tr("Render target lock mode"), CAPTION_TITLE_CSS_CLASS), 0, 4);
        displayContentPane.add(renderTargetModeLockComboBox, 1, 4);

        final ComboBox<Multisampling> multisamplingComboBox = new ComboBox<>();
        multisamplingComboBox.setMaxWidth(Double.MAX_VALUE);
        multisamplingComboBox.setValue(winePrefixContainerDTO.getMultisampling());
        multisamplingComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(multisamplingComboBox, Multisampling.class);
        displayContentPane.add(new TextWithStyle(tr("Multisampling"), CAPTION_TITLE_CSS_CLASS), 0, 5);
        displayContentPane.add(multisamplingComboBox, 1, 5);

        final ComboBox<StrictDrawOrdering> strictDrawOrderingComboBox = new ComboBox<>();
        strictDrawOrderingComboBox.setMaxWidth(Double.MAX_VALUE);
        strictDrawOrderingComboBox.setValue(winePrefixContainerDTO.getStrictDrawOrdering());
        strictDrawOrderingComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(strictDrawOrderingComboBox, StrictDrawOrdering.class);
        displayContentPane.add(new TextWithStyle(tr("Strict Draw Ordering"), CAPTION_TITLE_CSS_CLASS), 0, 6);
        displayContentPane.add(strictDrawOrderingComboBox, 1, 6);

        final ComboBox<AlwaysOffscreen> alwaysOffscreenComboBox = new ComboBox<>();
        alwaysOffscreenComboBox.setMaxWidth(Double.MAX_VALUE);
        alwaysOffscreenComboBox.setValue(winePrefixContainerDTO.getAlwaysOffscreen());
        alwaysOffscreenComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(alwaysOffscreenComboBox, AlwaysOffscreen.class);
        displayContentPane.add(new TextWithStyle(tr("Always Offscreen"), CAPTION_TITLE_CSS_CLASS), 0, 7);
        displayContentPane.add(alwaysOffscreenComboBox, 1, 7);

        Region spacer = new Region();
        GridPane.setHgrow(spacer, Priority.ALWAYS);
        displayContentPane.add(spacer, 2, 0);

        displayPane.getChildren().addAll(displayContentPane);
        displayTab.setContent(displayPane);
        displayTab.setClosable(false);

        lockableElements.addAll(Arrays.asList(glslComboBox, directDrawRendererComboBox, offscreenRenderingModeComboBox,
                renderTargetModeLockComboBox, multisamplingComboBox, strictDrawOrderingComboBox,
                alwaysOffscreenComboBox, videoMemorySizeComboBox));

        return displayTab;
    }

    private void changeSetting(WinePrefixContainerDTO winePrefixContainerDTO, RegistryParameter newValue) {
        onChangeSetting.accept(winePrefixContainerDTO, newValue);
        lockAll();
    }

    private Tab drawInputTab(WinePrefixContainerDTO containerEntity) {
        final Tab inputTab = new Tab(tr("Input"));
        final VBox inputPane = new VBox();
        final Text title = new TextWithStyle(tr("Input settings"), TITLE_CSS_CLASS);

        inputPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        inputPane.getChildren().add(title);

        final GridPane inputContentPane = new GridPane();
        inputContentPane.getStyleClass().add("grid");

        final ComboBox<MouseWarpOverride> mouseWarpOverrideComboBox = new ComboBox<>();
        mouseWarpOverrideComboBox.setValue(containerEntity.getMouseWarpOverride());
        addItems(mouseWarpOverrideComboBox, MouseWarpOverride.class);
        inputContentPane.add(new TextWithStyle(tr("Mouse Warp Override"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        inputContentPane.add(mouseWarpOverrideComboBox, 1, 0);

        inputContentPane.getColumnConstraints().addAll(new ColumnConstraintsWithPercentage(30),
                new ColumnConstraintsWithPercentage(70));

        inputPane.getChildren().addAll(inputContentPane);
        inputTab.setContent(inputPane);
        inputTab.setClosable(false);

        lockableElements.add(mouseWarpOverrideComboBox);
        return inputTab;
    }

    private Tab drawWineToolsTab(WinePrefixContainerDTO containerEntity) {
        final Tab toolsTab = new Tab(tr("Wine tools"));
        final VBox toolsPane = new VBox();
        final Text title = new TextWithStyle(tr("Wine tools"), TITLE_CSS_CLASS);

        toolsPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        toolsPane.getChildren().add(title);

        final GridPane toolsContentPane = new GridPane();
        toolsContentPane.getStyleClass().add("grid");

        Button configureWine = new Button(tr("Configure Wine"));
        configureWine.getStyleClass().addAll("wineToolButton", "configureWine");
        configureWine.setOnMouseClicked(e -> {
            this.lockAll();
            this.onWineCfg.accept(containerEntity);
        });

        GridPane.setHalignment(configureWine, HPos.CENTER);

        Button registryEditor = new Button(tr("Registry Editor"));
        registryEditor.getStyleClass().addAll("wineToolButton", "registryEditor");
        registryEditor.setOnMouseClicked(e -> {
            this.lockAll();
            this.onRegedit.accept(containerEntity);
        });

        GridPane.setHalignment(registryEditor, HPos.CENTER);

        Button rebootWindows = new Button(tr("Windows reboot"));
        rebootWindows.getStyleClass().addAll("wineToolButton", "rebootWindows");
        rebootWindows.setOnMouseClicked(e -> {
            this.lockAll();
            this.onWineboot.accept(containerEntity);
        });

        GridPane.setHalignment(rebootWindows, HPos.CENTER);

        Button repairVirtualDrive = new Button(tr("Repair virtual drive"));
        repairVirtualDrive.getStyleClass().addAll("wineToolButton", "repairVirtualDrive");
        repairVirtualDrive.setOnMouseClicked(e -> {
            this.lockAll();
            this.onWinebootRepair.accept(containerEntity);
        });

        GridPane.setHalignment(repairVirtualDrive, HPos.CENTER);

        Button commandPrompt = new Button(tr("Command prompt"));
        commandPrompt.getStyleClass().addAll("wineToolButton", "commandPrompt");
        commandPrompt.setOnMouseClicked(e -> {
            this.lockAll();
            this.onWineConsole.accept(containerEntity);
        });

        GridPane.setHalignment(commandPrompt, HPos.CENTER);

        Button taskManager = new Button(tr("Task manager"));
        taskManager.getStyleClass().addAll("wineToolButton", "taskManager");
        taskManager.setOnMouseClicked(e -> {
            this.lockAll();
            this.onTaskMgr.accept(containerEntity);
        });

        GridPane.setHalignment(taskManager, HPos.CENTER);

        Button killProcesses = new Button(tr("Kill processes"));
        killProcesses.getStyleClass().addAll("wineToolButton", "killProcesses");
        killProcesses.setOnMouseClicked(e -> {
            this.lockAll();
            this.onKillProcess.accept(containerEntity);
        });

        GridPane.setHalignment(killProcesses, HPos.CENTER);

        Button uninstallWine = new Button(tr("Wine uninstaller"));
        uninstallWine.getStyleClass().addAll("wineToolButton", "uninstallWine");
        uninstallWine.setOnMouseClicked(e -> {
            this.lockAll();
            this.onUninstaller.accept(containerEntity);
        });

        GridPane.setHalignment(uninstallWine, HPos.CENTER);

        this.lockableElements.addAll(Arrays.asList(configureWine, registryEditor, rebootWindows, repairVirtualDrive,
                commandPrompt, taskManager, uninstallWine));

        toolsContentPane.add(configureWine, 0, 0);
        toolsContentPane.add(wineToolCaption(tr("Configure Wine")), 0, 1);

        toolsContentPane.add(registryEditor, 1, 0);
        toolsContentPane.add(wineToolCaption(tr("Registry Editor")), 1, 1);

        toolsContentPane.add(rebootWindows, 2, 0);
        toolsContentPane.add(wineToolCaption(tr("Windows reboot")), 2, 1);

        toolsContentPane.add(repairVirtualDrive, 3, 0);
        toolsContentPane.add(wineToolCaption(tr("Repair virtual drive")), 3, 1);

        toolsContentPane.add(commandPrompt, 0, 3);
        toolsContentPane.add(wineToolCaption(tr("Command prompt")), 0, 4);

        toolsContentPane.add(taskManager, 1, 3);
        toolsContentPane.add(wineToolCaption(tr("Task manager")), 1, 4);

        toolsContentPane.add(killProcesses, 2, 3);
        toolsContentPane.add(wineToolCaption(tr("Kill processes")), 2, 4);

        toolsContentPane.add(uninstallWine, 3, 3);
        toolsContentPane.add(wineToolCaption(tr("Wine uninstaller")), 3, 4);

        toolsPane.getChildren().addAll(toolsContentPane);

        toolsContentPane.getColumnConstraints().addAll(new ColumnConstraintsWithPercentage(25),
                new ColumnConstraintsWithPercentage(25), new ColumnConstraintsWithPercentage(25),
                new ColumnConstraintsWithPercentage(25));

        toolsTab.setContent(toolsPane);
        toolsTab.setClosable(false);
        return toolsTab;
    }

    private Tab drawToolsTab(WinePrefixContainerDTO containerEntity) {
        final Tab toolsTab = new Tab(tr("Tools"));
        final VBox toolsPane = new VBox();
        final Text title = new TextWithStyle(tr("Tools"), TITLE_CSS_CLASS);

        toolsPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        toolsPane.getChildren().add(title);

        final GridPane toolsContentPane = new GridPane();
        toolsContentPane.getStyleClass().add("grid");

        Button openTerminal = new Button(tr("Open a terminal"));
        openTerminal.getStyleClass().addAll("wineToolButton", "openTerminal");
        openTerminal.setOnMouseClicked(e -> {
            this.lockAll();
            this.onOpenTerminalInWinePrefix.accept(containerEntity);
        });

        GridPane.setHalignment(openTerminal, HPos.CENTER);

        this.lockableElements.add(openTerminal);

        toolsContentPane.add(openTerminal, 0, 0);
        toolsContentPane.add(wineToolCaption(tr("Open a terminal")), 0, 1);

        toolsPane.getChildren().addAll(toolsContentPane);

        toolsContentPane.getColumnConstraints().addAll(new ColumnConstraintsWithPercentage(25),
                new ColumnConstraintsWithPercentage(25), new ColumnConstraintsWithPercentage(25),
                new ColumnConstraintsWithPercentage(25));

        toolsTab.setContent(toolsPane);
        toolsTab.setClosable(false);
        return toolsTab;
    }

    private Text wineToolCaption(String caption) {
        final Text text = new TextWithStyle(caption, "wineToolCaption");
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);
        lockableElements.add(text);
        return text;
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

    private void addItemsVideoMemorySize(ComboBox<VideoMemorySize> videoMemorySizeComboBox) {
        videoMemorySizeComboBox.setItems(new ImmutableObservableList<>(VideoMemorySize.possibleValues()));
    }

    private <T extends Enum> void addItems(ComboBox<T> comboBox, Class<T> clazz) {
        final List<T> possibleValues = new ArrayList<>(EnumSet.allOf(clazz));

        final ObservableList<T> possibleValuesObservable = new ObservableListWrapper<>(possibleValues);
        comboBox.setItems(possibleValuesObservable);
    }

    public void setOnWineCfg(Consumer<WinePrefixContainerDTO> onWineCfg) {
        this.onWineCfg = onWineCfg;
    }

    public void setOnRegedit(Consumer<WinePrefixContainerDTO> onRegedit) {
        this.onRegedit = onRegedit;
    }

    public void setOnWineboot(Consumer<WinePrefixContainerDTO> onWineboot) {
        this.onWineboot = onWineboot;
    }

    public void setOnWinebootRepair(Consumer<WinePrefixContainerDTO> onWinebootRepair) {
        this.onWinebootRepair = onWinebootRepair;
    }

    public void setOnWineConsole(Consumer<WinePrefixContainerDTO> onWineconsole) {
        this.onWineConsole = onWineconsole;
    }

    public void setOnTaskMgr(Consumer<WinePrefixContainerDTO> onTaskMgr) {
        this.onTaskMgr = onTaskMgr;
    }

    public void setOnUninstaller(Consumer<WinePrefixContainerDTO> onUninstaller) {
        this.onUninstaller = onUninstaller;
    }

    public void setOnKillProcess(Consumer<WinePrefixContainerDTO> onKillProcess) {
        this.onKillProcess = onKillProcess;
    }

    public void setOnOpenTerminalInWinePrefix(Consumer<WinePrefixContainerDTO> onOpenTerminalInWinePrefix) {
        this.onOpenTerminalInWinePrefix = onOpenTerminalInWinePrefix;
    }

    public void setOnChangeSetting(BiConsumer<WinePrefixContainerDTO, RegistryParameter> onChangeSetting) {
        this.onChangeSetting = onChangeSetting;
    }
}
