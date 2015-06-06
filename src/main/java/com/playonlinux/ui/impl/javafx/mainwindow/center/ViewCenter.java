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

package com.playonlinux.ui.impl.javafx.mainwindow.center;

import com.playonlinux.common.api.services.RemoteAvailableInstallers;
import com.playonlinux.common.dto.ui.CenterCategoryDTO;
import com.playonlinux.common.dto.ui.CenterItemDTO;
import com.playonlinux.ui.impl.javafx.common.SimpleIconListWidget;
import com.playonlinux.ui.impl.javafx.mainwindow.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.playonlinux.domain.Localisation.translate;

public class ViewCenter extends HBox implements Observer {
    private VBox failurePanel;
    private Button retryButton;
    private List<CenterCategoryDTO> categories;
    private List<CenterItemDTO> centerItems;
    private final SimpleIconListWidget availableInstallerListWidget;

    private final EventHandlerCenter eventHandlerCenter;

    private LeftSideBar leftContent;
    private String selectedCategory;
    private TextField searchBar;
    private HBox progressIndicatorPanel;

    public ViewCenter(MainWindow parent) {
        eventHandlerCenter = new EventHandlerCenter();
        this.getStyleClass().add("mainWindowScene");

        availableInstallerListWidget = new SimpleIconListWidget();
        availableInstallerListWidget.getStyleClass().add("rightPane");

        leftContent = new LeftSideBar();

        this.initWait();
        this.initFailure();

        this.drawSideBar();
        this.showWait();
    }

    private void initFailure() {
        failurePanel = new VBox();
        failurePanel.getStyleClass().add("rightPane");

        failurePanel.setSpacing(10);
        failurePanel.setAlignment(Pos.CENTER);

        Label failureNotificationLbl = new Label();
        failureNotificationLbl.setText(translate("Connecting to ${application.name} failed.\n" +
                "Please check your connection and try again."));
        failureNotificationLbl.setTextAlignment(TextAlignment.CENTER);

        ImageView retryImage = new ImageView(new Image(getClass().getResourceAsStream("refresh.png")));
        retryImage.setFitWidth(16);
        retryImage.setFitHeight(16);
        retryButton = new Button(translate("Retry"), retryImage);

        failurePanel.getChildren().addAll(failureNotificationLbl, retryButton);
    }

    private void initWait() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefWidth(64);
        progressIndicator.setPrefHeight(64);

        progressIndicatorPanel = new HBox();
        progressIndicatorPanel.getStyleClass().add("rightPane");

        progressIndicatorPanel.getChildren().add(progressIndicator);
        progressIndicatorPanel.setAlignment(Pos.BASELINE_CENTER);
    }


    private void drawSideBar() {
        this.getChildren().add(leftContent);
        this.drawSideBarContent();
    }

    private void removeRightItems() {
        this.getChildren().removeAll(availableInstallerListWidget, progressIndicatorPanel, failurePanel);
    }

    private void showContent() {
        this.removeRightItems();
        this.getChildren().add(availableInstallerListWidget);
        this.addApplicationsToList();
    }

    private void showWait() {
        this.removeRightItems();
        this.getChildren().add(progressIndicatorPanel);
    }

    private void showFailure() {
        this.removeRightItems();
        this.getChildren().add(failurePanel);
    }

    private void addApplicationsToList() {
        String searchFilter = searchBar.getText();
        availableInstallerListWidget.clear();

        if(centerItems != null) {
            if(!StringUtils.isBlank(searchFilter)) {
                for (CenterItemDTO centerItem : this.centerItems) {
                    if(centerItem.getName().toLowerCase().contains(searchFilter.toLowerCase())) {
                        availableInstallerListWidget.addItem(centerItem.getName());
                    }
                }
            } else {
                for (CenterItemDTO centerItem : this.centerItems) {
                    if(centerItem.getCategoryName().equals(this.selectedCategory)) {
                        availableInstallerListWidget.addItem(centerItem.getName());
                    }
                }
            }

        }
    }



    private void drawSideBarContent() {
        leftContent.getChildren().clear();
        searchBar = new TextField();
        searchBar.setOnKeyReleased(event -> addApplicationsToList());

        leftContent.getChildren().add(searchBar);
        if(categories != null && categories.size() > 0) {
            leftContent.getChildren().addAll(new LeftSpacer(), new LeftBarTitle("Category"));

            for (CenterCategoryDTO category : categories) {
                LeftButton categoryButton = new LeftButton(category.getIconName(), category.getName());
                leftContent.getChildren().add(categoryButton);
                categoryButton.setOnMouseClicked(event -> {
                    selectedCategory = categoryButton.getName();
                    clearSearch();
                    addApplicationsToList();
                });
            }
        }

        CheckBox testingCheck = new CheckBox(translate("Testing"));
        CheckBox noCdNeededCheck = new CheckBox(translate("No CD needed"));
        CheckBox commercialCheck = new CheckBox(translate("Commercial"));

        leftContent.getChildren().addAll(new LeftSpacer(), new LeftBarTitle("Filters"), testingCheck,
                noCdNeededCheck, commercialCheck);
    }


    public void setUpEvents() {
        eventHandlerCenter.getRemoteAvailableInstallers().addObserver(this);
        retryButton.setOnMouseClicked(event -> this.eventHandlerCenter.updateAvailableInstallers());
    }



    @Override
    public void update(Observable o, Object arg) {
        RemoteAvailableInstallers remoteAvailableInstallers = (RemoteAvailableInstallers) o;

        Platform.runLater(() -> update(remoteAvailableInstallers));
    }

    public void update(RemoteAvailableInstallers remoteAvailableInstallers) {
        if(remoteAvailableInstallers.isUpdating()) {
            this.showWait();
        } else if(remoteAvailableInstallers.hasFailed()) {
            this.showFailure();
        } else {
            this.showContent();
        }
        categories = remoteAvailableInstallers.getAllCategories();
        centerItems = remoteAvailableInstallers.getAllCenterItems();

        this.drawSideBarContent();
        this.addApplicationsToList();
    }

    public void clearSearch() {
        searchBar.clear();
    }
}
