package org.phoenicis.javafx.components.behavior;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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
        // ensure that one toggle button is always selected.
        getControl().getElements().addListener(
                (ListChangeListener.Change<? extends E> change) -> selectFirstToggleButton());

        // ensure that the first toggle button is selected at initialization
        selectFirstToggleButton();
    }

    /**
     * Ensures, that always a button is selected:
     * - if because of an invalidation of the input list the selection is lost, the selection is reapplied
     * - if no button is selected, select the first button
     */
    private void selectFirstToggleButton() {
        final ToggleGroup toggleGroup = getSkin().getToggleGroup();

        if (toggleGroup.getSelectedToggle() == null && !toggleGroup.getToggles().isEmpty()) {
            if (getControl().selectedElementProperty().getValue() != null &&
                    getControl().getElements().contains(getControl().selectedElementProperty().getValue())) {
                // 1 if an "all" button exists, 0 otherwise
                final int offset = toggleGroup.getToggles().size() - getControl().getElements().size();

                final int index = getControl().getElements().indexOf(getControl().selectedElementProperty().getValue());

                toggleGroup.selectToggle(toggleGroup.getToggles().get(offset + index));

            } else {
                final Toggle firstToggle = toggleGroup.getToggles().get(0);

                if (firstToggle instanceof ToggleButton) {
                    ((ToggleButton) firstToggle).fire();
                }
            }
        }
    }
}
