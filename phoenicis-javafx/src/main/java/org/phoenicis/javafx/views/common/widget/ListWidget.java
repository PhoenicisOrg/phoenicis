package org.phoenicis.javafx.views.common.widget;

import javafx.collections.ObservableList;

import java.util.Collection;

/**
 * Created by marc on 15.05.17.
 */
public interface ListWidget<E> {
    void bind(ObservableList<E> list);

    void deselectAll();

    void select(E item);

    Collection<E> getSelectedItems();
}
