package org.phoenicis.javafx.views.common.widget;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.views.common.MappedList;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by marc on 15.05.17.
 */
public class CombinedListWidget<E> extends VBox implements ListWidget<E> {
    private ObservableList<E> items;

    private MappedList<ListWidgetEntry<E>, E> mappedItems;

    private IconsListWidget<E> iconsList;
    private CompactListWidget<E> compactList;

    private ListWidget<ListWidgetEntry<E>> currentList;

    public CombinedListWidget(Function<E, ListWidgetEntry<E>> converter, BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.setPrefHeight(0);
        this.setPrefWidth(0);

        this.items = FXCollections.observableArrayList();
        this.mappedItems = new MappedList<>(items, converter);

        BiConsumer<E, MouseEvent> proxy = (element, event) -> {
            this.deselectAll();
            setOnMouseClicked.accept(element, event);
            this.select(element);
        };

        this.iconsList = new IconsListWidget<>(proxy);
        this.compactList = new CompactListWidget<>(proxy);

        this.iconsList.bind(mappedItems);
        this.compactList.bind(mappedItems);

        this.showList(ListWidgetType.ICONS_LIST);
    }

    public void showList(ListWidgetType type) {
        switch (type) {
            case ICONS_LIST:
                this.currentList = iconsList;
                break;
            case COMPACT_LIST:
                this.currentList = compactList;
                break;
            default:
                this.currentList = iconsList;
        }

        this.getChildren().setAll((Node) this.currentList);

        VBox.setVgrow((Node) this.currentList, Priority.ALWAYS);
    }

    @Override
    public void bind(ObservableList<E> list) {
        Bindings.bindContent(this.items, list);
    }

    @Override
    public void deselectAll() {
        this.iconsList.deselectAll();
        this.compactList.deselectAll();
    }

    @Override
    public void select(E item) {
        ListWidgetEntry<E> entry = mappedItems.get(items.indexOf(item));

        this.iconsList.select(entry);
        this.compactList.select(entry);
    }

    @Override
    public E getSelectedItem() {
        return this.currentList.getSelectedItem().getItem();
    }
}
