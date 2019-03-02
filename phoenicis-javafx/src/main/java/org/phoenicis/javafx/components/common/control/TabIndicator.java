package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Tab;
import org.phoenicis.javafx.components.common.skin.TabIndicatorSkin;

/**
 * An indicator symbol shown inside the header of a {@link Tab} object.
 * A {@link TabIndicator} can be used to inform the user about changes that occurred since the user visited the tab the
 * last time
 */
public class TabIndicator extends ControlBase<TabIndicator, TabIndicatorSkin> {
    /**
     * The text shown inside the tab indicator
     */
    private final StringProperty text;

    /**
     * Constructor
     */
    public TabIndicator() {
        super();

        this.text = new SimpleStringProperty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TabIndicatorSkin createSkin() {
        return new TabIndicatorSkin(this);
    }

    public String getText() {
        return this.text.get();
    }

    public StringProperty textProperty() {
        return this.text;
    }

    public void setText(String text) {
        this.text.set(text);
    }
}
