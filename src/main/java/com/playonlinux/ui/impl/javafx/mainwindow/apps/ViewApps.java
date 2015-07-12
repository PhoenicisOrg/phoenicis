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

package com.playonlinux.ui.impl.javafx.mainwindow.apps;

import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.services.availableapplications.RemoteAvailableInstallers;
import com.playonlinux.dto.ui.CenterCategoryDTO;
import com.playonlinux.dto.ui.AppsItemDTO;
import com.playonlinux.utils.filter.CenterItemFilter;
import com.playonlinux.utils.list.FilterPromise;
import com.playonlinux.collections.ObservableArrayList;
import com.playonlinux.ui.impl.javafx.widget.MiniatureListWidget;
import com.playonlinux.ui.impl.javafx.mainwindow.LeftBarTitle;
import com.playonlinux.ui.impl.javafx.mainwindow.LeftSideBar;
import com.playonlinux.ui.impl.javafx.mainwindow.LeftSpacer;
import com.playonlinux.ui.impl.javafx.mainwindow.MainWindow;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.Observer;

import static com.playonlinux.lang.Localisation.translate;

final public class ViewApps extends HBox implements Observer {
    private static final Logger LOGGER = Logger.getLogger(ViewApps.class);
    private FailurePanel failurePanel;
    private ObservableArrayList<CenterCategoryDTO> categories;
    private FilterPromise<AppsItemDTO> centerItems;
    private final MiniatureListWidget availableInstallerListWidget;

    private final EventHandlerApps eventHandlerApps;
    private final CenterItemFilter filter = new CenterItemFilter();

    private LeftSideBar leftContent;
    private TextField searchBar;
    private CategoryView categoryView;
    private HBox waitPanel;

    private Node visiblePane;

    public ViewApps(MainWindow parent) {
        eventHandlerApps = new EventHandlerApps();
        this.getStyleClass().add("mainWindowScene");

        availableInstallerListWidget = MiniatureListWidget.create();

        leftContent = new LeftSideBar();

        this.initWait();
        this.initFailure();

        categories = new ObservableArrayList<>();
        try {
            centerItems = new FilterPromise<>(eventHandlerApps.getRemoteAvailableInstallers(), this.filter);
        } catch (PlayOnLinuxException e) {
            LOGGER.error(e);
        }

        this.drawSideBar();
        this.showWait();
    }

    private void initFailure() {
        failurePanel = new FailurePanel();
    }

    private void initWait() {
        waitPanel = new WaitPanel();
    }


    private void drawSideBar() {
        searchBar = new TextField();
        searchBar.setOnKeyReleased(event -> filter.setTitle(searchBar.getText()));

        categoryView = new CategoryView(categories);
        categoryView.addObserver((cView, category) -> {
            filter.startTransaction();
            filter.setCategory(category);
            filter.setTitle("");
            filter.endTransaction(true);
        });

        CheckBox testingCheck = new CheckBox(translate("Testing"));
        testingCheck.setOnMouseReleased(actionEvent -> filter.setShowTesting(testingCheck.isSelected()));
        CheckBox noCdNeededCheck = new CheckBox(translate("No CD needed"));
        noCdNeededCheck.setOnMouseReleased(actionEvent -> filter.setShowTesting(noCdNeededCheck.isSelected()));
        CheckBox commercialCheck = new CheckBox(translate("Commercial"));
        commercialCheck.setOnMouseReleased(actionEvent -> filter.setShowTesting(commercialCheck.isSelected()));

        leftContent.getChildren().addAll(searchBar, new LeftSpacer(), categoryView, new LeftSpacer(), new LeftBarTitle("Filters"),
                testingCheck, noCdNeededCheck, commercialCheck);

        this.getChildren().add(leftContent);
    }


    private void showContent() {
        show(availableInstallerListWidget);
    }

    private void showWait() {
        show(waitPanel);
    }

    private void showFailure() {
        show(failurePanel);
    }

    private void showAppDetails(AppsItemDTO item) {
        show(new AppPanel(eventHandlerApps, item));
    }

    private void show(Node nodeToShow) {
        if(visiblePane != null) {
            this.getChildren().remove(visiblePane);
        }
        this.visiblePane = nodeToShow;
        this.getChildren().add(visiblePane);
    }

    private void addApplicationsToList() {
        availableInstallerListWidget.clear();

        if(centerItems != null) {
            for(AppsItemDTO item : centerItems){
                Node itemNode = availableInstallerListWidget.addItem(item.getName());
                itemNode.setOnMouseClicked((evt) -> showAppDetails(item));
            }
        }
    }



    public void setUpEvents() {
        centerItems.addObserver(this);
        failurePanel.getRetryButton().setOnMouseClicked(event -> {
            try {
                this.eventHandlerApps.updateAvailableInstallers();
            } catch (PlayOnLinuxException e) {
                LOGGER.warn(e);
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        RemoteAvailableInstallers remoteAvailableInstallers = (RemoteAvailableInstallers) centerItems.getSource();
        Platform.runLater(() -> {
            if (StringUtils.isBlank(filter.getTitle())) {
                searchBar.setText("");
            }
            update(remoteAvailableInstallers);
        });
    }

    private void update(RemoteAvailableInstallers remoteAvailableInstallers) {
        if(remoteAvailableInstallers.isUpdating()) {
            this.showWait();
        } else if(remoteAvailableInstallers.hasFailed()) {
            this.showFailure();
        } else {
            this.showContent();
            categories.swapContents(remoteAvailableInstallers.getCategories());
            this.addApplicationsToList();
        }
    }

}
