package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

/**
 * A base list widget component
 *
 * @param <E> The concrete type of the elements shown in this list widget
 * @param <C> The concrete list widget component class
 * @param <S> The concrete list widget skin class
 */
public abstract class ListWidgetBase<E, C extends ListWidgetBase<E, C, S>, S extends SkinBase<C, S>>
        extends ControlBase<C, S> {
    /**
     * A list containing all elements shown inside this {@link C}
     */
    private final ObservableList<ListWidgetEntry<E>> elements;

    /**
     * The currently selected element or null if no element is selected
     */
    private final ObjectProperty<ListWidgetEntry<E>> selectedElement;

    /**
     * Constructor
     *
     * @param elements The elements shown in this list widget
     * @param selectedElement The currently selected element
     */
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
