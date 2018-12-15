package org.phoenicis.javafx.components.common.skin;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.phoenicis.javafx.components.common.control.IconsListElement;

/**
 * The skin for the {@link IconsListElement} component
 *
 * @param <E> The concrete type of the element shown in this list element
 */
public class IconsListElementSkin<E> extends SkinBase<IconsListElement<E>, IconsListElementSkin<E>> {
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    /**
     * Constructor for all SkinBase instances
     *
     * @param control The control belonging to the skin
     */
    public IconsListElementSkin(IconsListElement<E> control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final VBox container = new VBox(createMiniature(), createLabel());
        container.getStyleClass().add("iconListElement");

        container.widthProperty().addListener((observable, oldValue, newValue) -> container
                .setClip(new Rectangle(container.getWidth(), container.getHeight())));

        container.heightProperty().addListener((observable, oldValue, newValue) -> container
                .setClip(new Rectangle(container.getWidth(), container.getHeight())));

        // update the selected pseudo class according to the selected state of the component
        getControl().selectedProperty().addListener((Observable invalidation) -> container
                .pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, getControl().isSelected()));
        // adopt the selected state during initialisation
        container.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, getControl().isSelected());

        getChildren().addAll(container);
    }

    /**
     * Creates a label for the title of the icon list element
     *
     * @return A label with the title of the list element
     */
    private Label createLabel() {
        final Label label = new Label();

        label.getStyleClass().add("iconListMiniatureLabel");
        label.textProperty().bind(getControl().titleProperty());

        return label;
    }

    /**
     * Creates a region with the miniature of the list element
     *
     * @return A region with the miniature of the list element
     */
    private Region createMiniature() {
        final Region miniature = new Region();
        miniature.getStyleClass().add("iconListMiniatureImage");
        miniature.styleProperty().bind(
                Bindings.createStringBinding(
                        () -> String.format("-fx-background-image: url(\"%s\");",
                                getControl().getMiniatureUri().toString()),
                        getControl().miniatureUriProperty()));

        final Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(getControl().titleProperty());

        Tooltip.install(miniature, tooltip);

        // set a gray filter for this element if it is not enabled
        getControl().enabledProperty().addListener((Observable invalidation) -> updateEnabled(miniature));
        // adopt the enable status during initialisation
        updateEnabled(miniature);

        return miniature;
    }

    /**
     * Updates grayscale of the miniature when the enabled state has been updated
     *
     * @param miniature The miniature
     */
    private void updateEnabled(final Region miniature) {
        if (!getControl().isEnabled()) {
            final ColorAdjust grayScale = new ColorAdjust();
            grayScale.setSaturation(-1);

            miniature.setEffect(grayScale);
        } else {
            miniature.setEffect(null);
        }
    }
}
