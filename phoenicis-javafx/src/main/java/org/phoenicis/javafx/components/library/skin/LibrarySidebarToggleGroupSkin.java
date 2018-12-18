package org.phoenicis.javafx.components.library.skin;

import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.library.control.LibrarySidebarToggleGroup;
import org.phoenicis.javafx.components.common.skin.SidebarToggleGroupBaseSkin;
import org.phoenicis.javafx.views.mainwindow.library.LibrarySidebar;
import org.phoenicis.library.dto.ShortcutCategoryDTO;

import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A {@link SidebarToggleGroupBaseSkin} implementation class used inside the {@link LibrarySidebar}
 */
public class LibrarySidebarToggleGroupSkin extends
        SidebarToggleGroupBaseSkin<ShortcutCategoryDTO, LibrarySidebarToggleGroup, LibrarySidebarToggleGroupSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public LibrarySidebarToggleGroupSkin(LibrarySidebarToggleGroup control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Optional<ToggleButton> createAllButton() {
        final ToggleButton allCategoryButton = createSidebarToggleButton(tr("All"));

        allCategoryButton.setId("allButton");
        allCategoryButton.setOnMouseClicked(event -> getControl().setNothingSelected());

        return Optional.of(allCategoryButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ToggleButton convertToToggleButton(ShortcutCategoryDTO category) {
        final ToggleButton categoryButton = createSidebarToggleButton(category.getName());

        categoryButton.setId(String.format("%sButton", category.getId().toLowerCase()));
        categoryButton.setOnMouseClicked(event -> getControl().setSelectedElement(category));

        return categoryButton;
    }
}
