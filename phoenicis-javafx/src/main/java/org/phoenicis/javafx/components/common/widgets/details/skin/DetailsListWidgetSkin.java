package org.phoenicis.javafx.components.common.widgets.details.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.common.widgets.details.control.DetailsListElement;
import org.phoenicis.javafx.components.common.widgets.details.control.DetailsListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetSelection;

import java.util.Optional;

/**
 * The skin for the {@link DetailsListWidget} component
 *
 * @param <E> The concrete type of the elements shown in this list widget
 */
public class DetailsListWidgetSkin<E> extends SkinBase<DetailsListWidget<E>, DetailsListWidgetSkin<E>> {
    /**
     * Mapped list between the input {@link ListWidgetElement} and {@link DetailsListElement}
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

        // ensure that empty rows have the same height as non-empty ones
        container.setCellFactory(param -> {
            final ListCell<DetailsListElement<E>> listCell = new ListCell<DetailsListElement<E>>() {
                @Override
                public void updateItem(DetailsListElement<E> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && item != null) {
                        setGraphic(item);
                    } else {
                        setGraphic(null);
                    }
                }
            };

            listCell.getStyleClass().addAll("detailsListElement");

            return listCell;
        });

        Bindings.bindContent(container.getItems(), mappedElements);

        // ensure that updates to the selected element property are automatically reflected in the view
        getControl().selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            // deselect the old element
            updateOldSelection(container, oldValue);

            // select the new element
            updateNewSelection(container, newValue);
        });

        // initialise selection at startup
        updateNewSelection(container, getControl().getSelectedElement());

        getChildren().addAll(container);
    }

    /**
     * Deselect the old/previous selection
     *
     * @param container The list view in which the selection should be updated
     * @param oldSelection The old/previous selection
     */
    private void updateOldSelection(ListView<DetailsListElement<E>> container, ListWidgetSelection<E> oldSelection) {
        // deselect the old element
        Optional.ofNullable(oldSelection).map(ListWidgetSelection::getSelection).ifPresent(selection -> {
            final int oldValueIndex = getControl().getElements().indexOf(selection);

            container.getSelectionModel().clearSelection(oldValueIndex);
        });
    }

    /**
     * Select the current/new selection
     *
     * @param container The list view in which the selection should be updated
     * @param newSelection The current/new selection
     */
    private void updateNewSelection(ListView<DetailsListElement<E>> container, ListWidgetSelection<E> newSelection) {
        // select the new element
        Optional.ofNullable(newSelection).map(ListWidgetSelection::getSelection).ifPresent(selection -> {
            final int newValueIndex = getControl().getElements().indexOf(selection);

            container.getSelectionModel().select(newValueIndex);
        });
    }
}
