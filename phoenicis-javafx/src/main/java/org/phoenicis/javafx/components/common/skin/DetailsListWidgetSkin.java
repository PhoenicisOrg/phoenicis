package org.phoenicis.javafx.components.common.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.control.DetailsListElement;
import org.phoenicis.javafx.components.common.control.DetailsListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListElementListCell;

public class DetailsListWidgetSkin<E> extends SkinBase<DetailsListWidget<E>, DetailsListWidgetSkin<E>> {
    private final ObservableList<DetailsListElement<E>> mappedElements;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public DetailsListWidgetSkin(DetailsListWidget<E> control) {
        super(control);

        this.mappedElements = new MappedList<>(getControl().getElements(), value -> {
            DetailsListElement<E> newElement = new DetailsListElement<>(value);

            newElement.setOnMouseClicked(event -> getControl().setSelectedElement(value));

            return newElement;
        });
    }

    @Override
    public void initialise() {
        ListView<DetailsListElement<E>> container = new ListView<>();

        container.setPrefWidth(0);
        container.setPrefHeight(0);
        container.getStyleClass().addAll("listWidget", "detailsListWidget");
        container.setCellFactory(param -> new ListElementListCell<>());

        getControl().selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                final int oldValueIndex = getControl().getElements().indexOf(oldValue);

                container.getSelectionModel().clearSelection(oldValueIndex);
            }

            if (newValue != null) {
                final int newValueIndex = getControl().getElements().indexOf(newValue);

                container.getSelectionModel().select(newValueIndex);
            }
        });

        Bindings.bindContent(container.getItems(), mappedElements);

        getChildren().addAll(container);
    }
}
