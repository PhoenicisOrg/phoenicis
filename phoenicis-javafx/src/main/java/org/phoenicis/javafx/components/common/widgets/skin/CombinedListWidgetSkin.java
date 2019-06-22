package org.phoenicis.javafx.components.common.widgets.skin;

import com.google.common.collect.ImmutableMap;
import javafx.beans.Observable;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.common.widgets.compact.control.CompactListWidget;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.widgets.control.ListWidgetBase;
import org.phoenicis.javafx.components.common.widgets.details.control.DetailsListWidget;
import org.phoenicis.javafx.components.common.widgets.icons.control.IconsListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;

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
