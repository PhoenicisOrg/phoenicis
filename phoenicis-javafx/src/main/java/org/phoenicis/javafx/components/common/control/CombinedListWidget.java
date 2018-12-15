package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.CombinedListWidgetSkin;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;

/**
 * A combined list widget component which consists of multiple inner list widgets from which the user can choose
 *
 * @param <E> The concrete type of the elements shown in this list widget
 */
public class CombinedListWidget<E> extends ListWidgetBase<E, CombinedListWidget<E>, CombinedListWidgetSkin<E>> {
    /**
     * The selected inner list widget
     */
    private final ObjectProperty<ListWidgetType> selectedListWidget;

    /**
     * Constructor
     *
     * @param elements The elements shown in this list widget
     * @param selectedElement The currently selected element
     * @param selectedListWidget The selected/shown inner list widget
     */
    public CombinedListWidget(ObservableList<ListWidgetEntry<E>> elements,
            ObjectProperty<ListWidgetEntry<E>> selectedElement, ObjectProperty<ListWidgetType> selectedListWidget) {
        super(elements, selectedElement);

        this.selectedListWidget = selectedListWidget;
    }

    /**
     * Constructor
     *
     * @param elements The elements shown in this list widget
     */
    public CombinedListWidget(ObservableList<ListWidgetEntry<E>> elements) {
        this(elements, new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
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
