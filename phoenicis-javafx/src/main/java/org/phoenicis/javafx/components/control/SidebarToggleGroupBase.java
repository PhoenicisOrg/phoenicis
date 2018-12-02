package org.phoenicis.javafx.components.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.skin.SidebarToggleGroupBaseSkin;

/**
 * A base toggle group component to be used inside sidebars
 *
 * @param <E> The element class
 * @param <C> The concrete component class
 * @param <S> The concrete skin class
 */
public abstract class SidebarToggleGroupBase<E, C extends SidebarToggleGroupBase<E, C, S>, S extends SidebarToggleGroupBaseSkin<E, C, S>>
        extends ControlBase<C, S> {
    /**
     * The title of the {@link SidebarToggleGroupBase}
     */
    private final StringProperty title;

    /**
     * An {@link ObservableList} containing all objects for which a {@link ToggleButton} is to be shown in the
     * {@link SidebarToggleGroupBase}
     */
    private final ObservableList<E> elements;

    /**
     * Constructor
     *
     * @param title The title of the sidebar toggle group
     * @param elements The elements to be shown inside the sidebar toggle group
     */
    public SidebarToggleGroupBase(StringProperty title, ObservableList<E> elements) {
        super();

        this.title = title;
        this.elements = elements;
    }

    /**
     * Constructor
     *
     * @param title The title of the sidebar toggle group
     */
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
