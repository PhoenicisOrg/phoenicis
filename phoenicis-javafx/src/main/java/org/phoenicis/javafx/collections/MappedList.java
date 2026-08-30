package org.phoenicis.javafx.collections;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MappedList<S, T> extends ObservableListBase<S> {
    private final ObservableList<T> source;
    private final Function<T, S> mapper;
    private final List<S> backingList;

    public MappedList(ObservableList<T> source, Function<T, S> mapper) {
        this.source = source;
        this.mapper = mapper;
        this.backingList = new ArrayList<>();

        refresh();
        this.source.addListener((ListChangeListener<T>) change -> refresh());
    }

    @Override
    public S get(int index) {
        return backingList.get(index);
    }

    @Override
    public int size() {
        return backingList.size();
    }

    private void refresh() {
        final List<S> oldValues = new ArrayList<>(backingList);

        backingList.clear();
        for (T item : source) {
            backingList.add(mapper.apply(item));
        }

        beginChange();
        nextReplace(0, backingList.size(), oldValues);
        endChange();
    }
}
