package org.phoenicis.javafx.views.mainwindow.apps;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.CheckBox;
import org.phoenicis.javafx.components.application.control.ApplicationSidebarToggleGroup;
import org.phoenicis.javafx.components.common.control.ListWidgetSelector;
import org.phoenicis.javafx.components.common.control.SearchBox;
import org.phoenicis.javafx.components.common.control.SidebarGroup;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.Sidebar;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An instance of this class represents the sidebar of the apps tab view.
 * This sidebar contains three items:
 * <ul>
 * <li>
 * A searchbar, which enables the user to search for an application in the selected categories of his/her repositories.
 * </li>
 * <li>
 * A toggle group containing all categories contained in his/her repositories including an "All" category.
 * </li>
 * <li>
 * A filter group, containing filters to be used to remove testing, requires patch and
 * commercial applications from the shown applications
 * </li>
 * </ul>
 *
 * @author marc
 * @since 21.04.17
 */
public class ApplicationsSidebar extends Sidebar {
    private final ApplicationFilter filter;
    private final JavaFxSettingsManager javaFxSettingsManager;

    private final ObservableList<CategoryDTO> categories;

    /**
     * Constructor
     *
     * @param combinedListWidget The list widget to be managed by the ListWidgetChooser in the sidebar
     * @param javaFxSettingsManager The settings manager for the JavaFX GUI
     */
    public ApplicationsSidebar(ApplicationFilter filter, JavaFxSettingsManager javaFxSettingsManager,
            ObservableList<CategoryDTO> categories, CombinedListWidget<ApplicationDTO> combinedListWidget) {
        super();

        this.filter = filter;
        this.javaFxSettingsManager = javaFxSettingsManager;
        this.categories = categories;

        initialize(combinedListWidget);
    }

    private void initialize(final CombinedListWidget<ApplicationDTO> combinedListWidget) {
        SearchBox searchBox = createSearchBox();
        ListWidgetSelector listWidgetSelector = createListWidgetSelector(combinedListWidget);

        ApplicationSidebarToggleGroup sidebarToggleGroup = createSidebarToggleGroup();
        SidebarGroup<CheckBox> filterGroup = createFilterGroup();

        this.setTop(searchBox);
        this.setCenter(createScrollPane(sidebarToggleGroup, createSpacer(), filterGroup));
        this.setBottom(listWidgetSelector);
    }

    private SearchBox createSearchBox() {
        final SearchBox searchBox = new SearchBox();

        filter.filterTextProperty().bind(searchBox.searchTermProperty());

        return searchBox;
    }

    private ApplicationSidebarToggleGroup createSidebarToggleGroup() {
        final FilteredList<CategoryDTO> filteredCategories = categories.filtered(filter::filter);

        filteredCategories.predicateProperty().bind(
                Bindings.createObjectBinding(() -> filter::filter,
                        filter.filterTextProperty(),
                        filter.containAllOSCompatibleApplicationsProperty(),
                        filter.containCommercialApplicationsProperty(),
                        filter.containRequiresPatchApplicationsProperty(),
                        filter.containTestingApplicationsProperty()));

        ApplicationSidebarToggleGroup categoryView = new ApplicationSidebarToggleGroup(tr("Categories"),
                filteredCategories);

        filter.filterCategoryProperty().bind(categoryView.selectedElementProperty());

        return categoryView;
    }

    private SidebarGroup<CheckBox> createFilterGroup() {
        final CheckBox testingCheck = new CheckBox(tr("Testing"));
        testingCheck.getStyleClass().add("sidebarCheckBox");
        filter.containTestingApplicationsProperty().bind(testingCheck.selectedProperty());

        final CheckBox requiresPatchCheck = new CheckBox(tr("Patch required"));
        requiresPatchCheck.getStyleClass().add("sidebarCheckBox");
        filter.containRequiresPatchApplicationsProperty().bind(requiresPatchCheck.selectedProperty());

        final CheckBox commercialCheck = new CheckBox(tr("Commercial"));
        commercialCheck.getStyleClass().add("sidebarCheckBox");
        commercialCheck.setSelected(true);
        filter.containCommercialApplicationsProperty().bind(commercialCheck.selectedProperty());

        final CheckBox operatingSystemCheck = new CheckBox(tr("All Operating Systems"));
        operatingSystemCheck.getStyleClass().add("sidebarCheckBox");
        filter.containAllOSCompatibleApplicationsProperty().bind(operatingSystemCheck.selectedProperty());

        final SidebarGroup<CheckBox> filterGroup = new SidebarGroup<>(tr("Filters"));
        filterGroup.getComponents().addAll(testingCheck, requiresPatchCheck, commercialCheck, operatingSystemCheck);

        return filterGroup;
    }

    /**
     * This method populates the list widget choose
     *
     * @param combinedListWidget The managed CombinedListWidget
     */
    private ListWidgetSelector createListWidgetSelector(CombinedListWidget<ApplicationDTO> combinedListWidget) {
        final ListWidgetSelector listWidgetSelector = new ListWidgetSelector();

        listWidgetSelector.setSelected(this.javaFxSettingsManager.getAppsListType());
        listWidgetSelector.setOnSelect(type -> {
            combinedListWidget.showList(type);

            this.javaFxSettingsManager.setAppsListType(type);
            this.javaFxSettingsManager.save();
        });

        return listWidgetSelector;
    }

    public ObservableList<CategoryDTO> getCategories() {
        return categories;
    }
}
