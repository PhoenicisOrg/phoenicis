package org.phoenicis.javafx.components.common.skin;

import javafx.beans.Observable;
import javafx.scene.Node;
import org.phoenicis.javafx.components.common.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.control.CompactListWidget;
import org.phoenicis.javafx.components.common.control.DetailsListWidget;
import org.phoenicis.javafx.components.common.control.IconsListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;

/**
 * The skin for the {@link CombinedListWidget} component
 *
 * @param <E> The concrete type of the elements shown in this list widget
 */
public class CombinedListWidgetSkin<E> extends SkinBase<CombinedListWidget<E>, CombinedListWidgetSkin<E>> {
    private final IconsListWidget<E> iconsListWidget;
    private final CompactListWidget<E> compactListWidget;
    private final DetailsListWidget<E> detailsListWidget;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public CombinedListWidgetSkin(CombinedListWidget<E> control) {
        super(control);

        this.iconsListWidget = new IconsListWidget<>(
                getControl().getElements(), getControl().selectedElementProperty());

        this.compactListWidget = new CompactListWidget<>(
                getControl().getElements(), getControl().selectedElementProperty());

        this.detailsListWidget = new DetailsListWidget<>(
                getControl().getElements(), getControl().selectedElementProperty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        // ensure, that updates to the property are automatically reflected in the view
        getControl().selectedListWidgetProperty().addListener((Observable invalidation) -> selectListWidget());
        // ensure, that the correct list widget is shown at startup
        selectListWidget();
    }

    /**
     * Updates the shown list widget based on the current selection status
     */
    private void selectListWidget() {
        final ListWidgetType listWidgetType = getControl().getSelectedListWidget();

        final Node currentListWidget = getListWidget(listWidgetType);

        getChildren().setAll(currentListWidget);
    }

    /**
     * Gets the list widget which belongs to the given {@link ListWidgetType}
     *
     * @param listWidgetType The list widget type
     * @return The list widget belonging to the given {@link ListWidgetType}
     */
    private Node getListWidget(final ListWidgetType listWidgetType) {
        switch (listWidgetType) {
            case ICONS_LIST:
                return iconsListWidget;
            case COMPACT_LIST:
                return compactListWidget;
            case DETAILS_LIST:
                return detailsListWidget;
            default:
                return iconsListWidget;
        }
    }
}
