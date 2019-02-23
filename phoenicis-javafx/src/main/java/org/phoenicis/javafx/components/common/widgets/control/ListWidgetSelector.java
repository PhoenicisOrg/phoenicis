package org.phoenicis.javafx.components.common.widgets.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.common.widgets.skin.ListWidgetSelectorSkin;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;

/**
 * A selector component to select one of multiple list widgets to display
 */
public class ListWidgetSelector extends ControlBase<ListWidgetSelector, ListWidgetSelectorSkin> {
    /**
     * The type of the selected list widget
     */
    private final ObjectProperty<ListWidgetType> selected;

    /**
     * Constructor
     *
     * @param selected The type of the selected list widget
     */
    public ListWidgetSelector(ObjectProperty<ListWidgetType> selected) {
        super();

        this.selected = selected;
    }

    /**
     * Constructor
     *
     * @param selected The type of the selected list widget
     */
    public ListWidgetSelector(ListWidgetType selected) {
        this(new SimpleObjectProperty<>(selected));
    }

    /**
     * Constructor
     */
    public ListWidgetSelector() {
        this(new SimpleObjectProperty<>());
    }

    @Override
    public ListWidgetSelectorSkin createSkin() {
        return new ListWidgetSelectorSkin(this);
    }

    public ListWidgetType getSelected() {
        return selected.get();
    }

    public ObjectProperty<ListWidgetType> selectedProperty() {
        return selected;
    }

    public void setSelected(ListWidgetType selected) {
        this.selected.set(selected);
    }
}
