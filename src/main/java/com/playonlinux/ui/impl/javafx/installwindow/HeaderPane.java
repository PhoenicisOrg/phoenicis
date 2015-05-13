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

package com.playonlinux.ui.impl.javafx.installwindow;

import com.playonlinux.common.dtos.CategoryDTO;
import com.playonlinux.ui.api.RemoteAvailableInstallers;
import com.playonlinux.ui.impl.javafx.common.ClickableImageView;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public class HeaderPane extends GridPane implements Observer {
    InstallWindowEventHandler installWindowEventHandler;

    HeaderPane(InstallWindowEventHandler installWindowEventHandler) {
        this.setPrefHeight(70);
        this.setPrefWidth(804);
        this.setLayoutY(-2);
        this.setId("header");

        this.installWindowEventHandler = installWindowEventHandler;

        this.setUpEvents();
    }

    private void setUpEvents() {

    }

    @Override
    public void update(Observable o, Object arg) {
        RemoteAvailableInstallers remoteAvailableInstallers = (RemoteAvailableInstallers) arg;

        int iconSizeInPercent = 100 / remoteAvailableInstallers.getNumberOfCategories();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(iconSizeInPercent);
        this.getColumnConstraints().add(columnConstraints);

        for(CategoryDTO categoryDTO: remoteAvailableInstallers) {
            CategoryButton categoryIcon = new CategoryButton(categoryDTO.getName());
            this.getChildren().add(categoryIcon);
        }
    }

    class CategoryButton extends GridPane {

        private final String categoryName;
        private final ImageView categoryIcon;
        private final Text categoryNameText;

        CategoryButton(String categoryName) {
            this.setPrefHeight(64);
            this.setMaxHeight(64);

            this.setAlignment(Pos.CENTER);
            this.categoryName = categoryName;
            categoryIcon = new ClickableImageView(new Image(this.getIcon().toString()));
            categoryIcon.setFitHeight(32);
            categoryIcon.setFitWidth(32);
            categoryIcon.setLayoutY(10);
            categoryIcon.setLayoutX(16);

            categoryNameText = new Text(categoryName);
            categoryNameText.setLayoutY(53);
            categoryNameText.setFont(new Font(10));
            categoryNameText.setTextAlignment(TextAlignment.CENTER);

            RowConstraints rowConstraintIcon = new RowConstraints();
            rowConstraintIcon.setPercentHeight(70);
            RowConstraints rowConstraintText = new RowConstraints();
            rowConstraintText.setPercentHeight(20);

            this.getRowConstraints().addAll(rowConstraintIcon, rowConstraintText);

            this.add(categoryIcon, 0, 0);
            this.add(categoryNameText, 0, 1);

        }

        public void setMouseClicked(EventHandler<?super MouseEvent> mouseEventEventHandler) {
            this.categoryIcon.setOnMouseClicked(mouseEventEventHandler);
        }

        public URL getIcon() {
            return this.getClass().getResource("applications-"+categoryName.toLowerCase()+".png");
        }
    }
}
