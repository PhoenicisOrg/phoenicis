package org.phoenicis.javafx.components.common.skin;

import com.google.common.collect.ImmutableMap;
import javafx.beans.Observable;
import javafx.scene.Node;
import org.phoenicis.javafx.components.common.control.*;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;

import java.util.Map;

/**
 * The skin for the {@link CombinedListWidget} component
 *
 * @param <E> The concrete type of the elements shown in this list widget
 */
public class CombinedListWidgetSkin<E> extends SkinBase<CombinedListWidget<E>, CombinedListWidgetSkin<E>> {
    private final Map<ListWidgetType, ListWidgetBase<E, ?, ?>> listWidgets;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public CombinedListWidgetSkin(CombinedListWidget<E> control) {
        super(control);

        this.listWidgets = ImmutableMap.<ListWidgetType, ListWidgetBase<E, ?, ?>> builder()
                .put(ListWidgetType.ICONS_LIST, new IconsListWidget<>(
                        getControl().getElements(), getControl().selectedElementProperty()))
                .put(ListWidgetType.COMPACT_LIST, new CompactListWidget<>(
                        getControl().getElements(), getControl().selectedElementProperty()))
                .put(ListWidgetType.DETAILS_LIST, new DetailsListWidget<>(
                        getControl().getElements(), getControl().selectedElementProperty()))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        // ensure that updates to the property are automatically reflected in the view
        getControl().selectedListWidgetProperty().addListener((Observable invalidation) -> selectListWidget());
        // ensure that the correct list widget is shown at startup
        selectListWidget();
    }

    /**
     * Updates the shown list widget based on the current selection status
     */
    private void selectListWidget() {
        final ListWidgetType listWidgetType = getControl().getSelectedListWidget();

        final ListWidgetBase<E, ?, ?> currentListWidget = listWidgets.get(listWidgetType);

        getChildren().setAll(currentListWidget);
    }
}
