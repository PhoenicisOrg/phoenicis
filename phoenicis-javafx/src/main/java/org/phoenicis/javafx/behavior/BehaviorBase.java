package org.phoenicis.javafx.behavior;

import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;

public abstract class BehaviorBase<C extends Control, S extends SkinBase<C>> {
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

    public abstract void initialise();

    /**
     * Disposes this behavior
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
