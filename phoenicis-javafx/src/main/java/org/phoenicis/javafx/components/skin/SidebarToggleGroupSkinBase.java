package org.phoenicis.javafx.components.skin;

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

/**
 * The base skin for all toggle groups shown inside sidebars
 *
 * @param <E> The element class
 * @param <C> The concrete component class
 * @param <S> The concrete skin class
 */
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

    /**
     * Creates a new {@link ToggleButton} for the sidebar.
     * This new toggle button is initialised with the necessary style classes
     *
     * @param text The text shown on the new toggle button
     * @return The created toggle button
     */
    protected static ToggleButton createSidebarToggleButton(String text) {
        ToggleButton toggleButton = new ToggleButton(text);

        toggleButton.getStyleClass().add("sidebarButton");

        return toggleButton;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SidebarToggleGroupBehavior<E, C, S> createBehavior() {
        return new SidebarToggleGroupBehavior<>(getControl(), (S) this);
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

    /**
     * Creates the all toggle button, which can be used to select all elements
     *
     * @return The created toggle button or {@link Optional#empty()} if no all toggle button is required
     */
    protected abstract Optional<ToggleButton> createAllButton();

    /**
     * Creates a toggle button for the given element
     *
     * @param element The element for which a toggle button should be created
     * @return The created toggle button
     */
    protected abstract ToggleButton convertToToggleButton(E element);

    /**
     * Gets the {@link ObservableList} containing all shown {@link ToggleButton} objects in this skin
     *
     * @return An {@link ObservableList} containing all shown {@link ToggleButton} objects in this skin
     */
    public ObservableList<ToggleButton> getAdhocToggleButtons() {
        return adhocToggleButtons;
    }
}
