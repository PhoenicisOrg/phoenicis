package org.phoenicis.javafx.components.behavior;

import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.control.ListWidgetSelector;
import org.phoenicis.javafx.components.skin.ListWidgetSelectorSkin;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;

import java.util.Optional;
import java.util.function.Consumer;

public class ListWidgetSelectorBehavior
        extends BehaviorBase<ListWidgetSelector, ListWidgetSelectorSkin, ListWidgetSelectorBehavior> {
    /**
     * Constructor
     *
     * @param control The control associated with this behavior
     * @param skin The skin associated with this behavior
     */
    public ListWidgetSelectorBehavior(ListWidgetSelector control, ListWidgetSelectorSkin skin) {
        super(control, skin);
    }

    @Override
    public void initialise() {
        getSkin().getIconsListButton().addEventFilter(ActionEvent.ANY, this::eventFilter);
        getSkin().getIconsListButton()
                .setOnAction(event -> {
                    getControl().setSelected(ListWidgetType.ICONS_LIST);
                    getOnSelect().ifPresent(consumer -> consumer.accept(ListWidgetType.ICONS_LIST));
                });

        getSkin().getCompactListButton().addEventFilter(ActionEvent.ANY, this::eventFilter);
        getSkin().getCompactListButton()
                .setOnAction(event -> {
                    getControl().setSelected(ListWidgetType.COMPACT_LIST);
                    getOnSelect().ifPresent(consumer -> consumer.accept(ListWidgetType.COMPACT_LIST));
                });

        getSkin().getDetailsListButton().addEventFilter(ActionEvent.ANY, this::eventFilter);
        getSkin().getDetailsListButton()
                .setOnAction(event -> {
                    getControl().setSelected(ListWidgetType.DETAILS_LIST);
                    getOnSelect().ifPresent(consumer -> consumer.accept(ListWidgetType.DETAILS_LIST));
                });

        // ensure that if at startup a selected type is set select it
        Optional.ofNullable(getControl().getSelected())
                .ifPresent(selected -> getSkin().getListButton(selected).fire());

        // change the selection based on a change in the selected property
        getControl().selectedProperty()
                .addListener(invalidatedEvent -> getSkin().getListButton(getControl().getSelected()).fire());
    }

    /**
     * An event filter to prevent the unselectation of all buttons
     *
     * @param event The input event to be filtered
     */
    private void eventFilter(ActionEvent event) {
        ToggleButton source = (ToggleButton) event.getSource();
        if (source.getToggleGroup() == null || !source.isSelected()) {
            source.fire();
        }
    }

    private Optional<Consumer<ListWidgetType>> getOnSelect() {
        return Optional.ofNullable(getControl().getOnSelect());
    }
}
