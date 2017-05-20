package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by marc on 15.05.17.
 */
public class LeftListWidgetChooser<E> extends HBox {
    private List<CombinedListWidget<E>> listWidgets;

    private ToggleGroup toggleGroup;

    private ToggleButton iconsListButton;
    private ToggleButton compactListButton;
    private ToggleButton detailsListButton;

    public LeftListWidgetChooser(CombinedListWidget<E> listWidget) {
        this(Arrays.asList(listWidget));
    }

    public LeftListWidgetChooser(List<CombinedListWidget<E>> listWidgets) {
        super();

        this.listWidgets = listWidgets;

        this.getStyleClass().add("listChooser");

        this.toggleGroup = new ToggleGroup();

        this.iconsListButton = new ToggleButton();
        this.iconsListButton.setToggleGroup(toggleGroup);
        this.iconsListButton.getStyleClass().addAll("listIcon", "iconsList");
        this.iconsListButton
                .setOnAction(event -> listWidgets.forEach(widget -> widget.showList(ListWidgetType.ICONS_LIST)));

        this.compactListButton = new ToggleButton();
        this.compactListButton.setToggleGroup(toggleGroup);
        this.compactListButton.getStyleClass().addAll("listIcon", "compactList");
        this.compactListButton
                .setOnAction(event -> listWidgets.forEach(widget -> widget.showList(ListWidgetType.COMPACT_LIST)));

        this.detailsListButton = new ToggleButton();
        this.detailsListButton.setToggleGroup(toggleGroup);
        this.detailsListButton.getStyleClass().addAll("listIcon", "detailsList");
        this.detailsListButton
                .setOnAction(event -> listWidgets.forEach(widget -> widget.showList(ListWidgetType.DETAILS_LIST)));

        this.iconsListButton.setSelected(true);

        this.getChildren().setAll(iconsListButton, compactListButton, detailsListButton);

        this.listWidgets.forEach(widget -> widget.showList(ListWidgetType.ICONS_LIST));
    }
}
