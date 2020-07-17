package org.phoenicis.javafx.components.application.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import org.phoenicis.javafx.components.application.control.ApplicationSidebar;
import org.phoenicis.javafx.components.application.control.ApplicationSidebarToggleGroup;
import org.phoenicis.javafx.components.common.control.SidebarGroup;
import org.phoenicis.javafx.components.common.skin.ExtendedSidebarSkinBase;
import org.phoenicis.repository.dto.CategoryDTO;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * The skin for the {@link ApplicationSidebar} component
 */
public class ApplicationSidebarSkin
        extends ExtendedSidebarSkinBase<CategoryDTO, ApplicationSidebar, ApplicationSidebarSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ApplicationSidebarSkin(ApplicationSidebar control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ScrollPane createMainContent() {
        return createScrollPane(createSidebarToggleGroup(), createSpacer(), createFilterGroup());
    }

    private ApplicationSidebarToggleGroup createSidebarToggleGroup() {
        final FilteredList<CategoryDTO> filteredCategories = getControl().getItems()
                .filtered(getControl()::filterCategory);

        filteredCategories.predicateProperty().bind(
                Bindings.createObjectBinding(() -> getControl()::filterCategory,
                        getControl().searchTermProperty(),
                        getControl().fuzzySearchRatioProperty(),
                        getControl().operatingSystemProperty(),
                        getControl().containAllOSCompatibleApplicationsProperty(),
                        getControl().containCommercialApplicationsProperty(),
                        getControl().containRequiresPatchApplicationsProperty(),
                        getControl().containTestingApplicationsProperty()));

        final ApplicationSidebarToggleGroup applicationSidebarToggleGroup = new ApplicationSidebarToggleGroup(
                filteredCategories);

        applicationSidebarToggleGroup.setTitle(tr("Categories"));
        getControl().selectedItemProperty().bind(applicationSidebarToggleGroup.selectedElementProperty());

        return applicationSidebarToggleGroup;
    }

    private SidebarGroup<CheckBox> createFilterGroup() {
        final CheckBox testingCheck = createCheckBox(tr("Testing"), tr("Also show apps in testing state"));
        getControl().containTestingApplicationsProperty().bind(testingCheck.selectedProperty());

        final CheckBox requiresPatchCheck = createCheckBox(tr("Patch required"),
                tr("Also show apps, where CD patch is necessary"));
        getControl().containRequiresPatchApplicationsProperty().bind(requiresPatchCheck.selectedProperty());

        final CheckBox commercialCheck = createCheckBox(tr("Commercial"), tr("Also show apps not free of costs"));
        commercialCheck.setSelected(true);
        getControl().containCommercialApplicationsProperty().bind(commercialCheck.selectedProperty());

        final CheckBox operatingSystemCheck = createCheckBox(tr("All Operating Systems"),
                tr("Also show apps tested on different OS"));
        getControl().containAllOSCompatibleApplicationsProperty().bind(operatingSystemCheck.selectedProperty());

        return new SidebarGroup<>(tr("Filters"), FXCollections.observableArrayList(
                testingCheck, requiresPatchCheck, commercialCheck, operatingSystemCheck));
    }
}
