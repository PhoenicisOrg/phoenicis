package org.phoenicis.javafx.views.common.widgets.lists;

import javafx.collections.ObservableList;

import java.util.Collection;

/**
 * Basic functionality every list widget needs to contain.
 *
 * @author marc
 * @since 15.05.17
 */
public interface ListWidget<E> {
    /**
     * Binds the given <code>list</code> to this list widget
     * @param list The list to be bound
     */
    void bind(ObservableList<E> list);

    /**
     * Deselects all currently selected elements in this list widget
     */
    void deselectAll();

    /**
     * Selects the element, which belongs to the given <code>item</code>
     * @param item
     */
    void select(E item);

    /**
     * Returns a collection containing all selected items
     * @return A collection containing all selected items
     */
    Collection<E> getSelectedItems();
}
