/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.javafx.containers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import com.playonlinux.containers.entities.WinePrefixContainerEntity;
import com.playonlinux.javafx.common.ColumnConstraintsWithPercentage;
import com.playonlinux.javafx.common.TextWithStyle;
import com.playonlinux.javafx.mainwindow.containers.ContainerConfigurationView;
import com.playonlinux.wine.parameters.AlwaysOffscreen;
import com.playonlinux.wine.parameters.DirectDrawRenderer;
import com.playonlinux.wine.parameters.GLSL;
import com.playonlinux.wine.parameters.MouseWarpOverride;
import com.playonlinux.wine.parameters.Multisampling;
import com.playonlinux.wine.parameters.OffscreenRenderingMode;
import com.playonlinux.wine.parameters.RenderTargetModeLock;
import com.playonlinux.wine.parameters.StrictDrawOrdering;
import com.playonlinux.wine.parameters.VideoMemorySize;
import com.sun.javafx.collections.ImmutableObservableList;
import com.sun.javafx.collections.ObservableListWrapper;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static com.playonlinux.core.lang.Localisation.translate;

public class WinePrefixContainerConfigurationView extends ContainerConfigurationView<WinePrefixContainerEntity> {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";


    private final List<Node> lockableElements = new ArrayList<>();
    private final WinePrefixEventHandler winePrefixEventHandler;

    public WinePrefixContainerConfigurationView(WinePrefixContainerEntity containerEntity,
                                                WinePrefixEventHandler winePrefixEventHandler) {
        super(containerEntity);
        this.getTabs().add(drawDisplayTab(containerEntity));
        this.getTabs().add(drawInputTab(containerEntity));
        this.getTabs().add(drawToolsTab(containerEntity));
        this.winePrefixEventHandler = winePrefixEventHandler;
    }



