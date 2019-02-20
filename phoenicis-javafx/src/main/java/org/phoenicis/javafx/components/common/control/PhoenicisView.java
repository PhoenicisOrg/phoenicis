package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.phoenicis.javafx.components.common.skin.PhoenicisViewSkin;

/**
 * A base component for a view of the Phoenicis application
 *
 * @param <C> The control type of the component
 * @param <S> The skin type of the component
 */
public abstract class PhoenicisView<C extends PhoenicisView<C, S>, S extends PhoenicisViewSkin<C, S>>
        extends ControlBase<C, S> {
    /**
     * A property containing the initialization state of this view
     */
    private final BooleanProperty initialized;

    /**
     * Constructor
     *
     * @param initialized A property containing the initialization state of this view
     */
    protected PhoenicisView(BooleanProperty initialized) {
        super();

        this.initialized = initialized;
    }

    /**
     * Constructor
     */
    protected PhoenicisView() {
        this(new SimpleBooleanProperty(false));
    }

    public boolean isInitialized() {
        return this.initialized.get();
    }

    public BooleanProperty initializedProperty() {
        return this.initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized.set(initialized);
    }
}
