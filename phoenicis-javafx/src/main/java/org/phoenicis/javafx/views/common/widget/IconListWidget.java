package org.phoenicis.javafx.views.common.widget;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.javafx.views.common.MappedList;

import java.net.URI;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by marc on 14.05.17.
 */
public class IconListWidget<E> extends ListView<IconListWidget.Element<E>> {
    private Element<E> selectedItem;

    private ObservableList<E> items;
    private MappedList<Element<E>, E> mappedElements;

    public IconListWidget(Function<E, Element> converter, BiConsumer<Element<E>, MouseEvent> setOnMouseClicked) {
        super();

        this.setPrefWidth(0);
        this.setPrefHeight(0);
        this.getStyleClass().add("iconListWidget");
        this.setCellFactory(param -> new ElementListCell<E>());

        this.items = FXCollections.observableArrayList();
        this.mappedElements = new MappedList<Element<E>, E>(items, value -> {
            Element newElement = converter.apply(value);

            newElement.setOnMouseClicked(event -> {
                setOnMouseClicked.accept(newElement, event);
            });

            return newElement;
        });

        Bindings.bindContent(super.getItems(), this.mappedElements);
    }

    public void bind(ObservableList<E> items) {
        Bindings.bindContent(this.items, items);
    }

    public static class ElementListCell<E> extends ListCell<Element<E>> {
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

    public static class DummyElement<E> extends VBox {
        public DummyElement() {
            super();

            this.getStyleClass().add("iconListCell");
        }
    }

    public static class Element<E> extends GridPane {
        private E element;
        private URI iconPath;
        private String title;

        private Region icon;

        private Label titleLabel;

        public Element(E element, String title, URI iconPath) {
            super();

            this.element = element;
            this.title = title;
            this.iconPath = iconPath;

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

        public static Element<ApplicationDTO> create(ApplicationDTO application) {
            return new Element<ApplicationDTO>(application, application.getName(), application.getMiniatures().get(0));
        }

        public E getElement() {
            return element;
        }
    }
}
