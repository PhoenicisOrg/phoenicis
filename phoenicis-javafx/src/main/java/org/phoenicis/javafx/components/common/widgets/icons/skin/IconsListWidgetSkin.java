package org.phoenicis.javafx.components.common.widgets.icons.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.CacheHint;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.widgets.icons.control.IconsListElement;
import org.phoenicis.javafx.components.common.widgets.icons.control.IconsListWidget;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetSelection;

/**
 * The skin for the {@link IconsListWidget} component
 *
 * @param <E> The concrete type of the elements shown in this list widget
 */
public class IconsListWidgetSkin<E> extends SkinBase<IconsListWidget<E>, IconsListWidgetSkin<E>> {
    /**
     * Mapped list between the input {@link ListWidgetElement} and {@link IconsListElement}
     */
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
        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("iconListWidget");

        scrollPane.setCache(true);
        scrollPane.setCacheHint(CacheHint.QUALITY);

        scrollPane.setContent(createContent(scrollPane));

        getChildren().addAll(scrollPane);
    }

    /**
     * Creates the {@link FlowPane} which contains the icons of the icons list
     *
     * @param container The scroll pane container which will contain the {@link FlowPane}
     * @return The new {@link FlowPane}
     */
    private FlowPane createContent(final ScrollPane container) {
        final FlowPane content = new FlowPane();

        content.prefWidthProperty().bind(container.widthProperty());
        content.setPrefHeight(0);

        // ensure that updates to the selected element property are automatically reflected in the view
        getControl().selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            // deselect the old element
            if (oldValue != null) {
                final int oldValueIndex = getControl().getElements().indexOf(oldValue.getSelection());

                IconsListElement<E> oldElement = mappedElements.get(oldValueIndex);

                oldElement.setSelected(false);
            }

            // select the new element
            if (newValue != null) {
                final int newValueIndex = getControl().getElements().indexOf(newValue.getSelection());

                IconsListElement<E> newElement = mappedElements.get(newValueIndex);

                newElement.setSelected(true);
            }
        });

        Bindings.bindContent(content.getChildren(), mappedElements);

        return content;
    }
}
