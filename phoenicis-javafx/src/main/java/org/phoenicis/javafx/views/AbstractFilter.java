package org.phoenicis.javafx.views;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract Filter class.
 * Every filter class for the application tabs should implement this class
 *
 * @author Marc Arndt
 */
public abstract class AbstractFilter {
    /**
     * A list of callbacks, which should be called when a filter dependency changes
     */
    protected final List<Runnable> onFilterChanged;

    /**
     * Constructor
     */
    public AbstractFilter() {
        super();

        this.onFilterChanged = new ArrayList<>();
    }

    /**
     * Adds a filter callback, that is called when a filter dependency changes
     * @param callback The callback method to be called
     */
    public void addOnFilterChanged(Runnable callback) {
        this.onFilterChanged.add(callback);
    }

    /**
     * Triggers a filter dependency change
     */
    public void triggerFilterChanged() {
        this.onFilterChanged.forEach(onFilterChanged -> Platform.runLater(onFilterChanged::run));
    }
}
