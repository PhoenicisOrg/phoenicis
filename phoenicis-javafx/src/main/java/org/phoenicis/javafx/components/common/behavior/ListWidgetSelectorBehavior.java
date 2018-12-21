package org.phoenicis.javafx.components.common.behavior;

import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.common.widgets.control.ListWidgetSelector;
import org.phoenicis.javafx.components.common.widgets.skin.ListWidgetSelectorSkin;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetType;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * The behavior for the {@link ListWidgetSelector} component
 */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        // set the behavior for the icons list toggle button
        getSkin().getIconsListButton().addEventFilter(ActionEvent.ANY, this::eventFilter);
        getSkin().getIconsListButton()
                .setOnAction(event -> {
                    getControl().setSelected(ListWidgetType.ICONS_LIST);
                    getOnSelect().ifPresent(consumer -> consumer.accept(ListWidgetType.ICONS_LIST));
                });

        // set the behavior for the compact list toggle button
        getSkin().getCompactListButton().addEventFilter(ActionEvent.ANY, this::eventFilter);
        getSkin().getCompactListButton()
                .setOnAction(event -> {
                    getControl().setSelected(ListWidgetType.COMPACT_LIST);
                    getOnSelect().ifPresent(consumer -> consumer.accept(ListWidgetType.COMPACT_LIST));
                });

        // set the behavior for the details list toggle button
        getSkin().getDetailsListButton().addEventFilter(ActionEvent.ANY, this::eventFilter);
        getSkin().getDetailsListButton()
                .setOnAction(event -> {
                    getControl().setSelected(ListWidgetType.DETAILS_LIST);
                    getOnSelect().ifPresent(consumer -> consumer.accept(ListWidgetType.DETAILS_LIST));
                });

        // perform the initial list widget selection based on the given input property, which normally represents the
        // user settings
        Optional.ofNullable(getControl().getSelected())
                .ifPresent(selected -> getSkin().getListButton(selected).fire());

        // change the selection based on a change in the selected property
        getControl().selectedProperty()
                .addListener(invalidatedEvent -> getSkin().getListButton(getControl().getSelected()).fire());
    }

    /**
     * An event filter to prevent the deselection of all buttons
     *
     * @param event The input event to be filtered
     */
    private void eventFilter(ActionEvent event) {
        ToggleButton source = (ToggleButton) event.getSource();
        if (source.getToggleGroup() == null || !source.isSelected()) {
            source.fire();
        }
    }

    /**
     * Gets the consumer method which is called when a list widget toggle button has been clicked
     *
     * @return The consumer method which is called when a list widget toggle button has been clicked
     */
    private Optional<Consumer<ListWidgetType>> getOnSelect() {
        return Optional.ofNullable(getControl().getOnSelect());
    }
}
