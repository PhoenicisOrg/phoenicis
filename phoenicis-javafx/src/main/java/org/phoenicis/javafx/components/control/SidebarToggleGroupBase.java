package org.phoenicis.javafx.components.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.skin.SidebarToggleGroupSkinBase;

public abstract class SidebarToggleGroupBase<E, C extends SidebarToggleGroupBase<E, C, S>, S extends SidebarToggleGroupSkinBase<E, C, S>>
        extends ControlBase<C, S> {
    private final StringProperty title;

    /**
     * An {@link ObservableList} containing all objects for which a {@link ToggleButton} is to be shown in this
     * SidebarToggleGroupBase
     */
    private final ObservableList<E> elements;

    public SidebarToggleGroupBase(StringProperty title, ObservableList<E> elements) {
        super();

        this.title = title;
        this.elements = elements;
    }

    public SidebarToggleGroupBase(String title) {
        this(new SimpleStringProperty(title), FXCollections.observableArrayList());
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

    public ObservableList<E> getElements() {
        return elements;
    }
}
