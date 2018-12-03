package org.phoenicis.javafx.components.skin;

import javafx.scene.control.ToggleButton;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.javafx.components.control.ContainersSidebarToggleGroup;
import org.phoenicis.javafx.views.mainwindow.containers.ContainersSidebar;

import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A {@link SidebarToggleGroupBaseSkin} implementation class used inside the {@link ContainersSidebar}
 */
public class ContainersSidebarToggleGroupSkin extends
        SidebarToggleGroupBaseSkin<ContainerCategoryDTO, ContainersSidebarToggleGroup, ContainersSidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ContainersSidebarToggleGroupSkin(ContainersSidebarToggleGroup control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Optional<ToggleButton> createAllButton() {
        final ToggleButton allCategoryButton = createSidebarToggleButton(tr("All"));

        allCategoryButton.getStyleClass().add("containerButton");
        allCategoryButton.setOnMouseClicked(event -> {
            getControl().setSelectedElement(null);
            getControl().getOnAllCategorySelection().run();
        });

        return Optional.of(allCategoryButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ToggleButton convertToToggleButton(ContainerCategoryDTO category) {
        final ToggleButton containerButton = createSidebarToggleButton(category.getName());

        containerButton.getStyleClass().add("containerButton");
        containerButton.setOnMouseClicked(event -> {
            getControl().setSelectedElement(category);
            getControl().getOnCategorySelection().accept(category);
        });

        return containerButton;
    }
}
