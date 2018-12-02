package org.phoenicis.javafx.components.skin;

import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.behavior.SidebarToggleGroupBehavior;
import org.phoenicis.javafx.components.control.ApplicationSidebarToggleGroup;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarToggleButton;
import org.phoenicis.repository.dto.CategoryDTO;

import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class ApplicationSidebarToggleGroupSkin extends
        SidebarToggleGroupSkinBase<CategoryDTO, ApplicationSidebarToggleGroup, ApplicationSidebarToggleGroupSkin> {
    /**
     * Constructor for all BehaviorSkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public ApplicationSidebarToggleGroupSkin(ApplicationSidebarToggleGroup control) {
        super(control);
    }

    @Override
    public SidebarToggleGroupBehavior<CategoryDTO, ApplicationSidebarToggleGroup, ApplicationSidebarToggleGroupSkin> createBehavior() {
        return new SidebarToggleGroupBehavior<>(getControl(), this);
    }

    @Override
    public Optional<ToggleButton> createAllButton() {
        final SidebarToggleButton allCategoryButton = new SidebarToggleButton(tr("All"));

        allCategoryButton.setSelected(true);
        allCategoryButton.setId("allButton");
        allCategoryButton.setOnAction(event -> getControl().getOnAllCategorySelection().run());

        return Optional.of(allCategoryButton);
    }

    @Override
    public ToggleButton convertToToggleButton(CategoryDTO category) {
        final SidebarToggleButton categoryButton = new SidebarToggleButton(category.getName());

        categoryButton.setId(String.format("%sButton", category.getId().toLowerCase()));
        categoryButton.setOnAction(event -> getControl().getOnCategorySelection().accept(category));

        return categoryButton;
    }
}
