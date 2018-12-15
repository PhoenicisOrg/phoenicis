package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.CompactListWidgetSkin;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

public class CompactListWidget<E> extends ControlBase<CompactListWidget<E>, CompactListWidgetSkin<E>> {
    /**
     * A list containing all elements to be shown inside this {@link ListWidget}
     */
    private final ObservableList<ListWidgetEntry<E>> elements;

    private final ObjectProperty<ListWidgetEntry<E>> selectedElement;

    public CompactListWidget(ObservableList<ListWidgetEntry<E>> elements,
            ObjectProperty<ListWidgetEntry<E>> selectedElement) {
        super();

        this.elements = elements;
        this.selectedElement = selectedElement;
    }

    @Override
    public CompactListWidgetSkin<E> createSkin() {
        return new CompactListWidgetSkin<>(this);
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
