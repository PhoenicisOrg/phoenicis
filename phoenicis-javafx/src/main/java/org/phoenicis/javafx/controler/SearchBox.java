package org.phoenicis.javafx.controler;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.phoenicis.javafx.skin.SearchBoxSkin;

import java.util.function.Consumer;

public class SearchBox extends Control {
    /**
     * A consumer, which is called when the search term has been modified
     */
    private final ObjectProperty<Consumer<String>> onSearch;

    /**
     * A consumer, which is called when the clear button has been pressed
     */
    private final ObjectProperty<Runnable> onClear;

    public SearchBox(ObjectProperty<Consumer<String>> onSearch, ObjectProperty<Runnable> onClear) {
        super();

        this.onSearch = onSearch;
        this.onClear = onClear;
    }

    public SearchBox(Consumer<String> onSearch, Runnable onClear) {
        this(new SimpleObjectProperty<>(onSearch), new SimpleObjectProperty<>(onClear));
    }

    public SearchBox() {
        this(new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    protected Skin<?> createDefaultSkin() {
        return new SearchBoxSkin(this);
    }

    public ObjectProperty<Consumer<String>> onSearchProperty() {
        return onSearch;
    }

    public Consumer<String> getOnSearch() {
        return onSearch.get();
    }

    public void setOnSearch(Consumer<String> onSearch) {
        this.onSearch.set(onSearch);
    }

    public ObjectProperty<Runnable> onClearProperty() {
        return onClear;
    }

    public Runnable getOnClear() {
        return onClear.get();
    }

    public void setOnClear(Runnable onClear) {
        this.onClear.set(onClear);
    }
}
