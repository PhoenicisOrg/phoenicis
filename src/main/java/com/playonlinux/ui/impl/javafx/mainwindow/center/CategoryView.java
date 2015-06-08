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

package com.playonlinux.ui.impl.javafx.mainwindow.center;

import com.playonlinux.common.api.list.ObservableList;
import com.playonlinux.common.dto.ui.CenterCategoryDTO;
import com.playonlinux.common.dto.web.CategoryDTO;
import com.playonlinux.ui.impl.javafx.mainwindow.LeftBarTitle;
import com.playonlinux.ui.impl.javafx.mainwindow.LeftButton;
import com.playonlinux.ui.impl.javafx.mainwindow.LeftSpacer;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.playonlinux.domain.Localisation.translate;

public class CategoryView extends VBox implements Observer {

    private ObservableList<CenterCategoryDTO> categories;
    private List<CategorySelectionObserver> observers = new ArrayList<>();

    public CategoryView (ObservableList<CenterCategoryDTO> categoryList){
        this.categories = categoryList;
        categoryList.addObserver(this);
        this.update(null, null);
    }


    @Override
    public void update(Observable observable, Object o) {
        this.getChildren().clear();
        this.getChildren().addAll(new LeftBarTitle(translate("Category")));

        if(categories.size() > 0) {
            for (CenterCategoryDTO category : categories) {
                LeftButton categoryButton = new LeftButton(category.getIconName(), category.getName());
                this.getChildren().add(categoryButton);
                categoryButton.setOnMouseClicked(event -> {
                    this.fireCategorySelection(categoryButton.getName());
                });
            }
        }
    }


    public void addObserver(CategorySelectionObserver o){
        this.observers.add(o);
    }

    public void deleteObserver(CategorySelectionObserver o){
        this.observers.remove(o);
    }

    private void fireCategorySelection(String categoryName){
        for(CategorySelectionObserver o : observers){
            o.update(this, categoryName);
        }
    }


    public interface CategorySelectionObserver {

        void update(CategoryView categoryView, String category);

    }


}
