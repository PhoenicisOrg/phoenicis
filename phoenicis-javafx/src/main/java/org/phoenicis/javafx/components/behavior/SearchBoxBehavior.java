package org.phoenicis.javafx.components.behavior;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.phoenicis.javafx.components.control.SearchBox;
import org.phoenicis.javafx.components.skin.SearchBoxSkin;

/**
 * The behavior for the {@link SearchBox} component
 */
public class SearchBoxBehavior extends BehaviorBase<SearchBox, SearchBoxSkin, SearchBoxBehavior> {
    private final PauseTransition pause;

    /**
     * Constructor
     *
     * @param control The control of the search box behavior
     * @param skin The skin of the search box behavior
     */
    public SearchBoxBehavior(SearchBox control, SearchBoxSkin skin) {
        super(control, skin);

        this.pause = new PauseTransition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        pause.durationProperty().bind(getControl().delayProperty());

        // add an input listener to the text field
        getSearchField().textProperty().addListener(event -> {
            pause.setOnFinished(pauseEvent -> {
                final String searchTerm = getSearchField().getText().toLowerCase();

                getControl().setSearchTerm(searchTerm);
            });

            // delay the adoption of the new search term
            this.pause.playFromStart();
        });

        // add a mouse click listener to the clear button
        getClearButton().setOnMouseClicked(event -> {
            getSearchField().clear();

            getControl().searchTermProperty().set("");
        });
    }

    private TextField getSearchField() {
        return getSkin().getSearchField();
    }

    private Button getClearButton() {
        return getSkin().getClearButton();
    }
}
