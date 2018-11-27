package org.phoenicis.javafx.components.skin;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import org.phoenicis.javafx.components.behavior.ListWidgetSelectorBehavior;
import org.phoenicis.javafx.components.control.ListWidgetSelector;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetType;

public class ListWidgetSelectorSkin
        extends BehaviorSkinBase<ListWidgetSelector, ListWidgetSelectorSkin, ListWidgetSelectorBehavior> {
    private ToggleButton iconsListButton;
    private ToggleButton compactListButton;
    private ToggleButton detailsListButton;

    /**
     * Constructor for all BehaviorSkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public ListWidgetSelectorSkin(ListWidgetSelector control) {
        super(control);
    }

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

    @Override
    public ListWidgetSelectorBehavior createBehavior() {
        return new ListWidgetSelectorBehavior(getControl(), this);
    }

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
