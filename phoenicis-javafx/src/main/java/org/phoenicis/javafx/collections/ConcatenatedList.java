package org.phoenicis.javafx.collections;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class ConcatenatedList<T> extends ObservableListBase<T> {
    private final ObservableList<? extends List<? extends T>> sources;
    private final Map<ObservableList<? extends T>, ListChangeListener<T>> listeners;
    private final List<T> backingList;

    private ConcatenatedList(ObservableList<? extends List<? extends T>> sources) {
        this.sources = sources;
        this.listeners = new IdentityHashMap<>();
        this.backingList = new ArrayList<>();

        bindSources();

        this.sources.addListener((ListChangeListener<List<? extends T>>) change -> {
            bindSources();
            refresh();
        });

        refresh();
    }

    public static <T> ConcatenatedList<T> create(ObservableList<? extends List<? extends T>> sources) {
        return new ConcatenatedList<>(sources);
    }

    @SafeVarargs
    public static <T> ConcatenatedList<T> create(ObservableList<? extends T>... sources) {
        return new ConcatenatedList<>(FXCollections.observableArrayList(sources));
    }

    @Override
    public T get(int index) {
        return backingList.get(index);
    }

    @Override
    public int size() {
        return backingList.size();
    }

    @SuppressWarnings("unchecked")
    private void bindSources() {
        listeners.forEach(ObservableList::removeListener);
        listeners.clear();

        for (List<? extends T> source : sources) {
            if (source instanceof ObservableList) {
                final ObservableList<? extends T> observableSource = (ObservableList<? extends T>) source;
                final ListChangeListener<T> listener = change -> refresh();
                ((ObservableList<T>) observableSource).addListener(listener);
                listeners.put(observableSource, listener);
            }
        }
    }

    private void refresh() {
        final List<T> oldValues = new ArrayList<>(backingList);

        backingList.clear();
        for (List<? extends T> source : sources) {
            backingList.addAll(source);
        }

        beginChange();
        nextReplace(0, backingList.size(), oldValues);
        endChange();
    }
}
