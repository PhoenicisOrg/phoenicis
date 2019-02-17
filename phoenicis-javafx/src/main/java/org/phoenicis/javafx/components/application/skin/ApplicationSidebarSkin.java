package org.phoenicis.javafx.components.application.skin;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import org.phoenicis.javafx.components.application.control.ApplicationSidebar;
import org.phoenicis.javafx.components.application.control.ApplicationSidebarToggleGroup;
import org.phoenicis.javafx.components.common.control.SidebarGroup;
import org.phoenicis.javafx.components.common.skin.ExtendedSidebarSkinBase;
import org.phoenicis.javafx.utils.CollectionBindings;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationFilter;
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
        final ObservableList<CategoryDTO> filteredCategories = CollectionBindings
                .mapToObservableList(getControl().filterProperty(),
                        filter -> filter.createFilteredList(getControl().getItems()));

        return new ApplicationSidebarToggleGroup(tr("Categories"),
                filteredCategories, getControl().filterCategoryProperty());
    }

    private SidebarGroup<CheckBox> createFilterGroup() {
        final CheckBox testingCheck = createCheckBox(tr("Testing"));
        getControl().containTestingApplicationsProperty().bind(testingCheck.selectedProperty());

        final CheckBox requiresPatchCheck = createCheckBox(tr("Patch required"));
        getControl().containRequiresPatchApplicationsProperty().bind(requiresPatchCheck.selectedProperty());

        final CheckBox commercialCheck = createCheckBox(tr("Commercial"));
        commercialCheck.setSelected(true);
        getControl().containCommercialApplicationsProperty().bind(commercialCheck.selectedProperty());

        final CheckBox operatingSystemCheck = createCheckBox(tr("All Operating Systems"));
        getControl().containAllOSCompatibleApplicationsProperty().bind(operatingSystemCheck.selectedProperty());

        return new SidebarGroup<>(tr("Filters"), FXCollections.observableArrayList(
                testingCheck, requiresPatchCheck, commercialCheck, operatingSystemCheck));
    }
}
