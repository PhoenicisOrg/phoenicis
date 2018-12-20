package org.phoenicis.javafx.views.mainwindow.engines;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.CheckBox;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.components.common.control.ListWidgetSelector;
import org.phoenicis.javafx.components.common.control.SearchBox;
import org.phoenicis.javafx.components.common.control.SidebarGroup;
import org.phoenicis.javafx.components.engine.control.EnginesSidebarToggleGroup;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.Sidebar;

import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An instance of this class represents the sidebar of the engines tab view.
 * This sidebar contains three items:
 * <ul>
 * <li>
 * A searchbar, which enables the user to search for an engine.
 * </li>
 * <li>
 * A button group containing a button for all known engine groups.
 * After pressing on one such button all engines belonging to the selected engine group are shown in the main window
 * panel.
 * </li>
 * <li>
 * A button group containing buttons to filter for installed and uninstalled engines.
 * </li>
 * </ul>
 *
 * @author marc
 * @since 22.04.17
 */
public class EnginesSidebar extends Sidebar {
    private final EnginesFilter filter;
    private final JavaFxSettingsManager javaFxSettingsManager;

    private final ObservableList<EngineCategoryDTO> engineCategories;

    /**
     * Constructor
     *
     * @param enginesVersionListWidgets The list widget to be managed by the ListWidgetChooser in the sidebar
     * @param javaFxSettingsManager The settings manager for the JavaFX GUI
     */
    public EnginesSidebar(EnginesFilter filter, JavaFxSettingsManager javaFxSettingsManager,
            ObservableList<EngineCategoryDTO> engineCategories,
            List<CombinedListWidget<EngineVersionDTO>> enginesVersionListWidgets) {
        super();

        this.filter = filter;
        this.javaFxSettingsManager = javaFxSettingsManager;
        this.engineCategories = engineCategories;

        initialise(enginesVersionListWidgets);
    }

    private void initialise(List<CombinedListWidget<EngineVersionDTO>> enginesVersionListWidgets) {
        SearchBox searchBox = createSearchBox();
        EnginesSidebarToggleGroup categoryView = createSidebarToggleGroup();
        SidebarGroup<CheckBox> installationFilterGroup = createInstallationFilters();
        ListWidgetSelector listWidgetSelector = createListWidgetSelector(enginesVersionListWidgets);

        setTop(searchBox);
        setCenter(createScrollPane(categoryView, createSpacer(), installationFilterGroup));
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

    /**
     * This method populates the button group showing all known engine categories
     */
    private EnginesSidebarToggleGroup createSidebarToggleGroup() {
        final FilteredList<EngineCategoryDTO> filteredEngineCategories = engineCategories.filtered(filter::filter);

        filteredEngineCategories.predicateProperty().bind(
                Bindings.createObjectBinding(() -> filter::filter,
                        filter.searchTermProperty(),
                        filter.showInstalledProperty(),
                        filter.showNotInstalledProperty()));

        final EnginesSidebarToggleGroup categoryView = new EnginesSidebarToggleGroup(tr("Engines"),
                filteredEngineCategories);

        filter.selectedEngineCategoryProperty().bind(categoryView.selectedElementProperty());

        return categoryView;
    }

    /**
     * This method populates the button group containing buttons to filter for installed and not installed engines
     */
    private SidebarGroup<CheckBox> createInstallationFilters() {
        final CheckBox installedCheck = new CheckBox(tr("Installed"));
        installedCheck.getStyleClass().add("sidebarCheckBox");
        installedCheck.setSelected(true);
        filter.showInstalledProperty().bind(installedCheck.selectedProperty());

        final CheckBox notInstalledCheck = new CheckBox(tr("Not installed"));
        notInstalledCheck.getStyleClass().add("sidebarCheckBox");
        notInstalledCheck.setSelected(true);
        filter.showNotInstalledProperty().bind(notInstalledCheck.selectedProperty());

        final SidebarGroup<CheckBox> installationFilterGroup = new SidebarGroup<>();
        installationFilterGroup.getComponents().addAll(installedCheck, notInstalledCheck);

        return installationFilterGroup;
    }

    /**
     * This method populates the list widget choose
     *
     * @param enginesVersionListWidgets The managed CombinedListWidgets
     */
    private ListWidgetSelector createListWidgetSelector(
            List<CombinedListWidget<EngineVersionDTO>> enginesVersionListWidgets) {
        ListWidgetSelector listWidgetSelector = new ListWidgetSelector();

        listWidgetSelector.setSelected(javaFxSettingsManager.getEnginesListType());
        listWidgetSelector.setOnSelect(type -> {
            enginesVersionListWidgets.forEach(widget -> widget.showList(type));

            javaFxSettingsManager.setEnginesListType(type);
            javaFxSettingsManager.save();
        });

        return listWidgetSelector;
    }
}
