package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.CompactListWidgetSkin;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetSelection;

/**
 * A compact list widget component used to show a list of elements in a list with a small miniature icon and some
 * additional information
 *
 * @param <E> The concrete type of the elements shown in this list widget
 */
public class CompactListWidget<E> extends ListWidgetBase<E, CompactListWidget<E>, CompactListWidgetSkin<E>> {
    /**
     * Constructor
     *
     * @param elements The elements shown in this list widget
     * @param selectedElement The currently selected element
     */
    public CompactListWidget(ObservableList<ListWidgetEntry<E>> elements,
            ObjectProperty<ListWidgetSelection<E>> selectedElement) {
        super(elements, selectedElement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompactListWidgetSkin<E> createSkin() {
        return new CompactListWidgetSkin<>(this);
    }
}
