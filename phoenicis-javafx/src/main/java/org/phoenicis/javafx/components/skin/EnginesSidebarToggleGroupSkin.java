package org.phoenicis.javafx.components.skin;

import javafx.scene.control.ToggleButton;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.javafx.components.control.EnginesSidebarToggleGroup;
import org.phoenicis.javafx.views.mainwindow.engines.EnginesSidebar;

import java.util.Optional;

/**
 * A {@link SidebarToggleGroupSkinBase} implementation class used inside the {@link EnginesSidebar}
 */
public class EnginesSidebarToggleGroupSkin extends
        SidebarToggleGroupSkinBase<EngineCategoryDTO, EnginesSidebarToggleGroup, EnginesSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public EnginesSidebarToggleGroupSkin(EnginesSidebarToggleGroup control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Optional<ToggleButton> createAllButton() {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ToggleButton convertToToggleButton(EngineCategoryDTO category) {
        ToggleButton categoryButton = createSidebarToggleButton(category.getName());

        categoryButton.setId(String.format("%sButton", category.getName().toLowerCase()));
        categoryButton.setOnAction(event -> getControl().getOnCategorySelection().accept(category));

        return categoryButton;
    }
}
