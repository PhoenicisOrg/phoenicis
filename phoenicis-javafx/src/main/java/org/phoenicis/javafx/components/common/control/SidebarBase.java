package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.SidebarSkinBase;

/**
 * A base sidebar class containing a toggle button group
 *
 * @param <E> The element type of the toggle button group
 * @param <C> The concrete component type
 * @param <S> The concrete skin type
 */
public abstract class SidebarBase<E, C extends SidebarBase<E, C, S>, S extends SidebarSkinBase<E, C, S>>
        extends ControlBase<C, S> {
    /**
     * The items shown inside a toggle button group in the sidebar
     */
    private final ObservableList<E> items;

    /**
     * The currently selected item in the toggle button group
     */
    private final ObjectProperty<E> selectedItem;

    /**
     * Constructor
     *
     * @param items        The items shown inside a toggle button group in the sidebar
     * @param selectedItem The currently selected item in the toggle button group
     */
    protected SidebarBase(ObservableList<E> items, ObjectProperty<E> selectedItem) {
        super();

        this.items = items;
        this.selectedItem = selectedItem;
    }

    /**
     * Constructor
     *
     * @param items The items shown inside a toggle button group in the sidebar
     */
    protected SidebarBase(ObservableList<E> items) {
        this(items, new SimpleObjectProperty<>());
    }

    public ObservableList<E> getItems() {
        return items;
    }

    public E getSelectedItem() {
        return selectedItem.get();
    }

    public ObjectProperty<E> selectedItemProperty() {
        return selectedItem;
    }

    public void setSelectedItem(E selectedItem) {
        this.selectedItem.set(selectedItem);
    }
}
