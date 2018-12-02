package org.phoenicis.javafx.components.behavior;

import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.control.SidebarToggleGroupBase;
import org.phoenicis.javafx.components.skin.SidebarToggleGroupSkinBase;

public class SidebarToggleGroupBehavior<E>
        extends BehaviorBase<SidebarToggleGroupBase<E>, SidebarToggleGroupSkinBase<E>, SidebarToggleGroupBehavior<E>> {
    /**
     * Constructor
     *
     * @param control The control associated with this behavior
     * @param skin The skin associated with this behavior
     */
    public SidebarToggleGroupBehavior(SidebarToggleGroupBase<E> control, SidebarToggleGroupSkinBase<E> skin) {
        super(control, skin);
    }

    @Override
    public void initialise() {
        final ObservableList<ToggleButton> adhocToggleButtons = getSkin().getAdhocToggleButtons();

        // Ensure that one ToggleButton is always selected.
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
