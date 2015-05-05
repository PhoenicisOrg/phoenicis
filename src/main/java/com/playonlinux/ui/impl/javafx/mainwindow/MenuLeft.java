package com.playonlinux.ui.impl.javafx.mainwindow;

import javafx.application.Platform;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;

public class MenuLeft extends Accordion {
    public MenuLeft() {
        super();

        TitledPane mainTitledPane = new TitledPane();
        mainTitledPane.setText("Titre");
        mainTitledPane.setContent(new Button());

        TitledPane mainTitledPane2 = new TitledPane();
        mainTitledPane2.setText("Titre");
        mainTitledPane2.setContent(new Button());

        this.getPanes().add(mainTitledPane);
        this.getPanes().add(mainTitledPane2);

        this.setExpandedPane(mainTitledPane);

        this.expandedPaneProperty().addListener((property, oldPane, newPane) -> {
            if (oldPane != null) oldPane.setCollapsible(true);
            if (newPane != null) Platform.runLater(() -> newPane.setCollapsible(false));
        });
    }
}
