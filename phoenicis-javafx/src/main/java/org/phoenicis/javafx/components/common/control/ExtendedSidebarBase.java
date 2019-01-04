package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.ExtendedSidebarSkinBase;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;

/**
 * A base sidebar class containing a toggle button group, a search box and a list widget selector
 *
 * @param <E> The element type of the toggle button group
 * @param <C> The concrete component type
 * @param <S> The concrete skin type
 */
public abstract class ExtendedSidebarBase<E, C extends ExtendedSidebarBase<E, C, S>, S extends ExtendedSidebarSkinBase<E, C, S>>
        extends SidebarBase<E, C, S> {
    /**
     * The search term entered by the user
     */
    private final StringProperty searchTerm;

    /**
     * The currently selected {@link ListWidgetType} by the user
     */
    private final ObjectProperty<ListWidgetType> selectedListWidget;

    /**
     * Constructor
     *
     * @param items The items shown inside a toggle button group in the sidebar
     * @param searchTerm The search term entered by the user
     * @param selectedListWidget The currently selected {@link ListWidgetType} by the user
     */
    protected ExtendedSidebarBase(ObservableList<E> items, StringProperty searchTerm,
            ObjectProperty<ListWidgetType> selectedListWidget) {
        super(items);

        this.searchTerm = searchTerm;
        this.selectedListWidget = selectedListWidget;
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
