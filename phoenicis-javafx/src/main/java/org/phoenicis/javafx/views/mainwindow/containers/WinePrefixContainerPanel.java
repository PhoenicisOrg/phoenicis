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

import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.parameters.*;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.javafx.views.common.TextWithStyle;
import com.sun.javafx.collections.ImmutableObservableList;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.phoenicis.javafx.views.common.ThemeManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

public class WinePrefixContainerPanel extends AbstractContainerPanel<WinePrefixContainerDTO> {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";
    private final List<Node> lockableElements = new ArrayList<>();
    private Consumer<WinePrefixContainerDTO> onWineCfg = winePrefix -> {};
    private Consumer<WinePrefixContainerDTO> onRegedit = winePrefix -> {};
    private Consumer<WinePrefixContainerDTO> onWineboot = winePrefix -> {};
    private Consumer<WinePrefixContainerDTO> onWinebootRepair = winePrefix -> {};
    private Consumer<WinePrefixContainerDTO> onWineConsole = winePrefix -> {};
    private Consumer<WinePrefixContainerDTO> onTaskMgr = winePrefix -> {};
    private Consumer<WinePrefixContainerDTO> onKillProcess = winePrefix -> {};
    private Consumer<WinePrefixContainerDTO> onUninstaller = winePrefix -> {};
    private Consumer<WinePrefixContainerDTO> onOpenTerminalInWinePrefix = winePrefix -> {};
    private Consumer<WinePrefixContainerDTO> onDeletePrefix = winePrefix -> {};

    private BiConsumer<WinePrefixContainerDTO, RegistryParameter> onChangeSetting = (winePrefix, value) -> {};

    public WinePrefixContainerPanel(WinePrefixContainerDTO containerEntity, ThemeManager themeManager) {
        super(containerEntity, themeManager);
        this.getTabs().add(drawDisplayTab(containerEntity));
        this.getTabs().add(drawInputTab(containerEntity));
        this.getTabs().add(drawWineToolsTab(containerEntity));
        this.getTabs().add(drawToolsTab(containerEntity));
    }

    public void setOnDeletePrefix(Consumer<WinePrefixContainerDTO> onDeletePrefix) {
        this.onDeletePrefix = onDeletePrefix;
    }

