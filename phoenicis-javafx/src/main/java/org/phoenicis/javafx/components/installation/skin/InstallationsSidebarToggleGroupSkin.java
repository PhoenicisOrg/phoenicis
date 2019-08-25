package org.phoenicis.javafx.components.installation.skin;

import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.installation.control.InstallationsSidebarToggleGroup;
import org.phoenicis.javafx.components.common.skin.SidebarToggleGroupBaseSkin;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;

import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A {@link SidebarToggleGroupBaseSkin} implementation class used inside the {@link InstallationSidebar}
 */
public class InstallationsSidebarToggleGroupSkin extends
        SidebarToggleGroupBaseSkin<InstallationCategoryDTO, InstallationsSidebarToggleGroup, InstallationsSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public InstallationsSidebarToggleGroupSkin(InstallationsSidebarToggleGroup control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Optional<ToggleButton> createAllButton() {
        final ToggleButton allCategoryButton = createSidebarToggleButton(tr("All"));

        allCategoryButton.setId("all-button");
        allCategoryButton.setOnMouseClicked(event -> getControl().setNothingSelected());

        return Optional.of(allCategoryButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ToggleButton convertToToggleButton(InstallationCategoryDTO category) {
        final ToggleButton categoryButton = createSidebarToggleButton(category.getName());

        categoryButton.setId(SidebarToggleGroupBaseSkin.getToggleButtonId(category.getId()));
        categoryButton.setOnMouseClicked(event -> getControl().setSelectedElement(category));

        return categoryButton;
    }
}
