package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import org.phoenicis.javafx.components.common.skin.DetailsPanelBaseSkin;

public abstract class DetailsPanelBase<C extends DetailsPanelBase<C, S>, S extends DetailsPanelBaseSkin<C, S>>
        extends ControlBase<C, S> {
    private final ObjectProperty<Runnable> onClose;

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
