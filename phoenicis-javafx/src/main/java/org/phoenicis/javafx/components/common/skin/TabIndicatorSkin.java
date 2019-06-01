package org.phoenicis.javafx.components.common.skin;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.phoenicis.javafx.components.common.control.TabIndicator;

/**
 * A skin implementation for the {@link TabIndicator} component
 */
public class TabIndicatorSkin extends SkinBase<TabIndicator, TabIndicatorSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public TabIndicatorSkin(TabIndicator control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final Region circle = new Region();
        circle.getStyleClass().add("indicator-circle");

        final Text text = new Text();
        text.getStyleClass().add("indicator-information");
        text.textProperty().bind(getControl().textProperty());

        final StackPane container = new StackPane(circle, text);
        container.getStyleClass().add("indicator-container");

        final StackPane tabIndicator = new StackPane(container);
        tabIndicator.getStyleClass().add("tab-indicator");

        getChildren().add(tabIndicator);
    }
}
