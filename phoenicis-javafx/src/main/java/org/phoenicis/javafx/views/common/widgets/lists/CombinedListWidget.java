package org.phoenicis.javafx.views.common.widgets.lists;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.views.common.MappedList;
import org.phoenicis.javafx.views.common.widgets.lists.compact.CompactListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.details.DetailsListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.icons.IconsListWidget;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link ListWidget} fusing all other {@link ListWidget}s.
 * Using this {@link ListWidget} enables an easy swapping of the other {@link ListWidget}s at runtime.
 *
 * @author marc
 * @since 15.05.17
 */
public class CombinedListWidget<E> extends VBox implements ListWidget<E> {
    /**
     * A set of all currently selected items/entries
     */
    private Set<ListWidgetEntry<E>> selectedItems;

    /**
     * A list of all currently visible items
     */
    private ObservableList<E> items;

    /**
     * A list of all mapped items
     */
    private MappedList<ListWidgetEntry<E>, E> mappedItems;

    private IconsListWidget<E> iconsList;
    private CompactListWidget<E> compactList;
    private DetailsListWidget<E> detailsList;

    /**
     * The currently shown {@link ListWidget}
     */
    private ListWidget<ListWidgetEntry<E>> currentList;

    /**
     * Constructor
     *
     * @param converter         A converter function, that takes an object of type <code>E</code> and returns a {@link ListWidgetEntry} for it
     * @param setOnMouseClicked An event listener function to be called when an entry has been selected/clicked
     */
    public CombinedListWidget(Function<E, ListWidgetEntry<E>> converter, BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.setPrefHeight(0);
        this.setPrefWidth(0);

        this.selectedItems = new HashSet<>();
        this.items = FXCollections.observableArrayList();
        this.mappedItems = new MappedList<>(items, converter);

        BiConsumer<E, MouseEvent> proxy = (element, event) -> {
            this.deselectAll();
            setOnMouseClicked.accept(element, event);
            this.select(element);
        };

        this.iconsList = new IconsListWidget<>(proxy);
        this.compactList = new CompactListWidget<>(proxy);
        this.detailsList = new DetailsListWidget<>(proxy);

        this.iconsList.bind(mappedItems);
        this.compactList.bind(mappedItems);
        this.detailsList.bind(mappedItems);

        this.showList(ListWidgetType.ICONS_LIST);
    }

    /**
     * Shows the list of the given <code>type</code>
     * @param type The type of list to be shown in this list widget
     */
    public void showList(ListWidgetType type) {
        switch (type) {
            case ICONS_LIST:
                this.currentList = iconsList;
                break;
            case COMPACT_LIST:
                this.currentList = compactList;
                break;
            case DETAILS_LIST:
                this.currentList = detailsList;
                break;
            default:
                this.currentList = iconsList;
        }

        this.getChildren().setAll((Node) this.currentList);

        VBox.setVgrow((Node) this.currentList, Priority.ALWAYS);

        this.updateSelection();
    }

    /**
     * Updates/refreshes the selection of the the currently visible {@link ListWidget}
     */
    private void updateSelection() {
        this.currentList.deselectAll();
        this.selectedItems.forEach(currentList::select);
    }

    @Override
    public void bind(ObservableList<E> list) {
        Bindings.bindContent(this.items, list);
    }

    @Override
    public void deselectAll() {
        this.selectedItems.clear();
        this.updateSelection();
    }

    @Override
    public void select(E item) {
        this.selectedItems.add(mappedItems.get(items.indexOf(item)));
        this.updateSelection();
    }

    @Override
    public Collection<E> getSelectedItems() {
        return this.selectedItems.stream().map(ListWidgetEntry::getItem).collect(Collectors.toList());
    }
}
