package org.phoenicis.javafx.components.common.skin;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.components.common.control.SidebarBase;

public abstract class SidebarSkinBase<E, C extends SidebarBase<E, C, S>, S extends SidebarSkinBase<E, C, S>>
        extends SkinBase<C, S> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public SidebarSkinBase(C control) {
        super(control);
    }

    protected static Pane createSpacer() {
        final Pane spacer = new Pane();

        spacer.getStyleClass().add("sidebarSpacer");

        return spacer;
    }

    protected static ScrollPane createScrollPane(Node... nodes) {
        final VBox content = new VBox(nodes);

        final ScrollPane scrollPane = new ScrollPane(content);

        scrollPane.getStyleClass().add("sidebarScrollbar");

        return scrollPane;
    }

    @Override
    public void initialise() {
        final BorderPane container = new BorderPane();
        container.getStyleClass().add("sidebar");

        container.setCenter(createMainContent());

        getChildren().addAll(container);
    }

    protected abstract ScrollPane createMainContent();
}
