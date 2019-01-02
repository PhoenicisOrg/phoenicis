package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.SidebarSkinBase;

public abstract class SidebarBase<E, C extends SidebarBase<E, C, S>, S extends SidebarSkinBase<E, C, S>>
        extends ControlBase<C, S> {
    private final ObservableList<E> items;

    private final ObjectProperty<E> selectedItem;

    protected SidebarBase(ObservableList<E> items, ObjectProperty<E> selectedItem) {
        super();

        this.items = items;
        this.selectedItem = selectedItem;
    }

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
