package org.phoenicis.javafx.views.common.widgets.lists.icons;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

/**
 * A list element for a {@link IconsListWidget}
 *
 * @author marc
 * @since 15.05.17
 */
public class IconsListElement<E> extends VBox {
    /**
     * The item this element contains
     */
    private ListWidgetEntry<E> item;

    /**
     * Constructor
     *
     * @param item The item for which a IconsListElement should be created
     */
    public IconsListElement(ListWidgetEntry<E> item) {
        super();

        this.item = item;

        this.getStyleClass().add("miniatureListElement");
        this.setAlignment(Pos.CENTER);

        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            final Rectangle clip = new Rectangle(this.getWidth(), this.getHeight());
            this.setClip(clip);
        });

        this.heightProperty().addListener((observable, oldValue, newValue) -> {
            final Rectangle clip = new Rectangle(this.getWidth(), this.getHeight());
            this.setClip(clip);
        });

        final Label label = new Label(item.getTitle());
        label.getStyleClass().add("miniatureText");

        StaticMiniature miniature = new StaticMiniature(item.getIconUri());

        this.getChildren().add(miniature);
        this.getChildren().add(label);

        final Tooltip tooltip = new Tooltip(item.getTitle());
        Tooltip.install(miniature, tooltip);

        if (!item.isEnabled()) {
            ColorAdjust grayscale = new ColorAdjust();
            grayscale.setSaturation(-1);
            this.setEffect(grayscale);
        }
    }

    /**
     * Returns the item belonging to this IconsListElement
     * @return The item belonging to this IconsListElement
     */
    public E getElement() {
        return this.item.getItem();
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

        EqualsBuilder builder = new EqualsBuilder();

        builder.append(item, that.item);

        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(item);

        return builder.toHashCode();
    }
}