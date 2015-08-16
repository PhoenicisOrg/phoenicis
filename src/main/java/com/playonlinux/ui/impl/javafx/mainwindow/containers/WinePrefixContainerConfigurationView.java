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

package com.playonlinux.ui.impl.javafx.mainwindow.containers;

import com.playonlinux.containers.entities.WinePrefixContainerEntity;
import com.playonlinux.ui.impl.javafx.common.ColumnConstraintsWithPercentage;
import com.playonlinux.ui.impl.javafx.common.TextWithStyle;
import com.playonlinux.wine.parameters.*;
import com.sun.javafx.collections.ImmutableObservableList;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.playonlinux.core.lang.Localisation.translate;


public class WinePrefixContainerConfigurationView extends ContainerConfigurationView<WinePrefixContainerEntity> {
    public WinePrefixContainerConfigurationView(WinePrefixContainerEntity containerEntity) {
        super(containerEntity);
        this.getTabs().add(drawDisplayTab(containerEntity));
    }

    @Override
    protected Tab drawInformationTab(WinePrefixContainerEntity containerEntity) {
        final Tab informationTab = new Tab(translate("Information"));
        final VBox informationPane = new VBox();
        final Text title = new TextWithStyle(translate("Information"), "title");

        informationPane.getStyleClass().add("containerConfigurationPane");
        informationPane.getChildren().add(title);

        final GridPane informationContentPane = new GridPane();
        informationContentPane.getStyleClass().add("grid");

        informationContentPane.add(new TextWithStyle(translate("Name:"), "captionTitle"), 0, 0);
        informationContentPane.add(new Text(containerEntity.getName()), 1, 0);

        informationContentPane.add(new TextWithStyle(translate("Path:"), "captionTitle"), 0, 1);
        informationContentPane.add(new Text(containerEntity.getPath()), 1, 1);

        informationContentPane.add(new TextWithStyle(translate("Wine version:"), "captionTitle"), 0, 2);
        informationContentPane.add(new Text(containerEntity.getWineVersion()), 1, 2);

        informationContentPane.add(new TextWithStyle(translate("Wine architecture:"), "captionTitle"), 0, 3);
        informationContentPane.add(new Text(containerEntity.getWineArchitecture()), 1, 3);

        informationContentPane.add(new TextWithStyle(translate("Wine distribution:"), "captionTitle"), 0, 4);
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
        final Text title = new TextWithStyle(translate("Display settings"), "title");

        displayPane.getStyleClass().add("containerConfigurationPane");
        displayPane.getChildren().add(title);

        final GridPane displayContentPane = new GridPane();
        displayContentPane.getStyleClass().add("grid");

        final ComboBox<GLSL> glslComboBox = new ComboBox<>();
        glslComboBox.setValue(containerEntity.getGlslValue());
        addItems(glslComboBox, GLSL.class);
        displayContentPane.add(new TextWithStyle(translate("GLSL support"), "captionTitle"), 0, 0);
        displayContentPane.add(glslComboBox, 1, 0);

        final ComboBox<DirectDrawRenderer> directDrawRendererComboBox = new ComboBox<>();
        directDrawRendererComboBox.setValue(containerEntity.getDirectDrawRenderer());
        addItems(directDrawRendererComboBox, DirectDrawRenderer.class);
        displayContentPane.add(new TextWithStyle(translate("Direct Draw Renderer"), "captionTitle"), 0, 1);
        displayContentPane.add(directDrawRendererComboBox, 1, 1);

        final ComboBox<VideoMemorySize> videoMemorySizeComboBox = new ComboBox<>();
        videoMemorySizeComboBox.setValue(containerEntity.getVideoMemorySize());
        addItemsVideoMemorySize(videoMemorySizeComboBox);
        displayContentPane.add(new TextWithStyle(translate("Video memory size"), "captionTitle"), 0, 2);
        displayContentPane.add(videoMemorySizeComboBox, 1, 2);

        final ComboBox<OffscreenRenderingMode> offscreenRenderingModeComboBox = new ComboBox<>();
        offscreenRenderingModeComboBox.setValue(containerEntity.getOffscreenRenderingMode());
        addItems(offscreenRenderingModeComboBox, OffscreenRenderingMode.class);
        displayContentPane.add(new TextWithStyle(translate("Offscreen rendering mode"), "captionTitle"), 0, 3);
        displayContentPane.add(offscreenRenderingModeComboBox, 1, 3);

        final ComboBox<RenderTargetModeLock> renderTargetModeLockComboBox = new ComboBox<>();
        renderTargetModeLockComboBox.setValue(containerEntity.getRenderTargetModeLock());
        displayContentPane.add(new TextWithStyle(translate("Render target lock mode"), "captionTitle"), 0, 4);
        displayContentPane.add(renderTargetModeLockComboBox, 1, 4);

        final ComboBox<Multisampling> multisamplingComboBox = new ComboBox<>();
        multisamplingComboBox.setValue(containerEntity.getMultisampling());
        addItems(multisamplingComboBox, Multisampling.class);
        displayContentPane.add(new TextWithStyle(translate("Multisampling"), "captionTitle"), 0, 5);
        displayContentPane.add(multisamplingComboBox, 1, 5);

        final ComboBox<StrictDrawOrdering> strictDrawOrderingComboBox  = new ComboBox<>();
        strictDrawOrderingComboBox.setValue(containerEntity.getStrictDrawOrdering());
        addItems(strictDrawOrderingComboBox, StrictDrawOrdering.class);
        displayContentPane.add(new TextWithStyle(translate("Strict Draw Ordering"), "captionTitle"), 0, 6);
        displayContentPane.add(strictDrawOrderingComboBox, 1, 6);

        final ComboBox<AlwaysOffscreen> alwaysOffscreenComboBox  = new ComboBox<>();
        alwaysOffscreenComboBox.setValue(containerEntity.getAlwaysOffscreen());
        addItems(alwaysOffscreenComboBox, AlwaysOffscreen.class);
        displayContentPane.add(new TextWithStyle(translate("Always Offscreen"), "captionTitle"), 0, 7);
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
        return displayTab;
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


