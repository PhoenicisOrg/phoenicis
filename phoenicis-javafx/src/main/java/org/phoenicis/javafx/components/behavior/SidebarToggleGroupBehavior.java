package org.phoenicis.javafx.components.behavior;

import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.control.SidebarToggleGroupBase;
import org.phoenicis.javafx.components.skin.SidebarToggleGroupSkinBase;

/**
 * The behavior for all sidebar toggle groups, represented by the {@link SidebarToggleGroupBase} component class
 *
 * @param <E> The element class
 * @param <C> The concrete component class
 * @param <S> The concrete skin class
 */
public class SidebarToggleGroupBehavior<E, C extends SidebarToggleGroupBase<E, C, S>, S extends SidebarToggleGroupSkinBase<E, C, S>>
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
        final ObservableList<ToggleButton> adhocToggleButtons = getSkin().getAdhocToggleButtons();

        // ensure that one ToggleButton is always selected.
        adhocToggleButtons.addListener((Change<? extends ToggleButton> change) -> {
            // the adhoc toggle buttons contain no selected button
            if (adhocToggleButtons.stream().noneMatch(ToggleButton::isSelected)) {
                if (adhocToggleButtons.size() > 0) {
                    // to solve this automatically select the first button
                    adhocToggleButtons.get(0).fire();
                }
            }
        });
    }
}
