package org.phoenicis.javafx.views.common.widgets.lists.details;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.control.DetailsListElement;
import org.phoenicis.javafx.views.common.widgets.lists.ListElementListCell;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidget;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link ListWidget} for a details list containing a title and a set of additional information for each list entry.
 *
 * @author marc
 * @since 15.05.17
 */
public class DetailsListWidget<E> extends ListView<DetailsListElement<E>> implements ListWidget<E> {
    /**
     * A list containing all items to be shown inside this {@link ListWidget}
     */
    private ObservableList<E> items;

    /**
     * A list containing all mapped {@link DetailsListElement} objects to the <code>items</code>
     */
    private MappedList<DetailsListElement<E>, E> mappedElements;

    /**
     * Constructor
     *
     * @param converter A converter function used to convert a value of input type <code>E</code> to
     *            {@link DetailsListElement}
     * @param setOnMouseClicked An event listener function to be called when a list element has been selected/clicked
     */
    public DetailsListWidget(Function<E, DetailsListElement<E>> converter,
            BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.setPrefWidth(0);
        this.setPrefHeight(0);
        this.getStyleClass().addAll("listWidget", "detailsListWidget");
        this.setCellFactory(param -> new ListElementListCell<DetailsListElement<E>>());

        this.items = FXCollections.observableArrayList();
        this.mappedElements = new MappedList<>(items, value -> {
            DetailsListElement<E> newElement = converter.apply(value);

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
