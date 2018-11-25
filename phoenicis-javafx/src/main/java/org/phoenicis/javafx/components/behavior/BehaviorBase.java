package org.phoenicis.javafx.components.behavior;

import org.phoenicis.javafx.components.control.ControlBase;
import org.phoenicis.javafx.components.skin.BehaviorSkinBase;

/**
 * A base class for a JavaFX component behavior/controller
 *
 * @param <C> The control/model class of the behavior
 * @param <S> The skin/view class of the behavior
 * @param <B> The behavior/controller class itself
 */
public abstract class BehaviorBase<C extends ControlBase<C, S>, S extends BehaviorSkinBase<C, S, B>, B extends BehaviorBase<C, S, B>> {
    /**
     * The control associated with this behavior
     */
    private C control;

    /**
     * The skin associated with this behavior
     */
    private S skin;

    /**
     * Constructor
     *
     * @param control The control associated with this behavior
     * @param skin The skin associated with this behavior
     */
    protected BehaviorBase(C control, S skin) {
        super();

        this.control = control;
        this.skin = skin;

        // initialise the behavior
        initialise();
    }

    /**
     * Called during the initialisation of the behavior object.
     * This method is called after the control and skin have been initialised
     */
    public abstract void initialise();

    /**
     * Called during the disposal of of the behavior object
     */
    public void dispose() {
        this.control = null;
        this.skin = null;
    }

    /**
     * Gets the control associated with this behavior
     *
     * @return The control associated with this behavior
     */
    public C getControl() {
        return control;
    }

    /**
     * Gets the skin associated with this behavior
     *
     * @return The skin associated with this behavior
     */
    public S getSkin() {
        return skin;
    }
}
