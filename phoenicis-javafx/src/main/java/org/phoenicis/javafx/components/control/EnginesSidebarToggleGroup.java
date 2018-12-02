package org.phoenicis.javafx.components.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.javafx.components.skin.EnginesSidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationsSidebar;

import java.util.function.Consumer;

/**
 * A toggle group component used inside the {@link ApplicationsSidebar}
 */
public class EnginesSidebarToggleGroup
        extends SidebarToggleGroupBase<EngineCategoryDTO, EnginesSidebarToggleGroup, EnginesSidebarToggleGroupSkin> {
    /**
     * A consumer, which is called when a category has been selected
     */
    private final ObjectProperty<Consumer<EngineCategoryDTO>> onCategorySelection;

    /**
     * Constructor
     *
     * @param title The title of the engines sidebar toggle group
     */
    public EnginesSidebarToggleGroup(String title) {
        super(title);

        this.onCategorySelection = new SimpleObjectProperty<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EnginesSidebarToggleGroupSkin createSkin() {
        return new EnginesSidebarToggleGroupSkin(this);
    }

    public Consumer<EngineCategoryDTO> getOnCategorySelection() {
        return onCategorySelection.get();
    }

    public ObjectProperty<Consumer<EngineCategoryDTO>> onCategorySelectionProperty() {
        return onCategorySelection;
    }

    public void setOnCategorySelection(Consumer<EngineCategoryDTO> onCategorySelection) {
        this.onCategorySelection.set(onCategorySelection);
    }
}
