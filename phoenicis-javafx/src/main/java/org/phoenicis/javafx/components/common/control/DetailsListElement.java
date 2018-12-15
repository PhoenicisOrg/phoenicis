package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.skin.DetailsListElementSkin;
import org.phoenicis.javafx.views.common.widgets.lists.AdditionalListWidgetInformation;
import org.phoenicis.javafx.views.common.widgets.lists.ListWidgetEntry;
import org.phoenicis.javafx.views.common.widgets.lists.details.DetailsListWidget;

/**
 * A list element component shown inside a {@link DetailsListWidget}
 *
 * @param <E> The concrete type of the element shown in this list element
 */
public class DetailsListElement<E> extends ControlBase<DetailsListElement<E>, DetailsListElementSkin<E>> {
    /**
     * The item represented by this list element
     */
    private final ObjectProperty<E> item;

    /**
     * The title belonging to the item
     */
    private final StringProperty title;

    /**
     * A list with additional information for the item
     */
    private final ObservableList<AdditionalListWidgetInformation> additionalInformation;

    /**
     * A list with additional detailed information for the item
     */
    private final ObservableList<AdditionalListWidgetInformation> detailedInformation;

    /**
     * Constructor
     *
     * @param item The item
     * @param title The title of the item
     * @param additionalInformation Additional information for the item
     * @param detailedInformation Additional detailed information for the item
     */
    public DetailsListElement(ObjectProperty<E> item, StringProperty title,
            ObservableList<AdditionalListWidgetInformation> additionalInformation,
            ObservableList<AdditionalListWidgetInformation> detailedInformation) {
        super();

        this.item = item;
        this.title = title;
        this.additionalInformation = additionalInformation;
        this.detailedInformation = detailedInformation;
    }

    /**
     * Constructor
     *
     * @param item The list widget entry
     */
    public DetailsListElement(ListWidgetEntry<E> item) {
        this(new SimpleObjectProperty<>(item.getItem()), new SimpleStringProperty(item.getTitle()),
                FXCollections.observableArrayList(item.getAdditionalInformation()),
                FXCollections.observableArrayList(item.getDetailedInformation()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DetailsListElementSkin<E> createSkin() {
        return new DetailsListElementSkin<>(this);
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

    public ObservableList<AdditionalListWidgetInformation> getDetailedInformation() {
        return detailedInformation;
    }
}
