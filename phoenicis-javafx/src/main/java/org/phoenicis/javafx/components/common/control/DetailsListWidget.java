package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.DetailsListWidgetSkin;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

public class DetailsListWidget<E> extends ControlBase<DetailsListWidget<E>, DetailsListWidgetSkin<E>> {
    /**
     * A list containing all elements to be shown inside this {@link ListWidget}
     */
    private final ObservableList<ListWidgetEntry<E>> elements;

    private final ObjectProperty<ListWidgetEntry<E>> selectedElement;

    public DetailsListWidget(ObservableList<ListWidgetEntry<E>> elements, ObjectProperty<ListWidgetEntry<E>> selectedElement) {
        super();

        this.elements = elements;
        this.selectedElement = selectedElement;
    }

    @Override
    public DetailsListWidgetSkin<E> createSkin() {
        return new DetailsListWidgetSkin<>(this);
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
