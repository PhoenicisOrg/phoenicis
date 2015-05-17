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
import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.ui.api.RemoteAvailableInstallers;
import com.playonlinux.ui.impl.javafx.common.ClickableImageView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public class HeaderPane extends GridPane implements Observer {
    private final RemoteAvailableInstallers availableInstallers;
    InstallWindowEventHandler eventHandler;

    HeaderPane(InstallWindowEventHandler installWindowEventHandler) throws PlayOnLinuxError {
        this.setPrefHeight(70);
        this.setPrefWidth(804);
        this.setLayoutY(-2);
        this.setId("header");

        this.eventHandler = installWindowEventHandler;
        this.availableInstallers = this.eventHandler.getRemoteAvailableInstallers();

        this.update(availableInstallers);
        this.setAlignment(Pos.CENTER);

        this.setUpEvents();
    }

    private void setUpEvents() {
        availableInstallers.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        RemoteAvailableInstallers remoteAvailableInstallers = (RemoteAvailableInstallers) o;

        Platform.runLater(() -> update(remoteAvailableInstallers));
    }

    public void update(RemoteAvailableInstallers remoteAvailableInstallers) {
        if(!remoteAvailableInstallers.isUpdating() && !remoteAvailableInstallers.hasFailed()) {
            int iconSizeInPercent = 100 / (remoteAvailableInstallers.getNumberOfCategories() - 1);


            int i = 0;
            this.getColumnConstraints().clear();
            this.getChildren().clear();
            for(CategoryDTO categoryDTO: remoteAvailableInstallers) {
                /* Functions should not be shown in this window */
                if(categoryDTO.getType() == CategoryDTO.CategoryType.INSTALLERS) {
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setPercentWidth(iconSizeInPercent);
                    columnConstraints.setHalignment(HPos.CENTER);
                    this.getColumnConstraints().add(columnConstraints);

                    CategoryButton categoryIcon = new CategoryButton(categoryDTO.getName());
                    categoryIcon.setMouseClicked((evt) -> eventHandler.selectCategory(categoryDTO.getName()));

                    this.add(categoryIcon, i, 0);
                    i++;
                }
            }

        }
    }

    class CategoryButton extends GridPane {

        private final String categoryName;
        private final ImageView categoryIcon;
        private final Text categoryNameText;

        CategoryButton(String categoryName) {
            this.setPrefHeight(62);
            this.setMaxHeight(62);

            this.setAlignment(Pos.CENTER);
            this.categoryName = categoryName;
            categoryIcon = new ClickableImageView(new Image(this.getIcon().toString()));
            categoryIcon.setFitWidth(32);
            categoryIcon.setFitHeight(32);

            categoryNameText = new Text(categoryName);
            categoryNameText.setLayoutY(53);
            categoryNameText.setFont(new Font(10));
            categoryNameText.setTextAlignment(TextAlignment.CENTER);

            RowConstraints rowConstraintIcon = new RowConstraints();
            rowConstraintIcon.setPercentHeight(70);
            rowConstraintIcon.setValignment(VPos.CENTER);

            RowConstraints rowConstraintText = new RowConstraints();
            rowConstraintText.setPercentHeight(20);
            rowConstraintText.setValignment(VPos.CENTER);


            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100);
            columnConstraints.setHalignment(HPos.CENTER);

            this.getRowConstraints().addAll(rowConstraintIcon, rowConstraintText);
            this.getColumnConstraints().add(columnConstraints);

            this.add(categoryIcon, 0, 0);
            this.add(categoryNameText, 0, 1);

        }

        public void setMouseClicked(EventHandler<?super MouseEvent> mouseEventEventHandler) {
            this.categoryIcon.setOnMouseClicked(mouseEventEventHandler);
            this.categoryNameText.setOnMouseClicked(mouseEventEventHandler);
        }

        public URL getIcon() {
            return this.getClass().getResource("applications-"+categoryName.toLowerCase()+".png");
        }
    }
}
