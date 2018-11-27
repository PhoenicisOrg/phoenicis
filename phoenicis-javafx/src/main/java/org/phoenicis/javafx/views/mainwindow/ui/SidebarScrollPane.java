package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * Created by marc on 19.05.17.
 */
public class SidebarScrollPane extends ScrollPane {
    private VBox content;

    public SidebarScrollPane(Node... nodes) {
        this();

        this.content.getChildren().setAll(nodes);
    }

    public SidebarScrollPane() {
        super();

        this.content = new VBox();

        this.setContent(content);

        this.getStyleClass().add("sidebarScrollbar");
    }

    public void setAll(Node... nodes) {
        this.content.getChildren().setAll(nodes);
    }

    public void addAll(Node... nodes) {
        this.content.getChildren().addAll(nodes);
    }
}
