package org.phoenicis.javafx.components.common.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import org.phoenicis.javafx.components.common.behavior.SidebarToggleGroupBehavior;
import org.phoenicis.javafx.components.common.control.SidebarGroup;
import org.phoenicis.javafx.components.common.control.SidebarToggleGroupBase;
import org.phoenicis.javafx.collections.AdhocList;
import org.phoenicis.javafx.collections.MappedList;

import java.util.Optional;

/**
 * The base skin for all toggle groups shown inside sidebars
 *
 * @param <E> The element class
 * @param <C> The concrete component class
 * @param <S> The concrete skin class
 */
public abstract class SidebarToggleGroupBaseSkin<E, C extends SidebarToggleGroupBase<E, C, S>, S extends SidebarToggleGroupBaseSkin<E, C, S>>
        extends BehaviorSkinBase<C, S, SidebarToggleGroupBehavior<E, C, S>> {

    private final ToggleGroup toggleGroup;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    protected SidebarToggleGroupBaseSkin(C control) {
        super(control);

        this.toggleGroup = new ToggleGroup();
    }

    /**
     * Creates a new {@link ToggleButton} for the sidebar.
     * This new toggle button is initialised with the necessary style classes
     *
     * @param text The text shown on the new toggle button
     * @return The created toggle button
     */
    protected static ToggleButton createSidebarToggleButton(String text) {
        final ToggleButton toggleButton = new ToggleButton(text);

        toggleButton.getStyleClass().add("sidebarButton");

        toggleButton.addEventFilter(ActionEvent.ANY, SidebarToggleGroupBaseSkin::eventFilter);

        return toggleButton;
    }

    /**
     * An event filter to prevent the deselection of all buttons
     *
     * @param event The input event to be filtered
     */
    private static void eventFilter(ActionEvent event) {
        ToggleButton source = (ToggleButton) event.getSource();
        if (source.getToggleGroup() == null || !source.isSelected()) {
            source.fire();
        }
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
        final ObservableList<ToggleButton> mappedToggleButtons = new MappedList<>(getControl().getElements(),
                this::convertToToggleButton);

        ToggleButton allToggleButton = createAllButton().orElse(null);

        final ObservableList<ToggleButton> adhocToggleButtons = allToggleButton != null
                ? new AdhocList<>(mappedToggleButtons, allToggleButton)
                : new AdhocList<>(mappedToggleButtons);

        Bindings.bindContent(toggleGroup.getToggles(), adhocToggleButtons);

        SidebarGroup<ToggleButton> sidebarGroup = new SidebarGroup<>(getControl().titleProperty(), adhocToggleButtons);

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

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }
}
