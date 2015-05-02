package com.playonlinux.ui.impl.javafx.mainwindow;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Observable;
import java.util.Observer;

public class ApplicationList extends TreeView implements Observer {
    private final TreeItem rootItem;

    public ApplicationList() {
        rootItem = new TreeItem();
        this.setRoot(rootItem);
        this.setShowRoot(false);
    }

    public void addItem() {
        rootItem.getChildren().add(new TreeItem(new ApplicationItem("Test", "test")));
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO
        //

    }

    private class ApplicationItem extends GridPane {
        private final String applicationName;
        private final String prefixName;

        ApplicationItem(String applicationName, String prefixName) {
            this.applicationName = applicationName;
            this.prefixName = prefixName;

            this.setPrefHeight(100.);
            Text applicationNameLabel = new Text(applicationName);
            Text prefixNameLabel = new Text(prefixName);
            this.add(applicationNameLabel, 0, 0);
            this.add(prefixNameLabel, 0, 1);
        }
    }
}
