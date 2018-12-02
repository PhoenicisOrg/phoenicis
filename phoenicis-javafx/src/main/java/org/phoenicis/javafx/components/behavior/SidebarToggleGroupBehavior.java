package org.phoenicis.javafx.components.behavior;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.control.SidebarToggleGroupBase;
import org.phoenicis.javafx.components.skin.SidebarToggleGroupBaseSkin;

/**
 * The behavior for all sidebar toggle groups, represented by the {@link SidebarToggleGroupBase} component class
 *
 * @param <E> The element class
 * @param <C> The concrete component class
 * @param <S> The concrete skin class
 */
public class SidebarToggleGroupBehavior<E, C extends SidebarToggleGroupBase<E, C, S>, S extends SidebarToggleGroupBaseSkin<E, C, S>>
        extends BehaviorBase<C, S, SidebarToggleGroupBehavior<E, C, S>> {
    /**
     * Constructor
     *
     * @param control The control associated with this behavior
     * @param skin The skin associated with this behavior
     */
    public SidebarToggleGroupBehavior(C control, S skin) {
        super(control, skin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        // ensure that the first toggle button is selected at initialization
        selectFirstToggleButton();

        // ensure that one toggle button is always selected.
        getSkin().getAdhocToggleButtons().addListener((Observable invalidation) -> selectFirstToggleButton());
    }

    /**
     * Select the first toggle button if at least one toggle button exists
     */
    private void selectFirstToggleButton() {
        final ObservableList<ToggleButton> adhocToggleButtons = getSkin().getAdhocToggleButtons();

        if (!adhocToggleButtons.isEmpty() && adhocToggleButtons.stream().noneMatch(ToggleButton::isSelected)) {
            adhocToggleButtons.get(0).fire();
        }
    }
}
