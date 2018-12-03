package org.phoenicis.javafx.components.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.phoenicis.javafx.components.skin.SearchBoxSkin;

import java.util.function.Consumer;

/**
 * A search box component used to add a search term
 */
public class SearchBox extends ControlBase<SearchBox, SearchBoxSkin> {
    /**
     * The submitted search term
     */
    private final StringProperty searchTerm;

    /**
     * A consumer, which is called when the search term has been modified
     */
    private final ObjectProperty<Consumer<String>> onSearch;

    /**
     * A consumer, which is called when the clear button has been pressed
     */
    private final ObjectProperty<Runnable> onClear;

    /**
     * Constructor
     *
     * @param searchTerm The submitted search term
     * @param onSearch Callback for search input
     * @param onClear Callback for clear input
     */
    public SearchBox(StringProperty searchTerm, ObjectProperty<Consumer<String>> onSearch,
            ObjectProperty<Runnable> onClear) {
        super();

        this.searchTerm = searchTerm;
        this.onSearch = onSearch;
        this.onClear = onClear;
    }

    /**
     * Constructor
     *
     * @param onSearch Callback for search input
     * @param onClear Callback for clear input
     */
    public SearchBox(Consumer<String> onSearch, Runnable onClear) {
        this(new SimpleStringProperty(""), new SimpleObjectProperty<>(onSearch), new SimpleObjectProperty<>(onClear));
    }

    /**
     * Constructor
     */
    public SearchBox() {
        this(new SimpleStringProperty(""), new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     *
     * @return A created search box skin object
     */
    @Override
    public SearchBoxSkin createSkin() {
        return new SearchBoxSkin(this);
    }

    public String getSearchTerm() {
        return searchTerm.get();
    }

    public StringProperty searchTermProperty() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm.set(searchTerm);
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
