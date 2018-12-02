package org.phoenicis.javafx.components.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.skin.ApplicationSidebarToggleGroupSkin;
import org.phoenicis.repository.dto.CategoryDTO;

import java.util.function.Consumer;

public class ApplicationSidebarToggleGroup
        extends SidebarToggleGroupBase<CategoryDTO, ApplicationSidebarToggleGroup, ApplicationSidebarToggleGroupSkin> {
    private ObjectProperty<Runnable> onAllCategorySelection;

    private ObjectProperty<Consumer<CategoryDTO>> onCategorySelection;

    public ApplicationSidebarToggleGroup(StringProperty title, ObservableList<CategoryDTO> elements,
            ObjectProperty<SidebarToggleButtonSelection> selected) {
        super(title, elements, selected);
    }

    public ApplicationSidebarToggleGroup(String title) {
        super(title);
    }

    @Override
    public ApplicationSidebarToggleGroupSkin createSkin() {
        return new ApplicationSidebarToggleGroupSkin(this);
    }

    public Runnable getOnAllCategorySelection() {
        return onAllCategorySelection.get();
    }

    public ObjectProperty<Runnable> onAllCategorySelectionProperty() {
        return onAllCategorySelection;
    }

    public void setOnAllCategorySelection(Runnable onAllCategorySelection) {
        this.onAllCategorySelection.set(onAllCategorySelection);
    }

    public Consumer<CategoryDTO> getOnCategorySelection() {
        return onCategorySelection.get();
    }

    public ObjectProperty<Consumer<CategoryDTO>> onCategorySelectionProperty() {
        return onCategorySelection;
    }

    public void setOnCategorySelection(Consumer<CategoryDTO> onCategorySelection) {
        this.onCategorySelection.set(onCategorySelection);
    }
}
