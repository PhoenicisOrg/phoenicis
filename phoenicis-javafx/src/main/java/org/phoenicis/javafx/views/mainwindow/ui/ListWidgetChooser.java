package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by marc on 15.05.17.
 */
public class ListWidgetChooser<E> extends HBox {
    private final Logger LOGGER = LoggerFactory.getLogger(ListWidgetChooser.class);

    private List<CombinedListWidget<E>> listWidgets;

    private ToggleGroup toggleGroup;

    private ToggleButton iconsListButton;
    private ToggleButton compactListButton;
    private ToggleButton detailsListButton;

    /**
     * consumer which is executed when a different the list type is chosen
     */
    private Optional<Consumer<ListWidgetType>> onChoose;

    public ListWidgetChooser(CombinedListWidget<E> listWidget) {
        this(Arrays.asList(listWidget));
    }

    public ListWidgetChooser(List<CombinedListWidget<E>> listWidgets) {
        super();

        this.listWidgets = listWidgets;

        this.getStyleClass().add("listChooser");

        this.onChoose = Optional.empty();

        this.toggleGroup = new ToggleGroup();

        // prevent unselecting all buttons
        EventHandler filter = (EventHandler<ActionEvent>) actionEvent -> {
            ToggleButton source = (ToggleButton) actionEvent.getSource();
            if (source.getToggleGroup() == null || !source.isSelected()) {
                source.fire();
            }
        };

        this.iconsListButton = new ToggleButton();
        this.iconsListButton.setToggleGroup(toggleGroup);
        this.iconsListButton.getStyleClass().addAll("listIcon", "iconsList");
        this.iconsListButton
                .setOnAction(event -> {
                    listWidgets.forEach(widget -> widget.showList(ListWidgetType.ICONS_LIST));
                    this.onChoose.ifPresent(consumer -> consumer.accept(ListWidgetType.ICONS_LIST));
                });
        this.iconsListButton.addEventFilter(ActionEvent.ANY, filter);

        this.compactListButton = new ToggleButton();
        this.compactListButton.setToggleGroup(toggleGroup);
        this.compactListButton.getStyleClass().addAll("listIcon", "compactList");
        this.compactListButton
                .setOnAction(event -> {
                    listWidgets.forEach(widget -> widget.showList(ListWidgetType.COMPACT_LIST));
                    this.onChoose.ifPresent(consumer -> consumer.accept(ListWidgetType.COMPACT_LIST));
                });
        this.compactListButton.addEventFilter(ActionEvent.ANY, filter);

        this.detailsListButton = new ToggleButton();
        this.detailsListButton.setToggleGroup(toggleGroup);
        this.detailsListButton.getStyleClass().addAll("listIcon", "detailsList");
        this.detailsListButton
                .setOnAction(event -> {
                    listWidgets.forEach(widget -> widget.showList(ListWidgetType.DETAILS_LIST));
                    this.onChoose.ifPresent(consumer -> consumer.accept(ListWidgetType.DETAILS_LIST));
                });
        this.detailsListButton.addEventFilter(ActionEvent.ANY, filter);

        this.getChildren().setAll(iconsListButton, compactListButton, detailsListButton);

        this.choose(ListWidgetType.ICONS_LIST);
    }

    public void choose(ListWidgetType type) {
        switch (type) {
            case COMPACT_LIST:
                this.compactListButton.fire();
                break;
            case DETAILS_LIST:
                this.detailsListButton.fire();
                break;
            case ICONS_LIST:
                this.iconsListButton.fire();
                break;
            default:
                LOGGER.error("Cannot select type " + type);
        }
    }

    public void setOnChoose(Consumer<ListWidgetType> onChoose) {
        this.onChoose = Optional.of(onChoose);
    }
}
