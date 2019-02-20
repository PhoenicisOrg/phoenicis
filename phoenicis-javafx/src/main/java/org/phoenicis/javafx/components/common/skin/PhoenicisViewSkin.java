package org.phoenicis.javafx.components.common.skin;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectExpression;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.phoenicis.javafx.components.common.control.DetailsPanel;
import org.phoenicis.javafx.components.common.control.PhoenicisView;
import org.phoenicis.javafx.components.common.control.SidebarBase;

/**
 * A base skin implementation for {@link PhoenicisView} components implementations
 *
 * @param <C> The control type of the component
 * @param <S> The skin type of the component
 */
public abstract class PhoenicisViewSkin<C extends PhoenicisView<C, S>, S extends PhoenicisViewSkin<C, S>>
        extends SkinBase<C, S> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public PhoenicisViewSkin(C control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final BorderPane container = new BorderPane();
        container.getStyleClass().add("mainWindowScene");

        container.leftProperty().bind(createSidebar());
        container.centerProperty().bind(Bindings.when(getControl().initializedProperty())
                .then(createContent()).otherwise(createWaitPanel()));
        container.rightProperty().bind(createDetailsPanel());

        getChildren().setAll(container);
    }

    /**
     * Creates a panel containing a "waiting" indicator
     *
     * @return A panel containing a "waiting" indicator
     */
    private HBox createWaitPanel() {
        final ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.getStyleClass().add("waiting-indicator");

        final HBox waitPanel = new HBox(progressIndicator);
        waitPanel.getStyleClass().add("rightPane");

        return waitPanel;
    }

    /**
     * Creates the sidebar for this component
     *
     * @return The sidebar
     */
    public abstract ObjectExpression<SidebarBase<?, ?, ?>> createSidebar();

    /**
     * Creates the main content for this component
     *
     * @return The main content
     */
    public abstract ObjectExpression<Node> createContent();

    /**
     * Creates the details panel for this component
     *
     * @return The details panel
     */
    public abstract ObjectExpression<DetailsPanel> createDetailsPanel();
}
