package org.phoenicis.javafx.components.common.widgets.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.common.widgets.skin.ListWidgetSelectorSkin;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;

import java.util.function.Consumer;

/**
 * A selector component to select one of multiple list widgets to display
 */
public class ListWidgetSelector extends ControlBase<ListWidgetSelector, ListWidgetSelectorSkin> {
    /**
     * The consumer to be called when a different list widget has been selected.
     * The consumer is then called with the type of the selected list widget
     */
    private final ObjectProperty<Consumer<ListWidgetType>> onSelect;

    /**
     * The type of the selected list widget
     */
    private final ObjectProperty<ListWidgetType> selected;

    /**
     * Constructor
     *
     * @param onSelect The consumer to be called when a different list widget has been selected
     * @param selected The type of the selected list widget
     */
    public ListWidgetSelector(ObjectProperty<Consumer<ListWidgetType>> onSelect,
            ObjectProperty<ListWidgetType> selected) {
        super();

        this.onSelect = onSelect;
        this.selected = selected;
    }

    /**
     * Constructor
     *
     * @param onSelect The consumer to be called when a different list widget has been selected
     * @param selected The type of the selected list widget
     */
    public ListWidgetSelector(Consumer<ListWidgetType> onSelect, ListWidgetType selected) {
        this(new SimpleObjectProperty<>(onSelect), new SimpleObjectProperty<>(selected));
    }

    /**
     * Constructor
     *
     * @param onSelect The consumer to be called when a different list widget has been selected
     */
    public ListWidgetSelector(Consumer<ListWidgetType> onSelect) {
        this(new SimpleObjectProperty<>(onSelect), new SimpleObjectProperty<>());
    }

    /**
     * Constructor
     */
    public ListWidgetSelector() {
        this(new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    @Override
    public ListWidgetSelectorSkin createSkin() {
        return new ListWidgetSelectorSkin(this);
    }

    public Consumer<ListWidgetType> getOnSelect() {
        return onSelect.get();
    }

    public ObjectProperty<Consumer<ListWidgetType>> onSelectProperty() {
        return onSelect;
    }

    public ListWidgetType getSelected() {
        return selected.get();
    }

    public ObjectProperty<ListWidgetType> selectedProperty() {
        return selected;
    }

    public void setOnSelect(Consumer<ListWidgetType> onSelect) {
        this.onSelect.set(onSelect);
    }

    public void setSelected(ListWidgetType selected) {
        this.selected.set(selected);
    }
}
