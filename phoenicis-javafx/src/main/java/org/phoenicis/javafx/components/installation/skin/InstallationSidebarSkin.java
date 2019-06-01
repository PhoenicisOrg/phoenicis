package org.phoenicis.javafx.components.installation.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ScrollPane;
import org.phoenicis.javafx.components.common.skin.ExtendedSidebarSkinBase;
import org.phoenicis.javafx.components.installation.control.InstallationSidebar;
import org.phoenicis.javafx.components.installation.control.InstallationsSidebarToggleGroup;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link InstallationSidebar} component
 */
public class InstallationSidebarSkin
        extends ExtendedSidebarSkinBase<InstallationCategoryDTO, InstallationSidebar, InstallationSidebarSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public InstallationSidebarSkin(InstallationSidebar control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ScrollPane createMainContent() {
        return createScrollPane(createSidebarToggleGroup());
    }

    /**
     * Creates the {@link InstallationsSidebarToggleGroup} which contains all active installation categories
     */
    private InstallationsSidebarToggleGroup createSidebarToggleGroup() {
        final FilteredList<InstallationCategoryDTO> filteredInstallationCategories = getControl().getItems()
                .filtered(getControl().getFilter()::filter);

        filteredInstallationCategories.predicateProperty().bind(
                Bindings.createObjectBinding(() -> getControl().getFilter()::filter,
                        getControl().searchTermProperty()));

        final InstallationsSidebarToggleGroup categoryView = new InstallationsSidebarToggleGroup(tr("Categories"),
                filteredInstallationCategories);

        getControl().selectedInstallationCategoryProperty().bind(categoryView.selectedElementProperty());

        return categoryView;
    }
}
