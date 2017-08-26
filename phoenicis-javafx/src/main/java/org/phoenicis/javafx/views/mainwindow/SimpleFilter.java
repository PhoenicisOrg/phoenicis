package org.phoenicis.javafx.views.mainwindow;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * a simple filter which filters based on a given text
 * @param <E>
 */
public class SimpleFilter<E> {
    private StringProperty filterText;

    private BiPredicate<String, E> filterTextMatcher;

    private ObservableList<Predicate<E>> filters;

    /**
     * constructor
     * @param filteredList list which shall be filtered
     * @param filterTextMatcher checks if the filter shall be applied
     */
    public SimpleFilter(FilteredList<E> filteredList, BiPredicate<String, E> filterTextMatcher) {
        this.filterTextMatcher = filterTextMatcher;

        this.filterText = new SimpleStringProperty("");
        this.filters = FXCollections.observableArrayList();

        this.filterText.addListener((observableValue, oldValue, newValue) -> filteredList.setPredicate(this::filter));
        this.filters.addListener(
                (ListChangeListener<? super Predicate<E>>) change -> filteredList.setPredicate(this::filter));
    }

    /**
     * sets the text which is used to filter
     * @param filterText
     */
    public void setFilterText(String filterText) {
        this.filterText.setValue(filterText);
    }

    /**
     * adds a new filter
     * @param filter
     */
    public void addFilter(Predicate<E> filter) {
        this.filters.add(filter);
    }

    /**
     * sets the list of filters
     * @param filters
     */
    public void setFilters(Predicate<E>... filters) {
        this.filters.setAll(filters);
    }

    /**
     * clears the filter (e.g. resets the filter text)
     */
    public void clearAll() {
        this.filterText.setValue("");
        this.filters.clear();
    }

    /**
     * clears only filters (not the filter text)
     */
    public void clearFilters() {
        this.filters.clear();
    }

    /**
     * filter a given value
     * @param value
     * @return if filtered
     */
    public boolean filter(E value) {
        boolean result = false;

        if (filters.isEmpty()) {
            result = true;
        }

        for (Predicate<E> filter : filters) {
            result |= filter.test(value);
        }

        return result && filterTextMatcher.test(filterText.getValue(), value);
    }

}
