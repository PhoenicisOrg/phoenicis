package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import org.phoenicis.javafx.components.common.skin.SearchBoxSkin;

/**
 * A search box component used to add a search term
 */
public class SearchBox extends ControlBase<SearchBox, SearchBoxSkin> {
    /**
     * The submitted search term
     */
    private final StringProperty searchTerm;

    /**
     * The delay between a change to the search term and its adoption to the model
     */
    private final ObjectProperty<Duration> delay;

    /**
     * Constructor
     *
     * @param searchTerm The submitted search term
     * @param delay The delay between a change to the search term and its adoption to the model
     */
    public SearchBox(StringProperty searchTerm, ObjectProperty<Duration> delay) {
        super();

        this.searchTerm = searchTerm;
        this.delay = delay;
    }

    /**
     * Constructor
     *
     * @param searchTerm The submitted search term
     */
    public SearchBox(StringProperty searchTerm) {
        this(searchTerm, new SimpleObjectProperty<>(Duration.seconds(0.5)));
    }

    /**
     * Constructor
     */
    public SearchBox() {
        this(new SimpleStringProperty(""), new SimpleObjectProperty<>(Duration.seconds(0.5)));
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

    public Duration getDelay() {
        return delay.get();
    }

    public ObjectProperty<Duration> delayProperty() {
        return delay;
    }

    public void setDelay(Duration delay) {
        this.delay.set(delay);
    }
}
