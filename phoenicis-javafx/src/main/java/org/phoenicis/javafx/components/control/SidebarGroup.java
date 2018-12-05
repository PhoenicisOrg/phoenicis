package org.phoenicis.javafx.components.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.phoenicis.javafx.components.skin.SidebarGroupSkin;

/**
 * A sidebar group component to be used inside sidebars
 *
 * @param <E> The element type
 */
public class SidebarGroup<E extends Node> extends ControlBase<SidebarGroup<E>, SidebarGroupSkin<E>> {
    /**
     * The title of the {@link SidebarGroup}
     */
    private final StringProperty title;

    /**
     * The components shown in the {@link SidebarGroup}
     */
    private final ObservableList<E> components;

    /**
     * Constructor
     *
     * @param title The title of the sidebar group
     * @param components The components located inside the sidebar group
     */
    public SidebarGroup(StringProperty title, ObservableList<E> components) {
        super();

        this.title = title;
        this.components = components;
    }

    /**
     * Constructor
     *
     * @param title The title of the sidebar group
     */
    public SidebarGroup(String title) {
        this(new SimpleStringProperty(title), FXCollections.observableArrayList());
    }

    /**
     * Constructor
     */
    public SidebarGroup() {
        this(new SimpleStringProperty(), FXCollections.observableArrayList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SidebarGroupSkin<E> createSkin() {
        return new SidebarGroupSkin<>(this);
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

    public ObservableList<E> getComponents() {
        return components;
    }
}
