package org.phoenicis.javafx.components.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenicis.javafx.components.skin.SidebarToggleGroupSkin;

public abstract class SidebarToggleGroup<E> extends ControlBase<SidebarToggleGroup<E>, SidebarToggleGroupSkin<E>> {
    private final StringProperty title;

    /**
     * An {@link ObservableList} containing all objects for which a {@link ToggleButton} is to be shown in this
     * SidebarToggleGroup
     */
    private final ObservableList<E> elements;

    private final ObjectProperty<SidebarToggleButtonSelection> selected;

    public SidebarToggleGroup(StringProperty title, ObservableList<E> elements,
            ObjectProperty<SidebarToggleButtonSelection> selected) {
        super();

        this.title = title;
        this.elements = elements;
        this.selected = selected;
    }

    public SidebarToggleGroup(String title) {
        this(new SimpleStringProperty(title), FXCollections.observableArrayList(), new SimpleObjectProperty<>());
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

    public SidebarToggleButtonSelection getSelected() {
        return selected.get();
    }

    public ObjectProperty<SidebarToggleButtonSelection> selectedProperty() {
        return selected;
    }

    public void selectAll() {
        selected.set(new SidebarToggleButtonSelection(null, 0, true));
    }

    public void selectElement(E element) {
        selected.set(new SidebarToggleButtonSelection(element, elements.indexOf(element), false));
    }

    public void selectFirstElement() {
        selectElement(elements.get(0));
    }

    public class SidebarToggleButtonSelection {
        private final E selectedElement;

        private final int index;

        private final boolean allButton;

        public SidebarToggleButtonSelection(E selectedElement, int index, boolean allButton) {
            this.selectedElement = selectedElement;
            this.index = index;
            this.allButton = allButton;
        }

        public E getSelectedElement() {
            return selectedElement;
        }

        public int getIndex() {
            return index;
        }

        public boolean isAllButton() {
            return allButton;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;

            if (o == null || getClass() != o.getClass())
                return false;

            SidebarToggleButtonSelection that = (SidebarToggleButtonSelection) o;

            return new EqualsBuilder()
                    .append(selectedElement, that.selectedElement)
                    .append(index, that.index)
                    .append(allButton, that.allButton)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(selectedElement)
                    .append(index)
                    .append(allButton)
                    .toHashCode();
        }
    }
}
