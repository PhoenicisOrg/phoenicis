package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.CompactListElementSkin;
import org.phoenicis.javafx.views.common.widgets.lists.AdditionalListWidgetInformation;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;

import java.net.URI;

/**
 * A list element component shown inside a {@link CompactListWidget}
 *
 * @param <E> The concrete type of the element shown in this list element
 */
public class CompactListElement<E> extends ControlBase<CompactListElement<E>, CompactListElementSkin<E>> {
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
     * A list with additional information for the item
     */
    private final ObservableList<AdditionalListWidgetInformation> additionalInformation;

    /**
     * Constructor
     *
     * @param item The item
     * @param miniatureUri The miniature icon for the item
     * @param title The title of the item
     * @param additionalInformation Additional information for the item
     */
    private CompactListElement(ObjectProperty<E> item, ObjectProperty<URI> miniatureUri, StringProperty title,
            ObservableList<AdditionalListWidgetInformation> additionalInformation) {
        super();

        this.item = item;
        this.miniatureUri = miniatureUri;
        this.title = title;
        this.additionalInformation = additionalInformation;
    }

    /**
     * Constructor
     *
     * @param item The list widget entry
     */
    public CompactListElement(ListWidgetEntry<E> item) {
        this(new SimpleObjectProperty<>(item.getItem()), new SimpleObjectProperty<>(item.getIconUri()),
                new SimpleStringProperty(item.getTitle()),
                FXCollections.observableArrayList(item.getAdditionalInformation()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompactListElementSkin<E> createSkin() {
        return new CompactListElementSkin<>(this);
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

    public ObservableList<AdditionalListWidgetInformation> getAdditionalInformation() {
        return additionalInformation;
    }
}
