package org.phoenicis.javafx.components.common.skin;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.components.common.control.SidebarBase;

/**
 * The base skin for all {@link SidebarBase} implementations
 *
 * @param <E> The element type of the toggle button group
 * @param <C> The concrete component type
 * @param <S> The concrete skin type
 */
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

    /**
     * Creates a spacer component
     *
     * @return A spacer component
     */
    protected static Pane createSpacer() {
        final Pane spacer = new Pane();

        spacer.getStyleClass().add("sidebarSpacer");

        return spacer;
    }

    /**
     * Creates a {@link ScrollPane} containing the given {@link Node[] nodes}.
     * The nodes are shown below each other in the {@link ScrollPane}
     *
     * @param nodes The nodes inside the returned {@link ScrollPane}
     * @return A {@link ScrollPane} containing all given nodes
     */
    protected static ScrollPane createScrollPane(Node... nodes) {
        final VBox content = new VBox(nodes);

        final ScrollPane scrollPane = new ScrollPane(content);

        scrollPane.getStyleClass().add("sidebarScrollbar");

        return scrollPane;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final BorderPane container = new BorderPane();
        container.getStyleClass().add("sidebar");

        container.setCenter(createMainContent());

        getChildren().addAll(container);
    }

    /**
     * Creates the main content of the sidebar
     *
     * @return A {@link ScrollPane} containing the main content of the sidebar
     */
    protected abstract ScrollPane createMainContent();
}
