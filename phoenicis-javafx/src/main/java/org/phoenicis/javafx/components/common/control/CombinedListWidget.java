package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.CombinedListWidgetSkin;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;

public class CombinedListWidget<E> extends ControlBase<CombinedListWidget<E>, CombinedListWidgetSkin<E>> {
    /**
     * A list containing all elements to be shown inside this {@link ListWidget}
     */
    private final ObservableList<ListWidgetEntry<E>> elements;

    private final ObjectProperty<ListWidgetEntry<E>> selectedElement;

    private final ObjectProperty<ListWidgetType> selectedListWidget;

    public CombinedListWidget(ObservableList<ListWidgetEntry<E>> elements,
            ObjectProperty<ListWidgetEntry<E>> selectedElement, ObjectProperty<ListWidgetType> selectedListWidget) {
        super();

        this.elements = elements;
        this.selectedElement = selectedElement;
        this.selectedListWidget = selectedListWidget;
    }

    public CombinedListWidget(ObservableList<ListWidgetEntry<E>> elements) {
        this(elements, new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    public CombinedListWidget() {
        this(FXCollections.observableArrayList(), new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    @Override
    public CombinedListWidgetSkin<E> createSkin() {
        return new CombinedListWidgetSkin<>(this);
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

    public ListWidgetType getSelectedListWidget() {
        return selectedListWidget.get();
    }

    public ObjectProperty<ListWidgetType> selectedListWidgetProperty() {
        return selectedListWidget;
    }

    public void setSelectedListWidget(ListWidgetType selectedListWidget) {
        this.selectedListWidget.set(selectedListWidget);
    }
}
