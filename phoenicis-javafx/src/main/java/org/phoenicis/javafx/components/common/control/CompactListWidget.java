package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.CompactListWidgetSkin;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

public class CompactListWidget<E> extends ListWidgetBase<E, CompactListWidget<E>, CompactListWidgetSkin<E>> {
    public CompactListWidget(ObservableList<ListWidgetEntry<E>> elements,
            ObjectProperty<ListWidgetEntry<E>> selectedElement) {
        super(elements, selectedElement);
    }

    @Override
    public CompactListWidgetSkin<E> createSkin() {
        return new CompactListWidgetSkin<>(this);
    }
}
