package org.phoenicis.javafx.views.common.widget;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.javafx.views.common.MappedList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Created by marc on 15.05.17.
 */
public class DetailsListWidget<E> extends ListView<DetailsListWidget.Element>
        implements ListWidget<ListWidgetEntry<E>> {
    private ObservableList<ListWidgetEntry<E>> items;
    private MappedList<Element<E>, ListWidgetEntry<E>> mappedElements;

    public DetailsListWidget(BiConsumer<E, MouseEvent> setOnMouseClicked) {
        super();

        this.setPrefWidth(0);
        this.setPrefHeight(0);
        this.getStyleClass().add("detailsListWidget");
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
                setGraphic(new DummyElement());
            }
        }
    }

    private class DummyElement extends VBox {
        public DummyElement() {
            super();

            this.getStyleClass().add("iconListCell");
        }
    }

    public class Element<E> extends GridPane {
        private E element;

        private String title;

        private Region icon;

        private Label titleLabel;

        public Element(ListWidgetEntry<E> item) {
            super();

            this.element = item.getItem();
            this.title = item.getTitle();

            this.getStyleClass().add("iconListCell");

            this.titleLabel = new Label(title);
            this.titleLabel.setWrapText(true);
            this.titleLabel.getStyleClass().add("information");

            List<ColumnConstraints> constraints = new ArrayList<>();
            constraints.add(new ColumnConstraintsWithPercentage(30));

            this.add(titleLabel, 0, 0);

            item.getAdditionalInformation()
                    .ifPresent(additionInformations -> additionInformations.forEach(information -> {
                        constraints.add(new ColumnConstraintsWithPercentage(information.getWidth()));

                        Label informationLabel = new Label(information.getContent());
                        informationLabel.setWrapText(true);
                        informationLabel.getStyleClass().add("information");

                        this.add(informationLabel, this.getChildren().size(), 0);
                    }));

            item.getDetailedInformation()
                    .ifPresent(additionInformations -> additionInformations.forEach(information -> {
                        constraints.add(new ColumnConstraintsWithPercentage(information.getWidth()));

                        Label informationLabel = new Label(information.getContent());
                        informationLabel.setWrapText(true);
                        informationLabel.getStyleClass().add("information");

                        this.add(informationLabel, this.getChildren().size(), 0);
                    }));

            constraints.set(constraints.size() - 1, new ColumnConstraints());

            this.getColumnConstraints().setAll(constraints);
        }

        public E getElement() {
            return element;
        }
    }
}
