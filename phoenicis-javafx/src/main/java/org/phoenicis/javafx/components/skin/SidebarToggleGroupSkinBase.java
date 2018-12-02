package org.phoenicis.javafx.components.skin;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.components.behavior.SidebarToggleGroupBehavior;
import org.phoenicis.javafx.components.control.SidebarGroup;
import org.phoenicis.javafx.components.control.SidebarToggleGroupBase;
import org.phoenicis.javafx.views.common.lists.AdhocList;
import org.phoenicis.javafx.views.common.lists.MappedList;

import java.util.Optional;

public abstract class SidebarToggleGroupSkinBase<E, C extends SidebarToggleGroupBase<E, C, S>, S extends SidebarToggleGroupSkinBase<E, C, S>>
        extends BehaviorSkinBase<C, S, SidebarToggleGroupBehavior<E, C, S>> {
    /**
     * An {@link ObservableList} containing both the "all" toggle button, if it's available, and the mapped toggle
     * buttons
     */
    private ObservableList<ToggleButton> adhocToggleButtons;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    protected SidebarToggleGroupSkinBase(C control) {
        super(control);
    }

    protected static ToggleButton createSidebarToggleButton(String text) {
        ToggleButton toggleButton = new ToggleButton(text);

        toggleButton.getStyleClass().add("sidebarButton");

        return toggleButton;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        ObservableList<ToggleButton> mappedToggleButtons = new MappedList<>(getControl().getElements(),
                this::convertToToggleButton);

        ToggleButton allToggleButton = createAllButton().orElse(null);
        if (allToggleButton != null) {
            adhocToggleButtons = new AdhocList<>(mappedToggleButtons, allToggleButton);
        } else {
            adhocToggleButtons = new AdhocList<>(mappedToggleButtons);
        }

        ToggleGroup toggleGroup = new ToggleGroup();
        Bindings.bindContent(toggleGroup.getToggles(), adhocToggleButtons);

        // always select the first toggle button when a change in the toggle buttons is detected
        adhocToggleButtons.addListener((Observable invalidation) -> {
            if (!adhocToggleButtons.isEmpty()) {
                adhocToggleButtons.get(0).fire();
            }
        });

        /*
         * Workaround for https://github.com/PhoenicisOrg/phoenicis/issues/1516
         * Normally
         * `SidebarGroup<ToggleButton> sidebarGroup = new SidebarGroup<>(getControl().titleProperty(),
         * adhocToggleButtons);`
         * should work
         */
        VBox container = new VBox();
        container.getStyleClass().add("sidebarInside");

        Bindings.bindContent(container.getChildren(), adhocToggleButtons);

        SidebarGroup<Node> sidebarGroup = new SidebarGroup<>(getControl().titleProperty(),
                FXCollections.singletonObservableList(container));

        getChildren().addAll(sidebarGroup);
    }

    abstract Optional<ToggleButton> createAllButton();

    abstract ToggleButton convertToToggleButton(E element);

    public ObservableList<ToggleButton> getAdhocToggleButtons() {
        return adhocToggleButtons;
    }
}
