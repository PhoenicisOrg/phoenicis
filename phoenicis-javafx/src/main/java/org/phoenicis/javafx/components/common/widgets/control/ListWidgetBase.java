package org.phoenicis.javafx.components.common.widgets.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetSelection;

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
    private final ObservableList<ListWidgetElement<E>> elements;

    /**
     * The currently selected element or null if no element is selected
     */
    private final ObjectProperty<ListWidgetSelection<E>> selectedElement;

    /**
     * Constructor
     *
     * @param elements The elements shown in this list widget
     * @param selectedElement The currently selected element
     */
    protected ListWidgetBase(ObservableList<ListWidgetElement<E>> elements,
            ObjectProperty<ListWidgetSelection<E>> selectedElement) {
        super();

        this.elements = elements;
        this.selectedElement = selectedElement;
    }

    public ObservableList<ListWidgetElement<E>> getElements() {
        return elements;
    }

    public ListWidgetSelection<E> getSelectedElement() {
        return selectedElement.get();
    }

    public ObjectProperty<ListWidgetSelection<E>> selectedElementProperty() {
        return selectedElement;
    }

    public void setSelectedElement(ListWidgetSelection<E> selectedElement) {
        this.selectedElement.set(selectedElement);
    }

    /**
     * Selects the {@link ListWidgetElement} which belongs to the given {@link E innerElement}
     *
     * @param innerElement The inner element
     * @param event The corresponding mouse event used to trigger the selection
     */
    public void select(E innerElement, MouseEvent event) {
        final ListWidgetElement<E> foundElement = elements.stream()
                .filter(element -> element.getItem() == innerElement).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "The searched element is not contained inside the list widget"));

        setSelectedElement(new ListWidgetSelection<>(foundElement, event));
    }

    /**
     * Selects the {@link ListWidgetElement} which belongs to the given {@link E innerElement}
     *
     * @param innerElement The inner element
     */
    public void select(E innerElement) {
        final MouseEvent event = new MouseEvent(
                MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1,
                false, false, false, false, false,
                false, false, false, false, false,
                null);

        select(innerElement, event);
    }
}
