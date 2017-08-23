package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class SearchBox extends AnchorPane {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchBox.class);

    private TextField searchField;
    private Button clearButton;
    private Consumer<String> onSearch;

    public SearchBox(Consumer<String> onSearch, Runnable onClear) {
        super();

        if (onSearch != null) {
            this.onSearch = onSearch;
        } else {
            LOGGER.error("Search behavior not set, will do nothing (default).");
            this.onSearch = (searchText) -> {
            };
        }

        this.getStyleClass().add("searchBox");

        this.searchField = new TextField();
        this.searchField.getStyleClass().add("searchBar");
        this.searchField.prefHeightProperty().bind(this.prefHeightProperty());
        this.searchField.prefWidthProperty().bind(this.prefWidthProperty());
        this.searchField.textProperty().addListener(event -> this.onSearch.accept(getText()));

        AnchorPane.setLeftAnchor(searchField, 0.0);
        AnchorPane.setRightAnchor(searchField, 0.0);

        this.clearButton = new Button();
        this.clearButton.getStyleClass().add("searchCleanButton");
        this.clearButton.setOnMouseClicked(event -> {
            this.searchField.clear();
            onClear.run();
        });

        AnchorPane.setRightAnchor(clearButton, 0.0);

        this.getChildren().addAll(searchField, clearButton);
    }

    public StringProperty textProperty() {
        return this.searchField.textProperty();
    }

    public String getText() {
        return this.searchField.getText();
    }
}
