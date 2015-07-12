/*
 * Copyright (C) 2015 Markus Ebner
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

package com.playonlinux.ui.impl.javafx.mainwindow.apps;

import com.playonlinux.dto.ui.apps.AppsCategoryDTO;
import com.playonlinux.ui.impl.javafx.mainwindow.LeftBarTitle;
import com.playonlinux.ui.impl.javafx.mainwindow.LeftButton;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.playonlinux.lang.Localisation.translate;

final class CategoryView extends VBox {

    private final ViewApps parent;

    public CategoryView(ViewApps parent) {
        this.parent = parent;
        this.getStyleClass().add("leftPaneInside");
    }

    public void addCategories(List<AppsCategoryDTO> categories) {
        this.getChildren().clear();
        this.getChildren().addAll(new LeftBarTitle(translate("Category")));

        if (!categories.isEmpty()) {
            for (AppsCategoryDTO category : categories) {
                LeftButton categoryButton = new LeftButton(category.getIconName(), category.getName());
                categoryButton.getStyleClass().add("leftPaneButtons");
                this.getChildren().add(categoryButton);
                categoryButton.setOnMouseClicked(event -> parent.selectCategory(category));
            }
        }
    }



}
