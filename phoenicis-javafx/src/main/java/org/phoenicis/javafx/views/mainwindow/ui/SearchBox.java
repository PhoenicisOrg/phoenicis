package org.phoenicis.javafx.views.mainwindow.ui;

import org.phoenicis.javafx.views.common.ThemeManager;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class SearchBox extends AnchorPane {
    private TextField searchField;
    private Button clearButton;

    private static final String cleanButtonIcon = "icons/mainwindow/general/edit-clear.png";

    public SearchBox(ThemeManager themeManager) {
        this.searchField = new TextField();

        this.searchField.getStyleClass().add("searchBar");
        this.searchField.prefHeightProperty().bind(this.prefHeightProperty());
        this.searchField.prefWidthProperty().bind(this.prefWidthProperty());

        AnchorPane.setLeftAnchor(searchField, 0.0);
        AnchorPane.setRightAnchor(searchField, 0.0);

        this.clearButton = new Button();
        this.clearButton.getStyleClass().add("searchCleanButton");
        this.clearButton.setOnMouseClicked(event -> this.searchField.clear());

        AnchorPane.setRightAnchor(clearButton, 0.0);

        this.getStyleClass().add("searchBox");
        this.getChildren().addAll(searchField, clearButton);
    }

    public StringProperty textProperty() {
        return this.searchField.textProperty();
    }

    public String getText() {
        return this.searchField.getText();
    }
}
