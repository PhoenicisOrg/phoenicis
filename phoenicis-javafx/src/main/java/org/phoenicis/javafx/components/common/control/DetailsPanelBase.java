package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import org.phoenicis.javafx.components.common.skin.DetailsPanelBaseSkin;

/**
 * A base details panel component
 *
 * @param <C> The concrete component class
 * @param <S> The concrete skin class
 */
public abstract class DetailsPanelBase<C extends DetailsPanelBase<C, S>, S extends DetailsPanelBaseSkin<C, S>>
        extends ControlBase<C, S> {
    /**
     * Callback for close button clicks
     */
    private final ObjectProperty<Runnable> onClose;

    /**
     * Constructor
     *
     * @param onClose The callback for close button clicks
     */
    protected DetailsPanelBase(ObjectProperty<Runnable> onClose) {
        super();

        this.onClose = onClose;
    }

    public Runnable getOnClose() {
        return onClose.get();
    }

    public ObjectProperty<Runnable> onCloseProperty() {
        return onClose;
    }

    public void setOnClose(Runnable onClose) {
        this.onClose.set(onClose);
    }
}
