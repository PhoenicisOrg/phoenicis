package org.phoenicis.javafx.views.mainwindow.apps;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Created by marc on 29.03.17.
 */
public class ApplicationFilter<E> {
    private StringProperty filterText;

    private BiPredicate<String, E> filterTextMatcher;

    private ObservableList<Predicate<E>> filters;

    public ApplicationFilter(FilteredList<E> filteredList, BiPredicate<String, E> filterTextMatcher) {
        this.filterTextMatcher = filterTextMatcher;

        this.filterText = new SimpleStringProperty("");
        this.filters = FXCollections.observableArrayList();

        this.filterText.addListener((observableValue, oldValue, newValue) -> filteredList.setPredicate(this::filter));
        this.filters.addListener((ListChangeListener<? super Predicate<E>>) change -> filteredList.setPredicate(this::filter));
    }

    public void setFilterText(String filterText) {
        this.filterText.setValue(filterText);
    }

    public void addFilter(Predicate<E> filter) {
        this.filters.add(filter);
    }

    public void setFilters(Predicate<E> ... filters) {
        this.filters.setAll(filters);
    }

    public void clearAll() {
        this.filterText.setValue("");
        this.filters.clear();
    }

    public void clearFilters() {
        this.filters.clear();
    }

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
