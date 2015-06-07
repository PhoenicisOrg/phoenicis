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

package com.playonlinux.ui.impl.javafx.common;

import javafx.scene.CacheHint;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static com.playonlinux.domain.Localisation.translate;

public class SimpleIconListWidget
        extends TreeView<SimpleIconListWidget.SimpleIconListItem> {
    private final TreeItem<SimpleIconListItem> rootItem;
    private Image defaultIcon =
            new Image(SimpleIconListWidget.class.getResource("playonlinux32.png").toExternalForm());

    private final static Logger logger = Logger.getLogger(SimpleIconListWidget.class);

    public SimpleIconListWidget() {
        rootItem = new TreeItem<>();
        this.setRoot(rootItem);
        this.setShowRoot(false);
        this.setCache(true);
        this.setCacheHint(CacheHint.QUALITY);
    }

    public void addItem(String itemName) {
        ImageView defaultIconImageView = new ImageView(defaultIcon);
        defaultIconImageView.setFitHeight(16);
        defaultIconImageView.setFitWidth(16);
        addItem(itemName, defaultIconImageView);
    }

    private void addItem(String itemName, ImageView icon) {
        TreeItem<SimpleIconListItem> treeItem = new TreeItem<>(new SimpleIconListItem(itemName, icon));
        treeItem.setGraphic(icon);
        rootItem.getChildren().add(treeItem);
    }


    public void addItem(String itemName, File iconPath) {
        try {
            addItem(itemName, new URL("file://"+iconPath.getAbsolutePath()));
        } catch (MalformedURLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            String errorTitle = String.format(translate("Error while trying to load the icon %s."),
                    iconPath.getAbsolutePath());
            alert.setTitle(errorTitle);
            alert.setContentText(String.format("The error was: %s", e));
            logger.warn(errorTitle, e);
        }
    }

    public void addItem(String itemName, URL iconUrl) {
        ImageView icon = new ImageView(iconUrl.toExternalForm());
        addItem(itemName, icon);
    }

    public void addChangeListener(SimpleIconChangeListener simpleIconChangeListener) {
        getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        simpleIconChangeListener.changed(newValue.getValue().getValue()
                        );
                    } else {
                        simpleIconChangeListener.changed(null);
                    }
                });
    }

    public void clear() {
        rootItem.getChildren().clear();
    }

    public String getSelectedItemLabel() {
        TreeItem<SimpleIconListItem> item = this.getSelectionModel().getSelectedItem();
        return item.getValue().getValue();
    }

    protected class SimpleIconListItem extends GridPane {
        private String itemName;

        SimpleIconListItem(String itemName, ImageView iconImageView) {
            this.itemName = itemName;
            this.setPrefHeight(0.);

            SimpleIconListItemLabel simpleIconListItemLabel = new SimpleIconListItemLabel(itemName);

            this.add(iconImageView, 0, 0);
            this.add(simpleIconListItemLabel, 1, 0);
        }

        public String getValue() {
            return itemName;
        }


        private class SimpleIconListItemLabel extends Pane {
            SimpleIconListItemLabel(String itemName) {
                Text simpleIconListItemLabelText = new Text(itemName);
                simpleIconListItemLabelText.setLayoutX(10);
                simpleIconListItemLabelText.setLayoutY(12);
                this.getChildren().add(simpleIconListItemLabelText);
            }
        }
    }

    public interface SimpleIconChangeListener {
        void changed(String newValue);
    }
}

