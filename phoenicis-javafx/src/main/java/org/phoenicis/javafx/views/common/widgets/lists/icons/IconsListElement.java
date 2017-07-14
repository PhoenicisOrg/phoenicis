package org.phoenicis.javafx.views.common.widgets.lists.icons;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

import java.net.URI;

/**
 * A list element for a {@link IconsListWidget}
 *
 * @author marc
 * @since 15.05.17
 */
public class IconsListElement<E> extends VBox {

    private static PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    BooleanProperty selected = new BooleanPropertyBase(false) {
        public void invalidated() {
            pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, get());
        }

        @Override
        public Object getBean() {
            return IconsListElement.this;
        }

        @Override
        public String getName() {
            return "selected";
        }
    };

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public boolean isSelected() {
        return this.selected.get();
    }

    /**
     * The item this element contains
     */
    private E item;

    /**
     * Constructor
     *
     * @param item         The item for which a IconsListElement should be created
     * @param miniatureUri An uri to the miniature which is shown inside this IconsListElement
     * @param title        The title which is shown inside this IconsListElement
     * @param enabled      True if this element should be shown as enabled, false otherwise
     */
    public IconsListElement(E item, URI miniatureUri, String title, boolean enabled) {
        super();

        this.item = item;

        this.getStyleClass().add("iconListCell");
        this.setAlignment(Pos.CENTER);

        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            final Rectangle clip = new Rectangle(this.getWidth(), this.getHeight());
            this.setClip(clip);
        });

        this.heightProperty().addListener((observable, oldValue, newValue) -> {
            final Rectangle clip = new Rectangle(this.getWidth(), this.getHeight());
            this.setClip(clip);
        });

        final Region miniature = new Region();
        miniature.getStyleClass().add("iconListMiniatureImage");
        miniature.setStyle(String.format("-fx-background-image: url(\"%s\");", miniatureUri.toString()));

        final Label label = new Label(title);
        label.getStyleClass().add("iconListMiniatureLabel");

        this.getChildren().setAll(miniature, label);

        final Tooltip tooltip = new Tooltip(title);
        Tooltip.install(miniature, tooltip);

        /*
         * set a gray filter for this element if it is not enabled
         */
        if (!enabled) {
            ColorAdjust grayscale = new ColorAdjust();
            grayscale.setSaturation(-1);
            miniature.setEffect(grayscale);
        }
    }

    public static <T> IconsListElement<T> create(ListWidgetEntry<T> item) {
        return new IconsListElement<>(item.getItem(), item.getIconUri(), item.getTitle(), item.isEnabled());
    }

    /**
     * Returns the item belonging to this IconsListElement
     *
     * @return The item belonging to this IconsListElement
     */
    public E getElement() {
        return this.item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IconsListElement<?> that = (IconsListElement<?>) o;

        return new EqualsBuilder()
                .append(item, that.item)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(item)
                .toHashCode();
    }
}