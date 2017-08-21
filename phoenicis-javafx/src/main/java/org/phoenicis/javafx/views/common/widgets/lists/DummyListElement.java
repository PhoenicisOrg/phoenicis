package org.phoenicis.javafx.views.common.widgets.lists;

import javafx.scene.layout.VBox;

/**
 * A dummy list element.
 * Using this element ensures, that empty {@link javafx.scene.control.ListCell}s have the same height as not empty ones
 *
 * @author marc
 * @since 15.05.17
 */
public class DummyListElement extends VBox {
    /**
     * Constructor
     */
    public DummyListElement() {
        super();

        this.getStyleClass().add("iconListElement");
    }
}
