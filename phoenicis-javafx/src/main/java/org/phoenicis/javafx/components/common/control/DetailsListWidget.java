package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.DetailsListWidgetSkin;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

public class DetailsListWidget<E> extends ListWidgetBase<E, DetailsListWidget<E>, DetailsListWidgetSkin<E>> {

    public DetailsListWidget(ObservableList<ListWidgetEntry<E>> elements,
            ObjectProperty<ListWidgetEntry<E>> selectedElement) {
        super(elements, selectedElement);
    }

    @Override
    public DetailsListWidgetSkin<E> createSkin() {
        return new DetailsListWidgetSkin<>(this);
    }
}
