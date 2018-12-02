package org.phoenicis.javafx.components.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.phoenicis.javafx.components.skin.SidebarGroupSkin;

public class SidebarGroup<E extends Node> extends ControlBase<SidebarGroup<E>, SidebarGroupSkin<E>> {
    private final StringProperty title;

    private final ObservableList<E> components;

    public SidebarGroup(StringProperty title, ObservableList<E> components) {
        super();

        this.title = title;
        this.components = components;
    }

    public SidebarGroup(String title) {
        this(new SimpleStringProperty(title), FXCollections.observableArrayList());
    }

    public SidebarGroup() {
        this(new SimpleStringProperty(), FXCollections.observableArrayList());
    }

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
