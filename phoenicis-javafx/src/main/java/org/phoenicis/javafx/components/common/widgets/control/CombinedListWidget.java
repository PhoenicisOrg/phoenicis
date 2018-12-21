package org.phoenicis.javafx.components.common.widgets.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.widgets.skin.CombinedListWidgetSkin;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetSelection;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;

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
    public CombinedListWidget(ObservableList<ListWidgetElement<E>> elements,
            ObjectProperty<ListWidgetSelection<E>> selectedElement, ObjectProperty<ListWidgetType> selectedListWidget) {
        super(elements, selectedElement);

        this.selectedListWidget = selectedListWidget;
    }

    /**
     * Constructor
     *
     * @param elements The elements shown in this list widget
     */
    public CombinedListWidget(ObservableList<ListWidgetElement<E>> elements) {
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
