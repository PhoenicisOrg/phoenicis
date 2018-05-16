package org.phoenicis.javafx.views.common.widgets.lists.compact;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.phoenicis.javafx.views.common.lists.MappedList;
import org.phoenicis.javafx.views.common.widgets.lists.ListElementListCell;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidget;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link ListWidget} for a compact list containing a small miniature image, a title and a set of additional
 * information for each list entry.
 *
 * @author marc
 * @since 14.05.17
 */
public class CompactListWidget<E> extends ListView<CompactListElement<E>> implements ListWidget<E> {
    /**
     * A list containing all items to be shown inside this {@link ListWidget}
     */
    private ObservableList<E> items;

    /**
     * A list containing all mapped {@link CompactListElement} objects to the <code>items</code>
     */
    private MappedList<CompactListElement<E>, E> mappedElements;

    /**
     * Constructor
     *
     * @param converter A converter function used to convert a value of input type <code>E</code> to
     *            {@link CompactListElement}
     * @param setOnMouseClicked An event listener function to be called when a list element has been selected/clicked
     */
    public CompactListWidget(Function<E, CompactListElement<E>> converter,
            BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.setPrefWidth(0);
        this.setPrefHeight(0);
        this.getStyleClass().addAll("listWidget", "compactListWidget");
        this.setCellFactory(param -> new ListElementListCell<CompactListElement<E>>());

        this.items = FXCollections.observableArrayList();
        this.mappedElements = new MappedList<>(items, value -> {
            CompactListElement<E> newElement = converter.apply(value);

            newElement.setOnMouseClicked(event -> {
                this.deselectAll();
                setOnMouseClicked.accept(value, event);
                this.select(value);
            });

            return newElement;
        });

        Bindings.bindContent(super.getItems(), this.mappedElements);
    }

    @Override
    public void bind(ObservableList<E> items) {
        Bindings.bindContent(this.items, items);
    }

    @Override
    public void deselectAll() {
        this.getSelectionModel().clearSelection();
    }

    @Override
    public void select(E item) {
        this.getSelectionModel().select(this.items.indexOf(item));
    }

    @Override
    public Collection<E> getSelectedItems() {
        return this.getSelectionModel().getSelectedIndices().stream().map(index -> items.get(index))
                .collect(Collectors.toList());
    }
}
