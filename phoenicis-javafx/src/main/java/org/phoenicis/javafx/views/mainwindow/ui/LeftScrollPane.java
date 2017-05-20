package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * Created by marc on 19.05.17.
 */
public class LeftScrollPane extends ScrollPane {
    private VBox content;

    public LeftScrollPane(Node... nodes) {
        this();

        this.content.getChildren().setAll(nodes);
    }

    public LeftScrollPane() {
        super();

        this.content = new VBox();

        this.setContent(content);

        this.getStyleClass().add("leftPaneScrollbar");
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    public void setAll(Node... nodes) {
        this.content.getChildren().setAll(nodes);
    }

    public void addAll(Node... nodes) {
        this.content.getChildren().addAll(nodes);
    }
}
