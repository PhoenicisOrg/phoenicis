package org.phoenicis.javafx.components.common.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.control.DetailsListElement;
import org.phoenicis.javafx.components.common.control.DetailsListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListElementListCell;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetSelection;

/**
 * The skin for the {@link DetailsListWidget} component
 *
 * @param <E> The concrete type of the elements shown in this list widget
 */
public class DetailsListWidgetSkin<E> extends SkinBase<DetailsListWidget<E>, DetailsListWidgetSkin<E>> {
    /**
     * Mapped list between the input {@link ListWidgetEntry} and {@link DetailsListElement}
     */
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

            newElement.setOnMouseClicked(
                    event -> getControl().setSelectedElement(new ListWidgetSelection<>(value, event)));

            return newElement;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final ListView<DetailsListElement<E>> container = new ListView<>();
        container.getStyleClass().addAll("listWidget", "detailsListWidget");

        container.setPrefWidth(0);
        container.setPrefHeight(0);

        container.setCellFactory(param -> new ListElementListCell<>());

        // ensure that updates to the selected element property are automatically reflected in the view
        getControl().selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            // deselect the old element
            if (oldValue != null) {
                final int oldValueIndex = getControl().getElements().indexOf(oldValue.getSelection());

                container.getSelectionModel().clearSelection(oldValueIndex);
            }

            // select the new element
            if (newValue != null) {
                final int newValueIndex = getControl().getElements().indexOf(newValue.getSelection());

                container.getSelectionModel().select(newValueIndex);
            }
        });

        Bindings.bindContent(container.getItems(), mappedElements);

        getChildren().addAll(container);
    }
}
