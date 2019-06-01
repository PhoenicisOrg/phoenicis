package org.phoenicis.javafx.components.engine.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.javafx.components.common.control.SidebarGroup;
import org.phoenicis.javafx.components.common.skin.ExtendedSidebarSkinBase;
import org.phoenicis.javafx.components.engine.control.EngineSidebar;
import org.phoenicis.javafx.components.engine.control.EnginesSidebarToggleGroup;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A skin implementation for the {@link EngineSidebar} component
 */
public class EngineSidebarSkin extends ExtendedSidebarSkinBase<EngineCategoryDTO, EngineSidebar, EngineSidebarSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public EngineSidebarSkin(EngineSidebar control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ScrollPane createMainContent() {
        return createScrollPane(createSidebarToggleGroup(), createSpacer(), createInstallationFilters());
    }

    /**
     * Creates the {@link EnginesSidebarToggleGroup} which contains all known engine categories
     */
    private EnginesSidebarToggleGroup createSidebarToggleGroup() {
        final FilteredList<EngineCategoryDTO> filteredEngineCategories = getControl().getItems()
                .filtered(getControl().getFilter()::filter);

        filteredEngineCategories.predicateProperty().bind(
                Bindings.createObjectBinding(() -> getControl().getFilter()::filter,
                        getControl().searchTermProperty(),
                        getControl().showInstalledProperty(),
                        getControl().showNotInstalledProperty()));

        final EnginesSidebarToggleGroup categoryView = new EnginesSidebarToggleGroup(tr("Engines"),
                filteredEngineCategories);

        getControl().selectedEngineCategoryProperty().bind(categoryView.selectedElementProperty());

        return categoryView;
    }

    /**
     * Creates the {@link SidebarGroup} containing buttons to filter for installed and not installed engines
     */
    private SidebarGroup<CheckBox> createInstallationFilters() {
        final CheckBox installedCheck = new CheckBox(tr("Installed"));
        installedCheck.getStyleClass().add("sidebarCheckBox");
        installedCheck.setSelected(true);
        getControl().showInstalledProperty().bind(installedCheck.selectedProperty());

        final CheckBox notInstalledCheck = new CheckBox(tr("Not installed"));
        notInstalledCheck.getStyleClass().add("sidebarCheckBox");
        notInstalledCheck.setSelected(true);
        getControl().showNotInstalledProperty().bind(notInstalledCheck.selectedProperty());

        final SidebarGroup<CheckBox> installationFilterGroup = new SidebarGroup<>();
        installationFilterGroup.getComponents().addAll(installedCheck, notInstalledCheck);

        return installationFilterGroup;
    }
}
