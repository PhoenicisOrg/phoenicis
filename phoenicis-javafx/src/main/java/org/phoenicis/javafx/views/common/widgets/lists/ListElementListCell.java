package org.phoenicis.javafx.views.common.widgets.lists;

import javafx.scene.Node;
import javafx.scene.control.ListCell;

/**
 * Created by marc on 15.05.17.
 */
public class ListElementListCell<E extends Node> extends ListCell<E> {
    public ListElementListCell() {
        super();

        this.setPrefWidth(0);
    }

    @Override
    public void updateItem(E item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty && item != null) {
            setGraphic(item);
        } else {
            setGraphic(new DummyElement());
        }
    }
}
