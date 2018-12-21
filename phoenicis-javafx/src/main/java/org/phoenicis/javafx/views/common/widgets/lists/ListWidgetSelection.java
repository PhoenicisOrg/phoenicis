package org.phoenicis.javafx.views.common.widgets.lists;

import javafx.scene.input.MouseEvent;

/**
 * Represents a selection of an element in a list widget
 *
 * @param <E> The concrete type of the selected element
 */
public class ListWidgetSelection<E> {
    /**
     * The selected element
     */
    private final ListWidgetEntry<E> selection;

    /**
     * The mouse event, which has been fired during the selection
     */
    private final MouseEvent event;

    /**
     * Constructor
     *
     * @param selection The selected element
     * @param event The mouse event, which has been fired during the selection
     */
    public ListWidgetSelection(ListWidgetEntry<E> selection, MouseEvent event) {
        super();

        this.selection = selection;
        this.event = event;
    }

    /**
     * Gets the selected element
     *
     * @return The selected element
     */
    public ListWidgetEntry<E> getSelection() {
        return selection;
    }

    /**
     * Gets the item contained inside the selected element
     *
     * @return The item contained inside the selected element
     */
    public E getItem() {
        return getSelection().getItem();
    }

    /**
     * Gets the mouse event, which has been fired during the selection
     *
     * @return The mouse event fired during the selection
     */
    public MouseEvent getEvent() {
        return event;
    }
}
