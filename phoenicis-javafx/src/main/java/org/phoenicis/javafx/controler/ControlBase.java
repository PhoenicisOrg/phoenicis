package org.phoenicis.javafx.controler;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import org.phoenicis.javafx.skin.SkinBase;

public abstract class ControlBase<S extends SkinBase<? extends ControlBase<?>>> extends Control {

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
