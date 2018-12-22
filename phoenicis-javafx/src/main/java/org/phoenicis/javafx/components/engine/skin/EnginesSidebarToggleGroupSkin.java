package org.phoenicis.javafx.components.engine.skin;

import javafx.scene.control.ToggleButton;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.javafx.components.engine.control.EnginesSidebarToggleGroup;
import org.phoenicis.javafx.components.common.skin.SidebarToggleGroupBaseSkin;
import org.phoenicis.javafx.views.mainwindow.engines.EnginesSidebar;

import java.util.Optional;

/**
 * A {@link SidebarToggleGroupBaseSkin} implementation class used inside the {@link EnginesSidebar}
 */
public class EnginesSidebarToggleGroupSkin extends
        SidebarToggleGroupBaseSkin<EngineCategoryDTO, EnginesSidebarToggleGroup, EnginesSidebarToggleGroupSkin> {
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
        final ToggleButton categoryButton = createSidebarToggleButton(category.getName());

        categoryButton.setId(String.format("%sButton", category.getName().toLowerCase()));
        categoryButton.setOnAction(event -> getControl().setSelectedElement(category));

        return categoryButton;
    }
}
