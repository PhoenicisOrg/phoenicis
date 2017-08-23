package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A search box component containing a textfield to enter a search term and a clear button
 */
public class SearchBox extends AnchorPane {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchBox.class);

    private TextField searchField;
    private Button clearButton;

    /**
     * A consumer, which is called when the search term has been modified
     */
    private final Optional<Consumer<String>> onSearch;

    /**
     * A consumer, which is called when the clear button has been pressed
     */
    private final Optional<Runnable> onClear;

    /**
     * Constructor.
     * Assumes that no additional action should be performed after the clear button has been pressed
     *
     * @param onSearch The onSearch callback
     */
    public SearchBox(Consumer<String> onSearch) {
        this(onSearch, null);
    }

    /**
     * Constructor
     *
     * @param onSearch The onSearch callback
     * @param onClear  The onClear callback
     */
    public SearchBox(Consumer<String> onSearch, Runnable onClear) {
        super();

        this.onSearch = Optional.ofNullable(onSearch);
        this.onClear = Optional.ofNullable(onClear);

        if (!this.onSearch.isPresent()) {
            LOGGER.error("Search behavior not set, will do nothing (default).");
        }

        this.populate();
    }

    /**
     * Populates the searchbox
     */
    private void populate() {
        this.getStyleClass().add("searchBox");

        this.searchField = new TextField();

        this.searchField.getStyleClass().add("searchBar");
        this.searchField.prefHeightProperty().bind(this.prefHeightProperty());
        this.searchField.prefWidthProperty().bind(this.prefWidthProperty());
        this.searchField.textProperty().addListener(event -> this.onSearch.ifPresent(onSearch -> onSearch.accept(getText())));

        AnchorPane.setLeftAnchor(searchField, 0.0);
        AnchorPane.setRightAnchor(searchField, 0.0);

        this.clearButton = new Button();
        this.clearButton.getStyleClass().add("searchCleanButton");
        this.clearButton.setOnMouseClicked(event -> {
            this.searchField.clear();
            this.onClear.ifPresent(Runnable::run);
        });

        AnchorPane.setRightAnchor(clearButton, 0.0);

        this.getChildren().addAll(searchField, clearButton);
    }

    /**
     * Returns the {@link StringProperty} belonging to the searchfield
     * @return
     */
    public StringProperty textProperty() {
        return this.searchField.textProperty();
    }

    /**
     * Returns the content of the searchfield
     * @return
     */
    public String getText() {
        return this.searchField.getText();
    }
}
