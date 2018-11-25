package org.phoenicis.javafx.controler;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.phoenicis.javafx.skin.SkinBase;

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
        return createSkin();
    }
}
