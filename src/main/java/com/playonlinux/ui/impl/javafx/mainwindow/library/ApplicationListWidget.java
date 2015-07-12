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

package com.playonlinux.ui.impl.javafx.mainwindow.library;

import com.playonlinux.dto.ui.library.InstalledApplicationDTO;
import com.playonlinux.utils.filter.InstalledApplicationFilter;
import com.playonlinux.utils.observer.Observable;
import com.playonlinux.utils.observer.Observer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.List;

import static com.playonlinux.lang.Localisation.translate;

class ApplicationListWidget extends TreeView<ApplicationListWidget.ApplicationItem>  {

    private final TreeItem<ApplicationItem> rootItem;
    private final ViewLibrary parent;

    public ApplicationListWidget(ViewLibrary parent) {
        this.parent = parent;
        this.rootItem = new TreeItem<>();
        this.setRoot(rootItem);
        this.setShowRoot(false);

    }

    public void setItems(List<InstalledApplicationDTO> applicationDTOs) {
        this.clear();
        for(InstalledApplicationDTO applicationDTO: applicationDTOs) {
            this.addItem(applicationDTO.getName(), applicationDTO.getIcon());
        }
    }

    private void addItem(String shortcutName, URL iconPath) {
        TreeItem<ApplicationItem> treeItem = new TreeItem<>(new ApplicationItem(shortcutName, iconPath));
        rootItem.getChildren().add(treeItem);
    }

    private void clear() {
        rootItem.getChildren().clear();
    }

    protected class ApplicationItem extends GridPane {

        ApplicationItem(String applicationName, URL iconPath) {
            this.setPrefHeight(60.);
            Text applicationNameLabel = new Text(applicationName);

            ImageView iconImageView = new ImageView(new Image(iconPath.toExternalForm()));
            iconImageView.setFitHeight(40);
            iconImageView.setFitWidth(40);

            this.addConstraints();
            this.setPadding(new Insets(10, 10, 10, 0));
            this.add(applicationNameLabel, 1, 0);
            this.add(iconImageView, 0, 0);


            ImageView playImageView = new ImageView(this.getClass().getResource("play.png").toExternalForm());
            playImageView.setFitHeight(16);
            playImageView.setFitWidth(16);

            Button runButton = new Button(translate("Run"), playImageView);
            runButton.setOnMouseClicked(evt -> parent.getEventHandler().runApplication(applicationName));
            runButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.add(runButton, 2, 0);
        }



        private void addConstraints() {
            ColumnConstraints columnConstraint = new ColumnConstraints();
            columnConstraint.setPercentWidth(15);

            ColumnConstraints columnConstraint2 = new ColumnConstraints();
            columnConstraint2.setPercentWidth(75);

            ColumnConstraints columnConstraint3 = new ColumnConstraints();
            columnConstraint3.setPercentWidth(10);

            this.getColumnConstraints().add(columnConstraint);
            this.getColumnConstraints().add(columnConstraint2);
            this.getColumnConstraints().add(columnConstraint3);

        }
    }
}
