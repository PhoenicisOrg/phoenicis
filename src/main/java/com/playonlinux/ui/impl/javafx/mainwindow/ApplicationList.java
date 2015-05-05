package com.playonlinux.ui.impl.javafx.mainwindow;

import com.playonlinux.ui.dtos.ShortcutDTO;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.File;
import java.util.Observable;
import java.util.Observer;


public class ApplicationList extends TreeView implements Observer {
    private final TreeItem rootItem;

    public ApplicationList() {
        rootItem = new TreeItem();
        this.setRoot(rootItem);
        this.setShowRoot(false);
    }

    public void addItem(String shortcutName, File iconPath) {
        rootItem.getChildren().add(new TreeItem(new ApplicationItem(shortcutName, iconPath)));
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
        this.clear();
        Platform.runLater(() -> {
            Iterable<ShortcutDTO> installedApplications = (Iterable<ShortcutDTO>) o;
            for (ShortcutDTO shortcut : installedApplications) {
                addItem(shortcut.getName(), shortcut.getIcon());
            }
        });
    }

    private void clear() {
        rootItem.getChildren().clear();
    }

    private class ApplicationItem extends GridPane {
        private final String applicationName;
        private final File iconPath;

        ApplicationItem(String applicationName, File iconPath) {
            this.applicationName = applicationName;
            this.iconPath = iconPath;
            System.out.println(iconPath.getAbsolutePath());
            this.setPrefHeight(70.);
            Text applicationNameLabel = new Text(applicationName);

            this.add(applicationNameLabel, 1, 0);
            this.add(new ImageView(new Image("file:/"+iconPath.getAbsolutePath())), 0, 0);
        }
    }
}
