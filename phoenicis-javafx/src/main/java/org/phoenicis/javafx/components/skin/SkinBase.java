package org.phoenicis.javafx.components.skin;

import org.phoenicis.javafx.components.behavior.BehaviorBase;
import org.phoenicis.javafx.components.control.ControlBase;

/**
 * A base class for a JavaFX component skin/view
 *
 * @param <C> The control/model class belonging to the skin
 * @param <S> The skin/view class itself
 */
public abstract class SkinBase<C extends ControlBase<C, S>, S extends SkinBase<C, S>>
        extends javafx.scene.control.SkinBase<C> {
    /**
     * Constructor for all SkinBase instances
     *
     * @param control The control belonging to the skin
     */
    SkinBase(C control) {
        super(control);
    }

    /**
     * Initialises the content of this skin.
     * This method is performed before the behavior is created and initialised
     */
    public abstract void initialise();

    /**
     * Creates the default behavior belonging to this {@link S}.
     * If the skin has no behavior null is returned
     *
     * @return The default behavior belonging to this skin
     */
    public BehaviorBase<C, ?, ?> createDefaultBehavior() {
        return null;
    }

    /**
     * Gets the control to which this skin is applied
     *
     * @return The control to which this skin is applied
     */
    public C getControl() {
        return getSkinnable();
    }
}
