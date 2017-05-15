package org.phoenicis.javafx.views.common.widget;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.javafx.views.common.MappedList;

import java.net.URI;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by marc on 14.05.17.
 */
public class CompactListWidget<E> extends ListView<CompactListWidget.Element>
        implements ListWidget<ListWidgetEntry<E>> {
    private ObservableList<ListWidgetEntry<E>> items;
    private MappedList<Element<E>, ListWidgetEntry<E>> mappedElements;

    public CompactListWidget(BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.setPrefWidth(0);
        this.setPrefHeight(0);
        this.getStyleClass().add("iconListWidget");
        this.setCellFactory(param -> new ElementListCell());

        this.items = FXCollections.observableArrayList();
        this.mappedElements = new MappedList<Element<E>, ListWidgetEntry<E>>(items, value -> {
            Element<E> newElement = new Element<E>(value);

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

    private class ElementListCell<E> extends ListCell<Element<E>> {
        public ElementListCell() {
            super();

            this.setPrefWidth(0);
        }

        @Override
        public void updateItem(Element<E> item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty && item != null) {
                setGraphic(item);
            } else {
                setGraphic(new DummyElement<E>());
            }
        }
    }

    private class DummyElement<E> extends VBox {
        public DummyElement() {
            super();

            this.getStyleClass().add("iconListCell");
        }
    }

    class Element<E> extends GridPane {
        private E element;
        private URI iconPath;
        private String title;

        private Region icon;

        private Label titleLabel;

        public Element(ListWidgetEntry<E> item) {
            super();

            this.element = item.getItem();
            this.title = item.getTitle();
            this.iconPath = item.getIconUri();

            this.getStyleClass().add("iconListCell");

            this.icon = new Region();
            this.icon.getStyleClass().add("iconListMiniatureImage");
            this.icon.setStyle(String.format("-fx-background-image: url(\"%s\");", iconPath.toString()));

            this.titleLabel = new Label(title);
            this.titleLabel.setWrapText(true);

            this.add(icon, 0, 0);
            this.add(titleLabel, 1, 0);

            GridPane.setHgrow(titleLabel, Priority.ALWAYS);
        }

        public E getElement() {
            return element;
        }
    }
}
