package org.phoenicis.javafx.behavior;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.phoenicis.javafx.controler.SearchBox;
import org.phoenicis.javafx.skin.SearchBoxSkin;

import java.util.Optional;
import java.util.function.Consumer;

public class SearchBoxBehavior extends BehaviorBase<SearchBox, SearchBoxSkin> {
    public SearchBoxBehavior(SearchBox control, SearchBoxSkin skin) {
        super(control, skin);
    }

    @Override
    public void initialise() {
        getSearchField().textProperty()
                .addListener(
                        event -> getOnClear().ifPresent(onSearch -> getOnSearch().accept(getSearchField().getText())));

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
