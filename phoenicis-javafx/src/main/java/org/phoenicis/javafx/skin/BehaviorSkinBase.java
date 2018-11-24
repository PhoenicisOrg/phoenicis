package org.phoenicis.javafx.skin;

import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import org.phoenicis.javafx.behavior.BehaviorBase;

public abstract class BehaviorSkinBase<C extends Control, B extends BehaviorBase<C, ?>> extends SkinBase<C> {
    /**
     * The behavior associated to this skin
     */
    private BehaviorBase<C, ?> behavior;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    protected BehaviorSkinBase(C control) {
        super(control);

        // initialise the view
        initialise();

        // create and initialise the behavior
        this.behavior = createDefaultBehavior();
    }

    /**
     * Creates the default behavior for this skin
     *
     * @return The default behavior for this skin
     */
    public abstract BehaviorBase<C, ?> createDefaultBehavior();

    /**
     * Initialises the content of this skin.
     * This method is performed before the behavior is created and initialised
     */
    public abstract void initialise();

    /**
     * Gets the control to which this skin is applied
     *
     * @return The control to which this skin is applied
     */
    public C getControl() {
        return getSkinnable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        super.dispose();

        // cleanup the behavior
        behavior.dispose();

        // remove reference to the behavior
        this.behavior = null;
    }
}
