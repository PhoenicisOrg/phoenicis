package org.phoenicis.javafx.skin;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.phoenicis.javafx.behavior.BehaviorBase;
import org.phoenicis.javafx.behavior.SearchBoxBehavior;
import org.phoenicis.javafx.controler.SearchBox;

public class SearchBoxSkin extends BehaviorSkinBase<SearchBox, SearchBoxBehavior> {
    /**
     * The search field of the search box
     */
    private TextField searchField;

    /**
     * The clear button of the search box
     */
    private Button clearButton;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public SearchBoxSkin(SearchBox control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        AnchorPane container = new AnchorPane();
        container.getStyleClass().add("searchBox");

        this.searchField = createTextField(container);
        this.clearButton = createClearButton();

        container.getChildren().addAll(searchField, clearButton);

        getChildren().addAll(container);
    }

    @Override
    public BehaviorBase<SearchBox, ?> createDefaultBehavior() {
        return new SearchBoxBehavior(getControl(), this);
    }

    private TextField createTextField(AnchorPane container) {
        TextField searchField = new TextField();

        searchField.getStyleClass().add("searchBar");

        searchField.prefHeightProperty().bind(container.prefHeightProperty());
        searchField.prefWidthProperty().bind(container.prefWidthProperty());

        AnchorPane.setLeftAnchor(searchField, 0.0);
        AnchorPane.setRightAnchor(searchField, 0.0);

        return searchField;
    }

    private Button createClearButton() {
        Button clearButton = new Button();

        clearButton.getStyleClass().add("searchCleanButton");

        AnchorPane.setRightAnchor(clearButton, 0.0);

        return clearButton;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Button getClearButton() {
        return clearButton;
    }
}