    @Override
    Tab drawInformationTab(WinePrefixContainerDTO container) {
        final Tab informationTab = new Tab(translate("Information"));
        final VBox informationPane = new VBox();
        final Text title = new TextWithStyle(translate("Information"), TITLE_CSS_CLASS);

        informationPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        informationPane.getChildren().add(title);

        final GridPane informationContentPane = new GridPane();
        informationContentPane.getStyleClass().add("grid");

        informationContentPane.add(new TextWithStyle(translate("Name:"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        Label name = new Label(container.getName());
        name.setWrapText(true);
        informationContentPane.add(name, 1, 0);

        informationContentPane.add(new TextWithStyle(translate("Path:"), CAPTION_TITLE_CSS_CLASS), 0, 1);
        Label path = new Label(container.getPath());
        path.setWrapText(true);
        informationContentPane.add(path, 1, 1);

        informationContentPane.add(new TextWithStyle(translate("Wine version:"), CAPTION_TITLE_CSS_CLASS), 0, 2);
        Label version = new Label(container.getVersion());
        version.setWrapText(true);
        informationContentPane.add(version, 1, 2);

        informationContentPane.add(new TextWithStyle(translate("Wine architecture:"), CAPTION_TITLE_CSS_CLASS), 0, 3);
        Label architecture = new Label(container.getArchitecture());
        architecture.setWrapText(true);
        informationContentPane.add(architecture, 1, 3);

        informationContentPane.add(new TextWithStyle(translate("Wine distribution:"), CAPTION_TITLE_CSS_CLASS), 0, 4);
        Label distribution = new Label(container.getDistribution());
        distribution.setWrapText(true);
        informationContentPane.add(distribution, 1, 4);

        informationContentPane.setHgap(20);
        informationContentPane.setVgap(10);

        Region spacer = new Region();
        spacer.setPrefHeight(30);
        VBox.setVgrow(spacer, Priority.NEVER);

        Button deleteButton = new Button("Delete container");
        deleteButton.setOnMouseClicked(event -> this.deletePrefix(container));

        informationPane.getChildren().addAll(informationContentPane, spacer, deleteButton);
        informationTab.setContent(informationPane);
        informationTab.setClosable(false);
        return informationTab;
    }

    private void deletePrefix(WinePrefixContainerDTO winePrefix) {
        this.onDeletePrefix.accept(winePrefix);
    }


    private Tab drawDisplayTab(WinePrefixContainerDTO winePrefixContainerDTO) {
        final Tab displayTab = new Tab(translate("Display"));
        final VBox displayPane = new VBox();
        final Text title = new TextWithStyle(translate("Display settings"), TITLE_CSS_CLASS);

        displayPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        displayPane.getChildren().add(title);

        final GridPane displayContentPane = new GridPane();
        displayContentPane.getStyleClass().add("grid");

        final ComboBox<UseGLSL> glslComboBox = new ComboBox<>();
        glslComboBox.setMaxWidth(Double.MAX_VALUE);
        glslComboBox.setValue(winePrefixContainerDTO.getUseGlslValue());
        glslComboBox.valueProperty().addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(glslComboBox, UseGLSL.class);
        displayContentPane.add(new TextWithStyle(translate("GLSL support"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        displayContentPane.add(glslComboBox, 1, 0);

        final ComboBox<DirectDrawRenderer> directDrawRendererComboBox = new ComboBox<>();
        directDrawRendererComboBox.setMaxWidth(Double.MAX_VALUE);
        directDrawRendererComboBox.setValue(winePrefixContainerDTO.getDirectDrawRenderer());
        directDrawRendererComboBox.valueProperty().addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(directDrawRendererComboBox, DirectDrawRenderer.class);
        displayContentPane.add(new TextWithStyle(translate("Direct Draw Renderer"), CAPTION_TITLE_CSS_CLASS), 0, 1);
        displayContentPane.add(directDrawRendererComboBox, 1, 1);

        final ComboBox<VideoMemorySize> videoMemorySizeComboBox = new ComboBox<>();
        videoMemorySizeComboBox.setMaxWidth(Double.MAX_VALUE);
        videoMemorySizeComboBox.setValue(winePrefixContainerDTO.getVideoMemorySize());
        videoMemorySizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItemsVideoMemorySize(videoMemorySizeComboBox);
        displayContentPane.add(new TextWithStyle(translate("Video memory size"), CAPTION_TITLE_CSS_CLASS), 0, 2);
        displayContentPane.add(videoMemorySizeComboBox, 1, 2);

        final ComboBox<OffscreenRenderingMode> offscreenRenderingModeComboBox = new ComboBox<>();
        offscreenRenderingModeComboBox.setMaxWidth(Double.MAX_VALUE);
        offscreenRenderingModeComboBox.setValue(winePrefixContainerDTO.getOffscreenRenderingMode());
        offscreenRenderingModeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(offscreenRenderingModeComboBox, OffscreenRenderingMode.class);
        displayContentPane.add(new TextWithStyle(translate("Offscreen rendering mode"), CAPTION_TITLE_CSS_CLASS), 0, 3);
        displayContentPane.add(offscreenRenderingModeComboBox, 1, 3);

        final ComboBox<RenderTargetModeLock> renderTargetModeLockComboBox = new ComboBox<>();
        renderTargetModeLockComboBox.setMaxWidth(Double.MAX_VALUE);
        renderTargetModeLockComboBox.setValue(winePrefixContainerDTO.getRenderTargetModeLock());
        renderTargetModeLockComboBox.valueProperty().addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(renderTargetModeLockComboBox, RenderTargetModeLock.class);
        displayContentPane.add(new TextWithStyle(translate("Render target lock mode"), CAPTION_TITLE_CSS_CLASS), 0, 4);
        displayContentPane.add(renderTargetModeLockComboBox, 1, 4);

        final ComboBox<Multisampling> multisamplingComboBox = new ComboBox<>();
        multisamplingComboBox.setMaxWidth(Double.MAX_VALUE);
        multisamplingComboBox.setValue(winePrefixContainerDTO.getMultisampling());
        multisamplingComboBox.valueProperty().addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(multisamplingComboBox, Multisampling.class);
        displayContentPane.add(new TextWithStyle(translate("Multisampling"), CAPTION_TITLE_CSS_CLASS), 0, 5);
        displayContentPane.add(multisamplingComboBox, 1, 5);

        final ComboBox<StrictDrawOrdering> strictDrawOrderingComboBox = new ComboBox<>();
        strictDrawOrderingComboBox.setMaxWidth(Double.MAX_VALUE);
        strictDrawOrderingComboBox.setValue(winePrefixContainerDTO.getStrictDrawOrdering());
        strictDrawOrderingComboBox.valueProperty().addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(strictDrawOrderingComboBox, StrictDrawOrdering.class);
        displayContentPane.add(new TextWithStyle(translate("Strict Draw Ordering"), CAPTION_TITLE_CSS_CLASS), 0, 6);
        displayContentPane.add(strictDrawOrderingComboBox, 1, 6);

        final ComboBox<AlwaysOffscreen> alwaysOffscreenComboBox = new ComboBox<>();
        alwaysOffscreenComboBox.setMaxWidth(Double.MAX_VALUE);
        alwaysOffscreenComboBox.setValue(winePrefixContainerDTO.getAlwaysOffscreen());
        alwaysOffscreenComboBox.valueProperty().addListener((observable, oldValue, newValue) -> this.changeSetting(winePrefixContainerDTO, newValue));
        addItems(alwaysOffscreenComboBox, AlwaysOffscreen.class);
        displayContentPane.add(new TextWithStyle(translate("Always Offscreen"), CAPTION_TITLE_CSS_CLASS), 0, 7);
        displayContentPane.add(alwaysOffscreenComboBox, 1, 7);

        displayContentPane.getRowConstraints().addAll(
                new RowConstraints(50.),
                new RowConstraints(50.),
                new RowConstraints(50.),
                new RowConstraints(50.),
                new RowConstraints(50.),
                new RowConstraints(50.),
                new RowConstraints(50.),
                new RowConstraints(50.),
                new RowConstraints(50.)
        );

        displayContentPane.setHgap(20);

        Region spacer = new Region();
        GridPane.setHgrow(spacer, Priority.ALWAYS);
        displayContentPane.add(spacer, 2, 0);

        displayPane.getChildren().addAll(displayContentPane);
        displayTab.setContent(displayPane);
        displayTab.setClosable(false);

        lockableElements.addAll(Arrays.asList(
                glslComboBox, directDrawRendererComboBox, offscreenRenderingModeComboBox,
                renderTargetModeLockComboBox, multisamplingComboBox, strictDrawOrderingComboBox,
                alwaysOffscreenComboBox, videoMemorySizeComboBox));

        return displayTab;
    }

    private void changeSetting(WinePrefixContainerDTO winePrefixContainerDTO, RegistryParameter newValue) {
        onChangeSetting.accept(winePrefixContainerDTO, newValue);
        lockAll();
    }


    private Tab drawInputTab(WinePrefixContainerDTO containerEntity) {
        final Tab inputTab = new Tab(translate("Input"));
        final VBox inputPane = new VBox();
        final Text title = new TextWithStyle(translate("Input settings"), TITLE_CSS_CLASS);

        inputPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        inputPane.getChildren().add(title);

        final GridPane inputContentPane = new GridPane();
        inputContentPane.getStyleClass().add("grid");

        final ComboBox<MouseWarpOverride> mouseWarpOverrideComboBox = new ComboBox<>();
        mouseWarpOverrideComboBox.setValue(containerEntity.getMouseWarpOverride());
        addItems(mouseWarpOverrideComboBox, MouseWarpOverride.class);
        inputContentPane.add(new TextWithStyle(translate("Mouse Warp Override"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        inputContentPane.add(mouseWarpOverrideComboBox, 1, 0);

        inputContentPane.getRowConstraints().addAll(
                new RowConstraints(50.)
        );

        inputContentPane.getColumnConstraints().addAll(
                new ColumnConstraintsWithPercentage(30),
                new ColumnConstraintsWithPercentage(70)
        );

        inputPane.getChildren().addAll(inputContentPane);
        inputTab.setContent(inputPane);
        inputTab.setClosable(false);

        lockableElements.add(mouseWarpOverrideComboBox);
        return inputTab;
    }


    private Tab drawWineToolsTab(WinePrefixContainerDTO containerEntity) {
        final Tab toolsTab = new Tab(translate("Wine tools"));
        final VBox toolsPane = new VBox();
        final Text title = new TextWithStyle(translate("Wine tools"), TITLE_CSS_CLASS);

        toolsPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        toolsPane.getChildren().add(title);

        final GridPane toolsContentPane = new GridPane();
        toolsContentPane.getStyleClass().add("grid");

        toolsContentPane.add(wineToolButton(translate("Configure Wine"), "winecfg.png",
                e -> this.onWineCfg.accept(containerEntity)), 0, 0);
        toolsContentPane.add(wineToolCaption(translate("Configure Wine")), 0, 1);

        toolsContentPane.add(wineToolButton(translate("Registry Editor"), "regedit.png",
                e -> this.onRegedit.accept(containerEntity)), 1, 0);
        toolsContentPane.add(wineToolCaption(translate("Registry Editor")), 1, 1);

        toolsContentPane.add(wineToolButton(translate("Windows reboot"), "rebootPrefix.png",
                e -> this.onWineboot.accept(containerEntity)), 2, 0);
        toolsContentPane.add(wineToolCaption(translate("Windows reboot")), 2, 1);

        toolsContentPane.add(wineToolButton(translate("Repair virtual drive"), "repair.png",
                e -> this.onWinebootRepair.accept(containerEntity)), 3, 0);
        toolsContentPane.add(wineToolCaption(translate("Repair virtual drive")), 3, 1);

        toolsContentPane.add(wineToolButton(translate("Command prompt"), "cmd.png",
                e -> this.onWineConsole.accept(containerEntity)), 0, 3);
        toolsContentPane.add(wineToolCaption(translate("Command prompt")), 0, 4);

        toolsContentPane.add(wineToolButton(translate("Task manager"), "taskmgr.png",
                e -> this.onTaskMgr.accept(containerEntity)), 1, 3);
        toolsContentPane.add(wineToolCaption(translate("Task manager")), 1, 4);

        toolsContentPane.add(wineToolButton(translate("Kill processes"), "killProcesses.png",
                e -> this.onKillProcess.accept(containerEntity), false), 2, 3);
        toolsContentPane.add(wineToolCaption(translate("Kill processes")), 2, 4);

        toolsContentPane.add(wineToolButton(translate("Wine uninstaller"), "uninstaller.png",
                e -> this.onUninstaller.accept(containerEntity)), 3, 3);
        toolsContentPane.add(wineToolCaption(translate("Wine uninstaller")), 3, 4);

        toolsPane.getChildren().addAll(toolsContentPane);

        toolsContentPane.getColumnConstraints().addAll(
                new ColumnConstraintsWithPercentage(25),
                new ColumnConstraintsWithPercentage(25),
                new ColumnConstraintsWithPercentage(25),
                new ColumnConstraintsWithPercentage(25)
        );

        toolsContentPane.getRowConstraints().addAll(
                new RowConstraints(96.),
                new RowConstraints(25.),
                new RowConstraints(30.),
                new RowConstraints(96.),
                new RowConstraints(25.)
        );


        toolsTab.setContent(toolsPane);
        toolsTab.setClosable(false);
        return toolsTab;
    }

    private Tab drawToolsTab(WinePrefixContainerDTO containerEntity) {
        final Tab toolsTab = new Tab(translate("Tools"));
        final VBox toolsPane = new VBox();
        final Text title = new TextWithStyle(translate("Tools"), TITLE_CSS_CLASS);

        toolsPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        toolsPane.getChildren().add(title);

        final GridPane toolsContentPane = new GridPane();
        toolsContentPane.getStyleClass().add("grid");

        toolsContentPane.add(wineToolButton(translate("Open a terminal"), "cmd.png",
                e -> this.onOpenTerminalInWinePrefix.accept(containerEntity)), 0, 0);
        toolsContentPane.add(wineToolCaption(translate("Open a terminal")), 0, 1);

        toolsPane.getChildren().addAll(toolsContentPane);

        toolsContentPane.getColumnConstraints().addAll(
                new ColumnConstraintsWithPercentage(25),
                new ColumnConstraintsWithPercentage(25),
                new ColumnConstraintsWithPercentage(25),
                new ColumnConstraintsWithPercentage(25)
        );

        toolsContentPane.getRowConstraints().addAll(
                new RowConstraints(96.),
                new RowConstraints(25.),
                new RowConstraints(30.),
                new RowConstraints(96.),
                new RowConstraints(25.)
        );


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

    private Button wineToolButton(String caption, String icon, EventHandler<? super MouseEvent> eventHandler) {
        return wineToolButton(caption, icon, eventHandler, true);
    }

    private Button wineToolButton(String caption, String icon, EventHandler<? super MouseEvent> eventHandler, boolean lockable) {
        final Button button = new Button(caption);
        button.getStyleClass().add("wineToolButton");
        final String iconResource = "icons/mainwindow/containers/" + icon;
        button.setStyle("-fx-background-image: url('" + themeManager.getResourceUrl(iconResource) + "');");
        button.setOnMouseClicked(event -> {
            lockAll();
            eventHandler.handle(event);
        });

        if (lockable) {
            lockableElements.add(button);
        }
        GridPane.setHalignment(button, HPos.CENTER);

        return button;
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
