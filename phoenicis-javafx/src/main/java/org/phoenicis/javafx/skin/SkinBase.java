package org.phoenicis.javafx.skin;

import org.phoenicis.javafx.controler.ControlBase;

/**
 * A base class for a JavaFX component skin/view
 *
 * @param <C> The control/model class belonging to the skin
 * @param <S> The skin/view class itself
 */
public abstract class SkinBase<C extends ControlBase<C, S>, S extends SkinBase<C, S>>
        extends javafx.scene.control.SkinBase<C> {
    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    protected SkinBase(C control) {
        super(control);

        // initialise the skin
        initialise();
    }

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
}
