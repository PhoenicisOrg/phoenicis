package org.phoenicis.javafx.views.common.lists;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.List;

/**
 * A {@link ListChangeListener.Change} implementation, which is to be fired when an observable list has been
 * initialised.
 * The fired Change event tells all listeners that the list has been freshly populated.
 *
 * @param <E> The type of object inside the {@link ObservableList}
 */
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