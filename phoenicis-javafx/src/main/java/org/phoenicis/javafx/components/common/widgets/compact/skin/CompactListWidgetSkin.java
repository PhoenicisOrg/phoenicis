package org.phoenicis.javafx.components.common.widgets.compact.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.common.widgets.compact.control.CompactListElement;
import org.phoenicis.javafx.components.common.widgets.compact.control.CompactListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetSelection;

import java.util.Optional;

/**
 * The skin for the {@link CompactListWidget} component
 *
 * @param <E> The concrete type of the elements shown in this list widget
 */
public class CompactListWidgetSkin<E> extends SkinBase<CompactListWidget<E>, CompactListWidgetSkin<E>> {
    /**
     * Mapped list between the input {@link ListWidgetElement} and {@link CompactListElement}
     */
    private final ObservableList<CompactListElement<E>> mappedElements;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public CompactListWidgetSkin(CompactListWidget<E> control) {
        super(control);

        this.mappedElements = new MappedList<>(getControl().getElements(), value -> {
            CompactListElement<E> newElement = new CompactListElement<>(value);

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
        final ListView<CompactListElement<E>> container = new ListView<>();
        container.getStyleClass().addAll("listWidget", "compactListWidget");

        container.setPrefWidth(0);
        container.setPrefHeight(0);

        // ensure that empty rows have the same height as non-empty ones
        container.setCellFactory(param -> {
            final ListCell<CompactListElement<E>> listCell = new ListCell<CompactListElement<E>>() {
                @Override
                public void updateItem(CompactListElement<E> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && item != null) {
                        setGraphic(item);
                    } else {
                        setGraphic(null);
                    }
                }
            };

            listCell.getStyleClass().addAll("compactListElement");

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
    private void updateOldSelection(ListView<CompactListElement<E>> container, ListWidgetSelection<E> oldSelection) {
        // deselect the old element
        Optional.ofNullable(oldSelection).map(ListWidgetSelection::getSelection).ifPresent(selection -> {
            final int oldValueIndex = getControl().getElements().indexOf(selection);

            // In some corner cases (category / filter changes, the current selection can disappear)
            if (mappedElements.size() > oldValueIndex && oldValueIndex != -1) {
                container.getSelectionModel().clearSelection(oldValueIndex);
            }
        });
    }

    /**
     * Select the current/new selection
     *
     * @param container The list view in which the selection should be updated
     * @param newSelection The current/new selection
     */
    private void updateNewSelection(ListView<CompactListElement<E>> container, ListWidgetSelection<E> newSelection) {
        // select the new element
        Optional.ofNullable(newSelection).map(ListWidgetSelection::getSelection).ifPresent(selection -> {
            final int newValueIndex = getControl().getElements().indexOf(selection);

            container.getSelectionModel().select(newValueIndex);
        });
    }
}
