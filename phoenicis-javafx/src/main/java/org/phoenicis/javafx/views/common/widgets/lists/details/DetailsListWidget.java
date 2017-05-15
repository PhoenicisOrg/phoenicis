package org.phoenicis.javafx.views.common.widgets.lists.details;

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
 * Created by marc on 15.05.17.
 */
public class DetailsListWidget<E> extends ListView<DetailsListElement<E>> implements ListWidget<ListWidgetEntry<E>> {
    private ObservableList<ListWidgetEntry<E>> items;
    private MappedList<DetailsListElement<E>, ListWidgetEntry<E>> mappedElements;

    public DetailsListWidget(BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.setPrefWidth(0);
        this.setPrefHeight(0);
        this.getStyleClass().add("detailsListWidget");
        this.setCellFactory(param -> new ListElementListCell<DetailsListElement<E>>());

        this.items = FXCollections.observableArrayList();
        this.mappedElements = new MappedList<DetailsListElement<E>, ListWidgetEntry<E>>(items, value -> {
            DetailsListElement<E> newElement = new DetailsListElement<E>(value);

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
