package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.IconsListWidgetSkin;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

public class IconsListWidget<E> extends ListWidgetBase<E, IconsListWidget<E>, IconsListWidgetSkin<E>> {
    public IconsListWidget(ObservableList<ListWidgetEntry<E>> elements,
            ObjectProperty<ListWidgetEntry<E>> selectedElement) {
        super(elements, selectedElement);
    }

    @Override
    public IconsListWidgetSkin<E> createSkin() {
        return new IconsListWidgetSkin<>(this);
    }
}
