package org.phoenicis.javafx.components.common.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.components.common.control.SidebarGroup;
import org.phoenicis.javafx.collections.AdhocList;

/**
 * A skin for the {@link SidebarGroup} component
 *
 * @param <E> The element type
 */
public class SidebarGroupSkin<E extends Node> extends SkinBase<SidebarGroup<E>, SidebarGroupSkin<E>> {
    private final ObservableList<? extends Node> content;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public SidebarGroupSkin(SidebarGroup<E> control) {
        super(control);

        this.content = getControl().getTitle() != null
                ? new AdhocList<>(getControl().getComponents(), createTitleLabel())
                : getControl().getComponents();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        VBox container = new VBox();
        container.getStyleClass().add("sidebarInside");

        Bindings.bindContent(container.getChildren(), content);

        getChildren().addAll(container);
    }

    /**
     * Creates a new {@link Label} object containing the title of the sidebar group
     *
     * @return The created label
     */
    private Label createTitleLabel() {
        Label title = new Label();
        title.getStyleClass().add("sidebarTitle");

        title.textProperty().bind(getControl().titleProperty());

        return title;
    }
}
