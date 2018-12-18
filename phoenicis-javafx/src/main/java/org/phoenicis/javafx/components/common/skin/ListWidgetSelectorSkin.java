package org.phoenicis.javafx.components.common.skin;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import org.phoenicis.javafx.components.common.behavior.ListWidgetSelectorBehavior;
import org.phoenicis.javafx.components.common.control.ListWidgetSelector;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;

/**
 * The skin for the {@link ListWidgetSelector} component
 */
public class ListWidgetSelectorSkin
        extends BehaviorSkinBase<ListWidgetSelector, ListWidgetSelectorSkin, ListWidgetSelectorBehavior> {
    /**
     * The toggle button to select the icons list widget
     */
    private ToggleButton iconsListButton;

    /**
     * The toggle button to select the compact list widget
     */
    private ToggleButton compactListButton;

    /**
     * The toggle button to select the details list widget
     */
    private ToggleButton detailsListButton;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ListWidgetSelectorSkin(ListWidgetSelector control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        ToggleGroup toggleGroup = new ToggleGroup();

        this.iconsListButton = new ToggleButton();
        this.iconsListButton.setToggleGroup(toggleGroup);
        this.iconsListButton.getStyleClass().addAll("listIcon", "iconsList");

        this.compactListButton = new ToggleButton();
        this.compactListButton.setToggleGroup(toggleGroup);
        this.compactListButton.getStyleClass().addAll("listIcon", "compactList");

        this.detailsListButton = new ToggleButton();
        this.detailsListButton.setToggleGroup(toggleGroup);
        this.detailsListButton.getStyleClass().addAll("listIcon", "detailsList");

        HBox container = new HBox(iconsListButton, compactListButton, detailsListButton);
        container.getStyleClass().add("listChooser");

        getChildren().addAll(container);
    }

    /**
     * {@inheritDoc}
     *
     * @return A created list widget selector behavior object
     */
    @Override
    public ListWidgetSelectorBehavior createBehavior() {
        return new ListWidgetSelectorBehavior(getControl(), this);
    }

    /**
     * Gets the {@link ToggleButton} associated to the given {@link ListWidgetType type}
     *
     * @param type The list widget type
     * @return The associated toggle button
     */
    public ToggleButton getListButton(ListWidgetType type) {
        switch (type) {
            case COMPACT_LIST:
                return compactListButton;
            case DETAILS_LIST:
                return detailsListButton;
            case ICONS_LIST:
                return iconsListButton;
            default:
                throw new IllegalArgumentException("Unknown ListWidgetType: " + type.toString());
        }
    }

    public ToggleButton getIconsListButton() {
        return iconsListButton;
    }

    public ToggleButton getCompactListButton() {
        return compactListButton;
    }

    public ToggleButton getDetailsListButton() {
        return detailsListButton;
    }
}
