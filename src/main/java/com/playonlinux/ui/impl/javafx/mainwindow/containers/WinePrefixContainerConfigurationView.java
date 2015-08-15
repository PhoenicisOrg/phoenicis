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
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static com.playonlinux.core.lang.Localisation.translate;


public class WinePrefixContainerConfigurationView extends ContainerConfigurationView<WinePrefixContainerEntity> {
    public WinePrefixContainerConfigurationView(WinePrefixContainerEntity containerEntity) {
        super(containerEntity);
    }

    @Override
    protected Tab drawInformationTab(WinePrefixContainerEntity containerEntity) {
        final Tab informationTab = new Tab(translate("Information"));
        final VBox informationPane = new VBox();
        final Text title = new Text(translate("Information"));
        title.getStyleClass().add("title");

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
        return informationTab;
    }
}


