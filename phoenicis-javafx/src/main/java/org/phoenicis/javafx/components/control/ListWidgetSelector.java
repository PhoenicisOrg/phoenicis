package org.phoenicis.javafx.components.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.javafx.components.skin.ListWidgetSelectorSkin;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;

import java.util.function.Consumer;

public class ListWidgetSelector extends ControlBase<ListWidgetSelector, ListWidgetSelectorSkin> {
    private final ObjectProperty<Consumer<ListWidgetType>> onSelect;

    private final ObjectProperty<ListWidgetType> selected;

    public ListWidgetSelector(ObjectProperty<Consumer<ListWidgetType>> onSelect,
            ObjectProperty<ListWidgetType> selected) {
        super();

        this.onSelect = onSelect;
        this.selected = selected;
    }

    public ListWidgetSelector(Consumer<ListWidgetType> onSelect, ListWidgetType selected) {
        this(new SimpleObjectProperty<>(onSelect), new SimpleObjectProperty<>(selected));
    }

    public ListWidgetSelector(Consumer<ListWidgetType> onSelect) {
        this(new SimpleObjectProperty<>(onSelect), new SimpleObjectProperty<>());
    }

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
