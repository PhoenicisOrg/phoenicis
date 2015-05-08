package com.playonlinux.ui.impl.javafx.mainwindow;

import com.playonlinux.ui.dtos.ShortcutDTO;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import static com.playonlinux.domain.Localisation.translate;


public class ApplicationListWidget extends TreeView implements Observer {
    private final TreeItem rootItem;

    public ApplicationListWidget() {
        rootItem = new TreeItem();
        this.setRoot(rootItem);
        this.setShowRoot(false);
    }

    public void addItem(String shortcutName, File iconPath) {
        TreeItem treeItem = new TreeItem(new ApplicationItem(shortcutName, iconPath));
        rootItem.getChildren().add(treeItem);
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
            this.setPrefHeight(60.);
            Text applicationNameLabel = new Text(applicationName);

            ImageView iconImageView = new ImageView(new Image("file://"+iconPath.getAbsolutePath()));
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
