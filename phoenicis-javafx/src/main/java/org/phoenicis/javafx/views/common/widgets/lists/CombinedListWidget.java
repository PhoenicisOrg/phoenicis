package org.phoenicis.javafx.views.common.widgets.lists;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.views.common.widgets.lists.compact.CompactListElement;
import org.phoenicis.javafx.views.common.widgets.lists.compact.CompactListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.details.DetailsListElement;
import org.phoenicis.javafx.views.common.widgets.lists.details.DetailsListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.icons.IconsListElement;
import org.phoenicis.javafx.views.common.widgets.lists.icons.IconsListWidget;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A {@link ListWidget} fusing all other {@link ListWidget}s.
 * Using this {@link ListWidget} enables an easy swapping of the other {@link ListWidget}s at runtime.
 *
 * @author marc
 * @since 15.05.17
 */
public class CombinedListWidget<E> extends VBox implements ListWidget<E> {
    private final Function<E, ListWidgetEntry<E>> converter;

    private final BiConsumer<E, MouseEvent> setOnMouseClicked;

    /**
     * A set of all currently selected items/entries
     */
    private Set<E> selectedItems;

    /**
     * A list of all currently visible items
     */
    private ObservableList<E> items;

    private IconsListWidget<E> iconsList;
    private CompactListWidget<E> compactList;
    private DetailsListWidget<E> detailsList;

    /**
     * The currently shown {@link ListWidget}
     */
    private ListWidget<E> currentList;

    /**
     * Constructor
     *
     * @param converter A converter function, that takes an object of type <code>E</code> and returns a
     *            {@link ListWidgetEntry} for it
     * @param setOnMouseClicked An event listener function to be called when an entry has been selected/clicked
     */
    public CombinedListWidget(ObservableList<E> items, Function<E, ListWidgetEntry<E>> converter,
            BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.items = items;
        this.converter = converter;
        this.setOnMouseClicked = setOnMouseClicked;

        this.selectedItems = new HashSet<>();

        initialise();
    }

    /**
     * Constructor
     *
     * @param converter A converter function, that takes an object of type <code>E</code> and returns a
     *            {@link ListWidgetEntry} for it
     * @param setOnMouseClicked An event listener function to be called when an entry has been selected/clicked
     */
    public CombinedListWidget(Function<E, ListWidgetEntry<E>> converter, BiConsumer<E, MouseEvent> setOnMouseClicked) {
        this(FXCollections.observableArrayList(), converter, setOnMouseClicked);
    }

    private void initialise() {
        setPrefHeight(0);
        setPrefWidth(0);

        BiConsumer<E, MouseEvent> proxy = (element, event) -> {
            deselectAll();
            setOnMouseClicked.accept(element, event);
            select(element);
        };

        this.iconsList = new IconsListWidget<>(item -> IconsListElement.create(converter.apply(item)), proxy);
        this.compactList = new CompactListWidget<>(item -> CompactListElement.create(converter.apply(item)), proxy);
        this.detailsList = new DetailsListWidget<>(item -> DetailsListElement.create(converter.apply(item)), proxy);

        iconsList.bind(items);
        compactList.bind(items);
        detailsList.bind(items);

        showList(ListWidgetType.ICONS_LIST);
    }

    /**
     * Shows the list of the given <code>type</code>
     *
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
        currentList.deselectAll();
        selectedItems.forEach(currentList::select);
    }

    @Override
    public void bind(ObservableList<E> list) {
        Bindings.bindContent(items, list);
    }

    @Override
    public void deselectAll() {
        selectedItems.clear();
        updateSelection();
    }

    @Override
    public void select(E item) {
        selectedItems.add(item);
        updateSelection();
    }

    @Override
    public Collection<E> getSelectedItems() {
        return selectedItems;
    }
}
