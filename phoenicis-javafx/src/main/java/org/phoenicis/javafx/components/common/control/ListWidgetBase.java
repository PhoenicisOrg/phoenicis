package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

public abstract class ListWidgetBase<E, C extends ListWidgetBase<E, C, S>, S extends SkinBase<C, S>>
        extends ControlBase<C, S> {
    /**
     * A list containing all elements to be shown inside this {@link C}
     */
    private final ObservableList<ListWidgetEntry<E>> elements;

    private final ObjectProperty<ListWidgetEntry<E>> selectedElement;

    protected ListWidgetBase(ObservableList<ListWidgetEntry<E>> elements,
            ObjectProperty<ListWidgetEntry<E>> selectedElement) {
        super();

        this.elements = elements;
        this.selectedElement = selectedElement;
    }

    public ObservableList<ListWidgetEntry<E>> getElements() {
        return elements;
    }

    public ListWidgetEntry<E> getSelectedElement() {
        return selectedElement.get();
    }

    public ObjectProperty<ListWidgetEntry<E>> selectedElementProperty() {
        return selectedElement;
    }

    public void setSelectedElement(ListWidgetEntry<E> selectedElement) {
        this.selectedElement.set(selectedElement);
    }
}
