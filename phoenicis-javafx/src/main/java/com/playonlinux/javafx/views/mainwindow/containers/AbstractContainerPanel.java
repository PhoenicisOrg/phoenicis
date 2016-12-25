package com.playonlinux.javafx.views.mainwindow.containers;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.util.List;

abstract class AbstractContainerPanel<T> extends VBox {
    private final TabPane tabPane;

    AbstractContainerPanel(T containerEntity) {
        this.tabPane = new TabPane();

        this.getChildren().add(tabPane);

        this.getStyleClass().add("rightPane");
        this.tabPane.getTabs().add(drawInformationTab(containerEntity));
    }

    abstract Tab drawInformationTab(T container);

    public List<Tab> getTabs() {
        return tabPane.getTabs();
    }


}
