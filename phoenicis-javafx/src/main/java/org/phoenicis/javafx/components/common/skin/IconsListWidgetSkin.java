package org.phoenicis.javafx.components.common.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.CacheHint;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.control.IconsListElement;
import org.phoenicis.javafx.components.common.control.IconsListWidget;

public class IconsListWidgetSkin<E> extends SkinBase<IconsListWidget<E>, IconsListWidgetSkin<E>> {
    private final ObservableList<IconsListElement<E>> mappedElements;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public IconsListWidgetSkin(IconsListWidget<E> control) {
        super(control);

        this.mappedElements = new MappedList<>(getControl().getElements(), value -> {
            IconsListElement<E> newElement = new IconsListElement<>(value);

            newElement.setOnMouseClicked(event -> getControl().setSelectedElement(value));

            return newElement;
        });
    }

    @Override
    public void initialise() {
        final FlowPane container = new FlowPane();

        container.setPrefHeight(0);
        container.setPrefWidth(0);

        final ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.getStyleClass().add("iconListWidget");

        scrollPane.setCache(true);
        scrollPane.setCacheHint(CacheHint.QUALITY);

        container.prefWidthProperty().bind(scrollPane.widthProperty());

        getControl().selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                final int oldValueIndex = getControl().getElements().indexOf(oldValue);

                IconsListElement<E> oldElement = mappedElements.get(oldValueIndex);

                oldElement.setSelected(false);
            }

            if (newValue != null) {
                final int newValueIndex = getControl().getElements().indexOf(newValue);

                IconsListElement<E> newElement = mappedElements.get(newValueIndex);

                newElement.setSelected(true);
            }
        });

        Bindings.bindContent(container.getChildren(), mappedElements);

        getChildren().addAll(scrollPane);
    }
}
