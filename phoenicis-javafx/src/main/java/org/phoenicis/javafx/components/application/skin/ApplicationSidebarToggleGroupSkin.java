package org.phoenicis.javafx.components.application.skin;

import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.application.control.ApplicationSidebar;
import org.phoenicis.javafx.components.application.control.ApplicationSidebarToggleGroup;
import org.phoenicis.javafx.components.common.skin.SidebarToggleGroupBaseSkin;
import org.phoenicis.repository.dto.CategoryDTO;

import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A {@link SidebarToggleGroupBaseSkin} implementation class used inside the {@link ApplicationSidebar}
 */
public class ApplicationSidebarToggleGroupSkin extends
        SidebarToggleGroupBaseSkin<CategoryDTO, ApplicationSidebarToggleGroup, ApplicationSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ApplicationSidebarToggleGroupSkin(ApplicationSidebarToggleGroup control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Optional<ToggleButton> createAllButton() {
        final ToggleButton allCategoryButton = createSidebarToggleButton(tr("All"));

        allCategoryButton.setId("all-button");
        allCategoryButton.setOnAction(event -> getControl().setNothingSelected());

        return Optional.of(allCategoryButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ToggleButton convertToToggleButton(CategoryDTO category) {
        final ToggleButton categoryButton = createSidebarToggleButton(category.getName());

        categoryButton.setId(ApplicationSidebarToggleGroupSkin.getToggleButtonId(category.getId()));
        categoryButton.setOnAction(event -> getControl().setSelectedElement(category));

        return categoryButton;
    }

    /**
     * Creates a button ID which can be used e.g. to assign icons via CSS based on the category ID
     *
     * @param categoryId The category ID which should be used
     * @return The created button ID
     */
    public static String getToggleButtonId(String categoryId) {
        return String.format("applications-%s", SidebarToggleGroupBaseSkin.getToggleButtonId(categoryId));
    }
}
