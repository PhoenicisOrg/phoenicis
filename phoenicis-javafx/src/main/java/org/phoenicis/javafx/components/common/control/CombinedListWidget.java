package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.CombinedListWidgetSkin;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;

public class CombinedListWidget<E> extends ListWidgetBase<E, CombinedListWidget<E>, CombinedListWidgetSkin<E>> {
    private final ObjectProperty<ListWidgetType> selectedListWidget;

    public CombinedListWidget(ObservableList<ListWidgetEntry<E>> elements,
            ObjectProperty<ListWidgetEntry<E>> selectedElement, ObjectProperty<ListWidgetType> selectedListWidget) {
        super(elements, selectedElement);

        this.selectedListWidget = selectedListWidget;
    }

    public CombinedListWidget(ObservableList<ListWidgetEntry<E>> elements) {
        this(elements, new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    @Override
    public CombinedListWidgetSkin<E> createSkin() {
        return new CombinedListWidgetSkin<>(this);
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
