package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.DetailsListWidgetSkin;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetSelection;

/**
 * A details list widget component used to show a list of elements in a detailed list
 *
 * @param <E> The concrete type of the elements shown in this list widget
 */
public class DetailsListWidget<E> extends ListWidgetBase<E, DetailsListWidget<E>, DetailsListWidgetSkin<E>> {
    /**
     * Constructor
     *
     * @param elements The elements shown in this list widget
     * @param selectedElement The currently selected element
     */
    public DetailsListWidget(ObservableList<ListWidgetEntry<E>> elements,
            ObjectProperty<ListWidgetSelection<E>> selectedElement) {
        super(elements, selectedElement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DetailsListWidgetSkin<E> createSkin() {
        return new DetailsListWidgetSkin<>(this);
    }
}
