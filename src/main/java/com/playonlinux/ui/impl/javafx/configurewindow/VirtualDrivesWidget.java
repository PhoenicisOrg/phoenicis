package com.playonlinux.ui.impl.javafx.configurewindow;

import com.playonlinux.ui.dtos.VirtualDriveDTO;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.File;
import java.util.Observable;
import java.util.Observer;


public class VirtualDrivesWidget extends TreeView implements Observer {

    private final TreeItem rootItem;

    VirtualDrivesWidget() {
        rootItem = new TreeItem();
        this.setRoot(rootItem);
        this.setShowRoot(false);
    }

    public void addItem(String shortcutName, File iconPath) {
        TreeItem treeItem = new TreeItem(new VirtualDriveItem(shortcutName, iconPath));
        rootItem.getChildren().add(treeItem);
    }

    @Override
    public void update(Observable o, Object arg) {
        this.clear();
        Platform.runLater(() -> {
            Iterable<VirtualDriveDTO> virtualDrives = (Iterable<VirtualDriveDTO>) o;
            for (VirtualDriveDTO virtualDrive : virtualDrives) {
                addItem(virtualDrive.getName(), virtualDrive.getIcon());
            }
        });
    }

    private void clear() {
        rootItem.getChildren().clear();
    }

    private class VirtualDriveItem extends GridPane {
        VirtualDriveItem(String virtualDriveName, File iconPath) {
            this.setPrefHeight(0.);

            VirtualDriveLabel virtualDriveLabel = new VirtualDriveLabel(virtualDriveName);

            ImageView iconImageView = new ImageView(new Image("file://"+iconPath.getAbsolutePath()));
            iconImageView.setFitHeight(16);
            iconImageView.setFitWidth(16);

            this.add(iconImageView, 0, 0);
            this.add(virtualDriveLabel, 1, 0);
        }


        private class VirtualDriveLabel extends Pane {
            VirtualDriveLabel(String virtualDriveName) {
                Text virtualDriveLabelText = new Text(virtualDriveName);
                virtualDriveLabelText.setLayoutX(10);
                virtualDriveLabelText.setLayoutY(12);
                this.getChildren().add(virtualDriveLabelText);
            }
        }
    }
}
