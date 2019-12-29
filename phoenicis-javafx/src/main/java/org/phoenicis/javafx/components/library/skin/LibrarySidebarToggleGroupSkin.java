package org.phoenicis.javafx.components.library.skin;

import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.components.library.control.LibrarySidebarToggleGroup;
import org.phoenicis.javafx.components.common.skin.SidebarToggleGroupBaseSkin;
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

        allCategoryButton.setId("all-button");
        allCategoryButton.setOnMouseClicked(event -> getControl().setNothingSelected());

        return Optional.of(allCategoryButton);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ToggleButton convertToToggleButton(ShortcutCategoryDTO category) {
        final ToggleButton categoryButton = createSidebarToggleButton(category.getName());
        // TODO: store category ID in shortcut.info?
        categoryButton.setId(LibrarySidebarToggleGroupSkin.getToggleButtonId(category.getId()));
        categoryButton.setOnMouseClicked(event -> getControl().setSelectedElement(category));

        return categoryButton;
    }

    /**
     * Creates a button ID which can be used e.g. to assign icons via CSS based on the category ID
     *
     * @param categoryId The category ID which should be used
     * @return The created button ID
     */
    public static String getToggleButtonId(String categoryId) {
        return String.format("library-%s", SidebarToggleGroupBaseSkin.getToggleButtonId(categoryId));
    }
}
