package org.phoenicis.javafx.components.common.widgets.icons.control;

import javafx.beans.property.*;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.common.widgets.icons.skin.IconsListElementSkin;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;

import java.net.URI;

/**
 * A list element component shown inside an {@link IconsListWidget}
 *
 * @param <E> The concrete type of the element shown in this list element
 */
public class IconsListElement<E> extends ControlBase<IconsListElement<E>, IconsListElementSkin<E>> {
    /**
     * The item represented by this list element
     */
    private final ObjectProperty<E> item;

    /**
     * The {@link URI} leading to the miniature image of the item
     */
    private final ObjectProperty<URI> miniatureUri;

    /**
     * The title belonging to the item
     */
    private final StringProperty title;

    /**
     * The enabled status of the item.
     * For engines this is true if the engine has been installed
     */
    private final BooleanProperty enabled;

    /**
     * The selection state of this list element
     */
    private final BooleanProperty selected;

    /**
     * Constructor
     *
     * @param item The item
     * @param miniatureUri The URI to the miniature image
     * @param title The title
     * @param enabled The enabled status
     * @param selected The selection status
     */
    private IconsListElement(ObjectProperty<E> item, ObjectProperty<URI> miniatureUri, StringProperty title,
            BooleanProperty enabled, BooleanProperty selected) {
        super();

        this.item = item;
        this.miniatureUri = miniatureUri;
        this.title = title;
        this.enabled = enabled;
        this.selected = selected;
    }

    /**
     * Constructor
     *
     * @param item The list widget entry
     */
    public IconsListElement(ListWidgetElement<E> item) {
        this(new SimpleObjectProperty<>(item.getItem()), new SimpleObjectProperty<>(item.getIconUri()),
                new SimpleStringProperty(item.getTitle()), new SimpleBooleanProperty(item.isEnabled()),
                new SimpleBooleanProperty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IconsListElementSkin<E> createSkin() {
        return new IconsListElementSkin<>(this);
    }

    public E getItem() {
        return item.get();
    }

    public ObjectProperty<E> itemProperty() {
        return item;
    }

    public void setItem(E item) {
        this.item.set(item);
    }

    public URI getMiniatureUri() {
        return miniatureUri.get();
    }

    public ObjectProperty<URI> miniatureUriProperty() {
        return miniatureUri;
    }

    public void setMiniatureUri(URI miniatureUri) {
        this.miniatureUri.set(miniatureUri);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public BooleanProperty enabledProperty() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
