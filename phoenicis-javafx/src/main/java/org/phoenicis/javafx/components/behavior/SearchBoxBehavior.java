package org.phoenicis.javafx.components.behavior;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.phoenicis.javafx.components.control.SearchBox;
import org.phoenicis.javafx.components.skin.SearchBoxSkin;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A search box behavior/controller
 */
public class SearchBoxBehavior extends BehaviorBase<SearchBox, SearchBoxSkin, SearchBoxBehavior> {
    /**
     * Constructor
     *
     * @param control The control of the search box behavior
     * @param skin The skin of the search box behavior
     */
    public SearchBoxBehavior(SearchBox control, SearchBoxSkin skin) {
        super(control, skin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        // add an input listener to the text field
        getSearchField().textProperty()
                .addListener(
                        event -> getOnClear().ifPresent(onSearch -> getOnSearch().accept(getSearchField().getText())));

        // add a mouse click listener to the clear button
        getClearButton().setOnMouseClicked(event -> {
            getSearchField().clear();
            getOnClear().ifPresent(Runnable::run);
        });
    }

    private Consumer<String> getOnSearch() {
        return getControl().getOnSearch();
    }

    private Optional<Runnable> getOnClear() {
        return Optional.ofNullable(getControl().getOnClear());
    }

    private TextField getSearchField() {
        return getSkin().getSearchField();
    }

    private Button getClearButton() {
        return getSkin().getClearButton();
    }
}
