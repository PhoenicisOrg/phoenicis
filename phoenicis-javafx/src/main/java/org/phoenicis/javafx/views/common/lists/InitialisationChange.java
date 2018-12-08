package org.phoenicis.javafx.views.common.lists;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.List;

public class InitialisationChange<E> extends ListChangeListener.Change<E> {
    private final int from;
    private final int to;
    private boolean invalid = true;

    public InitialisationChange(int from, int to, ObservableList<E> list) {
        super(list);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean next() {
        if (invalid) {
            invalid = false;
            return true;
        }
        return false;
    }

    @Override
    public void reset() {
        invalid = true;
    }

    @Override
    public int getFrom() {
        return from;
    }

    @Override
    public int getTo() {
        return to;
    }

    @Override
    public List<E> getRemoved() {
        return Collections.emptyList();
    }

    @Override
    protected int[] getPermutation() {
        return new int[0];
    }
}