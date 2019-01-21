package org.phoenicis.javafx.components.common.control;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.phoenicis.javafx.components.common.skin.SkinBase;

/**
 * A base class for a JavaFX component control/model
 *
 * @param <C> The control/model class itself
 * @param <S> The skin/view class belonging to the control
 */
public abstract class ControlBase<C extends ControlBase<C, S>, S extends SkinBase<C, S>> extends Control {

    /**
     * Creates a new skin/view object for the control
     *
     * @return The created view object
     */
    public abstract S createSkin();

    /**
     * {@inheritDoc}
     */
    @Override
    public Skin<?> createDefaultSkin() {
        S skin = createSkin();

        // initialise the skin
        skin.initialise();

        // create and initialise the behavior of the skin (if it exists)
        skin.createDefaultBehavior();

        return skin;
    }
}
