package org.phoenicis.javafx.components.common.skin;

import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.components.common.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.control.CompactListWidget;
import org.phoenicis.javafx.components.common.control.DetailsListWidget;
import org.phoenicis.javafx.components.common.control.IconsListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;

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

    @Override
    public void initialise() {
        final VBox container = new VBox();

        container.setPrefWidth(0);
        container.setPrefHeight(0);

        getControl().selectedListWidgetProperty().addListener((Observable invalidation) -> selectListWidget(container));
        selectListWidget(container);

        getChildren().addAll(container);
    }

    private void selectListWidget(VBox container) {
        final ListWidgetType listWidgetType = getControl().getSelectedListWidget();

        final Node currentList = getListWidget(listWidgetType);

        container.getChildren().setAll(currentList);

        VBox.setVgrow(currentList, Priority.ALWAYS);
    }

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