    @Override
    protected Tab drawInformationTab(WinePrefixContainerEntity containerEntity) {
        final Tab informationTab = new Tab(translate("Information"));
        final VBox informationPane = new VBox();
        final Text title = new TextWithStyle(translate("Information"), TITLE_CSS_CLASS);

        informationPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        informationPane.getChildren().add(title);

        final GridPane informationContentPane = new GridPane();
        informationContentPane.getStyleClass().add("grid");

        informationContentPane.add(new TextWithStyle(translate("Name:"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        informationContentPane.add(new Text(containerEntity.getName()), 1, 0);

        informationContentPane.add(new TextWithStyle(translate("Path:"), CAPTION_TITLE_CSS_CLASS), 0, 1);
        informationContentPane.add(new Text(containerEntity.getPath()), 1, 1);

        informationContentPane.add(new TextWithStyle(translate("Wine version:"), CAPTION_TITLE_CSS_CLASS), 0, 2);
        informationContentPane.add(new Text(containerEntity.getWineVersion()), 1, 2);

        informationContentPane.add(new TextWithStyle(translate("Wine architecture:"), CAPTION_TITLE_CSS_CLASS), 0, 3);
        informationContentPane.add(new Text(containerEntity.getWineArchitecture()), 1, 3);

        informationContentPane.add(new TextWithStyle(translate("Wine distribution:"), CAPTION_TITLE_CSS_CLASS), 0, 4);
        informationContentPane.add(new Text(containerEntity.getWineDistribution()), 1, 4);

        informationContentPane.getRowConstraints().addAll(
                new RowConstraints(20.),
                new RowConstraints(20.),
                new RowConstraints(20.),
                new RowConstraints(20.),
                new RowConstraints(20.)
        );

        informationContentPane.getColumnConstraints().addAll(
                new ColumnConstraintsWithPercentage(20),
                new ColumnConstraintsWithPercentage(80)
        );

        informationPane.getChildren().addAll(informationContentPane);
        informationTab.setContent(informationPane);
        informationTab.setClosable(false);
        return informationTab;
    }


    protected Tab drawDisplayTab(WinePrefixContainerEntity containerEntity) {
        final Tab displayTab = new Tab(translate("Display"));
        final VBox displayPane = new VBox();
        final Text title = new TextWithStyle(translate("Display settings"), TITLE_CSS_CLASS);

        displayPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        displayPane.getChildren().add(title);

        final GridPane displayContentPane = new GridPane();
        displayContentPane.getStyleClass().add("grid");

        final ComboBox<GLSL> glslComboBox = new ComboBox<>();
        glslComboBox.setValue(containerEntity.getGlslValue());
        addItems(glslComboBox, GLSL.class);
        displayContentPane.add(new TextWithStyle(translate("GLSL support"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        displayContentPane.add(glslComboBox, 1, 0);

        final ComboBox<DirectDrawRenderer> directDrawRendererComboBox = new ComboBox<>();
        directDrawRendererComboBox.setValue(containerEntity.getDirectDrawRenderer());
        addItems(directDrawRendererComboBox, DirectDrawRenderer.class);
        displayContentPane.add(new TextWithStyle(translate("Direct Draw Renderer"), CAPTION_TITLE_CSS_CLASS), 0, 1);
        displayContentPane.add(directDrawRendererComboBox, 1, 1);

        final ComboBox<VideoMemorySize> videoMemorySizeComboBox = new ComboBox<>();
        videoMemorySizeComboBox.setValue(containerEntity.getVideoMemorySize());
        addItemsVideoMemorySize(videoMemorySizeComboBox);
        displayContentPane.add(new TextWithStyle(translate("Video memory size"), CAPTION_TITLE_CSS_CLASS), 0, 2);
        displayContentPane.add(videoMemorySizeComboBox, 1, 2);

        final ComboBox<OffscreenRenderingMode> offscreenRenderingModeComboBox = new ComboBox<>();
        offscreenRenderingModeComboBox.setValue(containerEntity.getOffscreenRenderingMode());
        addItems(offscreenRenderingModeComboBox, OffscreenRenderingMode.class);
        displayContentPane.add(new TextWithStyle(translate("Offscreen rendering mode"), CAPTION_TITLE_CSS_CLASS), 0, 3);
        displayContentPane.add(offscreenRenderingModeComboBox, 1, 3);

        final ComboBox<RenderTargetModeLock> renderTargetModeLockComboBox = new ComboBox<>();
        renderTargetModeLockComboBox.setValue(containerEntity.getRenderTargetModeLock());
        displayContentPane.add(new TextWithStyle(translate("Render target lock mode"), CAPTION_TITLE_CSS_CLASS), 0, 4);
        displayContentPane.add(renderTargetModeLockComboBox, 1, 4);

        final ComboBox<Multisampling> multisamplingComboBox = new ComboBox<>();
        multisamplingComboBox.setValue(containerEntity.getMultisampling());
        addItems(multisamplingComboBox, Multisampling.class);
        displayContentPane.add(new TextWithStyle(translate("Multisampling"), CAPTION_TITLE_CSS_CLASS), 0, 5);
        displayContentPane.add(multisamplingComboBox, 1, 5);

        final ComboBox<StrictDrawOrdering> strictDrawOrderingComboBox  = new ComboBox<>();
        strictDrawOrderingComboBox.setValue(containerEntity.getStrictDrawOrdering());
        addItems(strictDrawOrderingComboBox, StrictDrawOrdering.class);
        displayContentPane.add(new TextWithStyle(translate("Strict Draw Ordering"), CAPTION_TITLE_CSS_CLASS), 0, 6);
        displayContentPane.add(strictDrawOrderingComboBox, 1, 6);

        final ComboBox<AlwaysOffscreen> alwaysOffscreenComboBox  = new ComboBox<>();
        alwaysOffscreenComboBox.setValue(containerEntity.getAlwaysOffscreen());
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

        displayContentPane.getColumnConstraints().addAll(
                new ColumnConstraintsWithPercentage(30),
                new ColumnConstraintsWithPercentage(70)
        );

        displayPane.getChildren().addAll(displayContentPane);
        displayTab.setContent(displayPane);
        displayTab.setClosable(false);

        lockableElements.addAll(Arrays.asList(
                glslComboBox, directDrawRendererComboBox, offscreenRenderingModeComboBox,
                renderTargetModeLockComboBox, multisamplingComboBox, strictDrawOrderingComboBox,
                alwaysOffscreenComboBox, videoMemorySizeComboBox));

        return displayTab;
    }


    protected Tab drawInputTab(WinePrefixContainerEntity containerEntity) {
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


    protected Tab drawToolsTab(WinePrefixContainerEntity containerEntity) {
        final Tab toolsTab = new Tab(translate("Tools"));
        final VBox toolsPane = new VBox();
        final Text title = new TextWithStyle(translate("Wine tools"), TITLE_CSS_CLASS);

        toolsPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        toolsPane.getChildren().add(title);

        final GridPane toolsContentPane = new GridPane();
        toolsContentPane.getStyleClass().add("grid");

        toolsContentPane.add(wineToolButton(translate("Configure Wine"), "winecfg.png",
                e -> winePrefixEventHandler.wineContainerActions().runWinecfg(this.getMiniWizard(), containerEntity.getWinePrefixDirectory(), arg -> {
                    unlockAll();
                })), 0, 0);

        toolsContentPane.add(wineToolCaption(translate("Configure Wine")), 0, 1);

        toolsContentPane.add(wineToolButton(translate("Registry Editor"), "regedit.png", null), 1, 0);
        toolsContentPane.add(wineToolCaption(translate("Registry Editor")), 1, 1);

        toolsContentPane.add(wineToolButton(translate("Windows reboot"), "rebootPrefix.png", null), 2, 0);
        toolsContentPane.add(wineToolCaption(translate("Windows reboot")), 2, 1);

        toolsContentPane.add(wineToolButton(translate("Repair virtual drive"), "repair.png", null), 3, 0);
        toolsContentPane.add(wineToolCaption(translate("Repair virtual drive")), 3, 1);

        toolsContentPane.add(wineToolButton(translate("Command prompt"), "cmd.png", null), 0, 3);
        toolsContentPane.add(wineToolCaption(translate("Command prompt")), 0, 4);

        toolsContentPane.add(wineToolButton(translate("Task manager"), "taskmgr.png", null), 1, 3);
        toolsContentPane.add(wineToolCaption(translate("Task manager")), 1, 4);

        toolsContentPane.add(wineToolButton(translate("Kill processes"), "killProcesses.png", null), 2, 3);
        toolsContentPane.add(wineToolCaption(translate("Kill processes")), 2, 4);

        toolsContentPane.add(wineToolButton(translate("Wine uninstaller"), "uninstaller.png", null), 3, 3);
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

    private Text wineToolCaption(String caption) {
        final Text text = new TextWithStyle(caption, "wineToolCaption");
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setValignment(text, VPos.CENTER);
        lockableElements.add(text);
        return text;
    }

    private Button wineToolButton(String caption, String imageName, EventHandler<? super MouseEvent> eventHandler) {
        final Button button = new Button(caption,
                new ImageView(
                        new Image(this.getClass().getResourceAsStream(imageName), 48., 48., true, true)
                )
        );
        button.getStyleClass().addAll("wineToolButton");
        button.setOnMouseClicked(event -> {
            lockAll();
            eventHandler.handle(event);
        });
        lockableElements.add(button);
        GridPane.setHalignment(button, HPos.CENTER);

        return button;
    }

    private void unlockAll() {
        for(Node element: lockableElements) {
            element.setDisable(false);
        }
    }

    private void lockAll() {
        for(Node element: lockableElements) {
            element.setDisable(true);
        }
    }

    private void addItemsVideoMemorySize(ComboBox<VideoMemorySize> videoMemorySizeComboBox) {
        videoMemorySizeComboBox.setItems(new ImmutableObservableList<>(VideoMemorySize.possibleValues()));
    }

    private <T extends Enum> void addItems(ComboBox<T> comboBox , Class<T> clazz) {
        final List<T> possibleValues = new ArrayList<>(EnumSet.allOf(clazz));

        final ObservableList<T> possibleValuesObservable = new ObservableListWrapper<>(possibleValues);
        comboBox.setItems(possibleValuesObservable);
    }


}


