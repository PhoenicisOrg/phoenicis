package org.phoenicis.javafx.components.common.skin;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.javafx.collections.ConcatenatedList;
import org.phoenicis.javafx.components.common.control.SidebarGroup;

/**
 * A skin for the {@link SidebarGroup} component
 *
 * @param <E> The element type
 */
public class SidebarGroupSkin<E extends Node> extends SkinBase<SidebarGroup<E>, SidebarGroupSkin<E>> {
    /**
     * A list containing the title {@link Label} if it contains a nonempty title
     */
    private final ObservableList<Label> shownTitle;

    /**
     * A concatenated list containing all components shown inside this {@link SidebarGroup}
     */
    private final ConcatenatedList<Node> components;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public SidebarGroupSkin(SidebarGroup<E> control) {
        super(control);

        this.shownTitle = FXCollections.observableArrayList();
        this.components = ConcatenatedList.create(shownTitle, getControl().getComponents());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final Label title = createTitleLabel();

        // ensure that the title label is only shown when it contains a nonempty string
        title.textProperty().addListener((Observable invalidation) -> updateTitleLabelVisibility(title));
        // ensure that the title label is correctly shown during initialisation
        updateTitleLabelVisibility(title);

        VBox container = new VBox();
        container.getStyleClass().add("sidebarInside");

        Bindings.bindContent(container.getChildren(), components);

        getChildren().addAll(container);
    }

    /**
     * Updates the visibility of the title label based on whether it contains a title text
     *
     * @param titleLabel The title label
     */
    private void updateTitleLabelVisibility(final Label titleLabel) {
        final String title = getControl().getTitle();

        if (StringUtils.isEmpty(title)) {
            shownTitle.clear();
        } else {
            shownTitle.setAll(titleLabel);
        }
    }

    /**
     * Creates a new {@link Label} object containing the title of the sidebar group
     *
     * @return The created label
     */
    private Label createTitleLabel() {
        Label titleLabel = new Label();
        titleLabel.getStyleClass().add("sidebarTitle");

        titleLabel.textProperty().bind(getControl().titleProperty());

        return titleLabel;
    }
}
