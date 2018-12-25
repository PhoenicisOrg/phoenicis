package org.phoenicis.javafx.components.common.widgets.icons.control;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.widgets.icons.skin.IconsListWidgetSkin;
import org.phoenicis.javafx.components.common.widgets.control.ListWidgetBase;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetSelection;

/**
 * An icons list widget component used to show a list of elements using large icons
 *
 * @param <E> The concrete type of the elements shown in this list widget
 */
public class IconsListWidget<E> extends ListWidgetBase<E, IconsListWidget<E>, IconsListWidgetSkin<E>> {
    /**
     * Constructor
     *
     * @param elements The elements shown in this list widget
     * @param selectedElement The currently selected element
     */
    public IconsListWidget(ObservableList<ListWidgetElement<E>> elements,
            ObjectProperty<ListWidgetSelection<E>> selectedElement) {
        super(elements, selectedElement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IconsListWidgetSkin<E> createSkin() {
        return new IconsListWidgetSkin<>(this);
    }
}
