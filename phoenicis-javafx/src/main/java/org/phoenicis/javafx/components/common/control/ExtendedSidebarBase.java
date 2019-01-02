package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.ExtendedSidebarSkinBase;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;

public abstract class ExtendedSidebarBase<E, C extends ExtendedSidebarBase<E, C, S>, S extends ExtendedSidebarSkinBase<E, C, S>>
        extends SidebarBase<E, C, S> {
    private final StringProperty searchTerm;

    private final ObjectProperty<ListWidgetType> selectedListWidget;

    protected ExtendedSidebarBase(ObservableList<E> items, StringProperty searchTerm,
            ObjectProperty<ListWidgetType> selectedListWidget) {
        super(items);

        this.searchTerm = searchTerm;
        this.selectedListWidget = selectedListWidget;
    }

    protected ExtendedSidebarBase(ObservableList<E> items, ObjectProperty<ListWidgetType> selectedListWidget) {
        this(items, new SimpleStringProperty(""), selectedListWidget);
    }

    public String getSearchTerm() {
        return searchTerm.get();
    }

    public StringProperty searchTermProperty() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm.set(searchTerm);
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
