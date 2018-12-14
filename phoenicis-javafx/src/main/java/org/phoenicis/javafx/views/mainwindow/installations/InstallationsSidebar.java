package org.phoenicis.javafx.views.mainwindow.installations;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.phoenicis.javafx.components.installation.control.InstallationsSidebarToggleGroup;
import org.phoenicis.javafx.components.common.control.ListWidgetSelector;
import org.phoenicis.javafx.components.common.control.SearchBox;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;
import org.phoenicis.javafx.views.mainwindow.ui.Sidebar;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarScrollPane;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarSpacer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An instance of this class represents the sidebar of the installations tab view.
 * This sidebar contains:
 * <ul>
 * <li>
 * A searchbar, which enables the user to search for a currently running.
 * </li>
 * <li>
 * A list of installation categories.
 * </li>
 * </ul>
 *
 * @author marc
 * @since 15.04.17
 */
public class InstallationsSidebar extends Sidebar {
    private final InstallationsFilter filter;
    private final JavaFxSettingsManager javaFxSettingsManager;

    private final ObservableList<InstallationCategoryDTO> installationCategories;

    /**
     * Constructor
     *
     * @param activeInstallations The list widget to be managed by the ListWidgetChooser in the sidebar
     * @param javaFxSettingsManager The settings manager for the JavaFX GUI
     */
    public InstallationsSidebar(InstallationsFilter filter, JavaFxSettingsManager javaFxSettingsManager,
            ObservableList<InstallationCategoryDTO> installationCategories,
            CombinedListWidget<InstallationDTO> activeInstallations) {
        super();

        this.filter = filter;
        this.javaFxSettingsManager = javaFxSettingsManager;
        this.installationCategories = installationCategories;

        initialise(activeInstallations);
    }

    private void initialise(CombinedListWidget<InstallationDTO> activeInstallations) {
        SearchBox searchBox = createSearchBox();
        InstallationsSidebarToggleGroup sidebarToggleGroup = createSidebarToggleGroup();
        ListWidgetSelector listWidgetSelector = createListWidgetSelector(activeInstallations);

        setTop(searchBox);
        setCenter(new SidebarScrollPane(sidebarToggleGroup, new SidebarSpacer()));
        setBottom(listWidgetSelector);
    }

    /**
     * This method populates the searchbar
     */
    private SearchBox createSearchBox() {
        final SearchBox searchBox = new SearchBox();

        filter.searchTermProperty().bind(searchBox.searchTermProperty());

        return searchBox;
    }

    private InstallationsSidebarToggleGroup createSidebarToggleGroup() {
        final FilteredList<InstallationCategoryDTO> filteredInstallationCategories = installationCategories
                .filtered(filter::filter);

        filteredInstallationCategories.predicateProperty().bind(
                Bindings.createObjectBinding(() -> filter::filter, filter.searchTermProperty()));

        final InstallationsSidebarToggleGroup categoryView = new InstallationsSidebarToggleGroup(tr("Categories"),
                filteredInstallationCategories);

        filter.selectedInstallationCategoryProperty().bind(categoryView.selectedElementProperty());

        return categoryView;
    }

    /**
     * This method populates the list widget chooser
     *
     * @param activeInstallations The managed CombinedListWidget
     */
    private ListWidgetSelector createListWidgetSelector(CombinedListWidget<InstallationDTO> activeInstallations) {
        ListWidgetSelector listWidgetSelector = new ListWidgetSelector();

        listWidgetSelector.setSelected(javaFxSettingsManager.getInstallationsListType());
        listWidgetSelector.setOnSelect(type -> {
            activeInstallations.showList(type);

            javaFxSettingsManager.setInstallationsListType(type);
            javaFxSettingsManager.save();
        });

        return listWidgetSelector;
    }
}
