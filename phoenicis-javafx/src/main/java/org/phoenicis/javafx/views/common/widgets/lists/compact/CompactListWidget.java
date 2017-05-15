package org.phoenicis.javafx.views.common.widgets.lists.compact;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.phoenicis.javafx.views.common.MappedList;
import org.phoenicis.javafx.views.common.widgets.lists.ListElementListCell;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Created by marc on 14.05.17.
 */
public class CompactListWidget<E> extends ListView<CompactListElement<E>> implements ListWidget<ListWidgetEntry<E>> {
    private ObservableList<ListWidgetEntry<E>> items;
    private MappedList<CompactListElement<E>, ListWidgetEntry<E>> mappedElements;

    public CompactListWidget(BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.setPrefWidth(0);
        this.setPrefHeight(0);
        this.getStyleClass().add("compactListWidget");
        this.setCellFactory(param -> new ListElementListCell<CompactListElement<E>>());

        this.items = FXCollections.observableArrayList();
        this.mappedElements = new MappedList<CompactListElement<E>, ListWidgetEntry<E>>(items, value -> {
            CompactListElement<E> newElement = new CompactListElement<E>(value);

            newElement.setOnMouseClicked(event -> {
                this.deselectAll();
                setOnMouseClicked.accept(value.getItem(), event);
                this.select(value);
            });

            return newElement;
        });

        Bindings.bindContent(super.getItems(), this.mappedElements);
    }

    public void bind(ObservableList<ListWidgetEntry<E>> items) {
        Bindings.bindContent(this.items, items);
    }

    @Override
    public void deselectAll() {
        this.getSelectionModel().clearSelection();
    }

    @Override
    public void select(ListWidgetEntry<E> item) {
        this.getSelectionModel().select(this.items.indexOf(item));
    }

    @Override
    public Collection<ListWidgetEntry<E>> getSelectedItems() {
        return this.getSelectionModel().getSelectedIndices().stream().map(index -> items.get(index))
                .collect(Collectors.toList());
    }
}
