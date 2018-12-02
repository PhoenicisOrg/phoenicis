package org.phoenicis.javafx.views.mainwindow.installations;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.control.InstallationsSidebarToggleGroup;
import org.phoenicis.javafx.components.control.ListWidgetSelector;
import org.phoenicis.javafx.components.control.SearchBox;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.DelayedFilterTextConsumer;
import org.phoenicis.javafx.views.common.lists.PhoenicisFilteredList;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;
import org.phoenicis.javafx.views.mainwindow.ui.Sidebar;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarScrollPane;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarSpacer;

import java.util.function.Consumer;

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

    // the search bar used for filtering
    private SearchBox searchBar;

    // container for the center content of this sidebar
    private SidebarScrollPane centerContent;

    private ObservableList<InstallationCategoryDTO> installationCategories;
    private PhoenicisFilteredList<InstallationCategoryDTO> filteredinstallationCategories;

    // the toggleable categories
    private InstallationsSidebarToggleGroup categoryView;

    // widget to switch between the different list widgets in the center view
    private ListWidgetSelector listWidgetSelector;

    // consumers called after a category selection has been made
    private Runnable onAllCategorySelection;
    private Consumer<InstallationCategoryDTO> onCategorySelection;

    private final JavaFxSettingsManager javaFxSettingsManager;

    /**
     * Constructor
     *
     * @param activeInstallations The list widget to be managed by the ListWidgetChooser in the sidebar
     * @param javaFxSettingsManager The settings manager for the JavaFX GUI
     */
    public InstallationsSidebar(CombinedListWidget<InstallationDTO> activeInstallations, InstallationsFilter filter,
            JavaFxSettingsManager javaFxSettingsManager) {
        super();

        this.filter = filter;
        this.javaFxSettingsManager = javaFxSettingsManager;

        this.populateSearchBar();
        this.populateCategories();
        this.populateListWidgetChooser(activeInstallations);

        this.centerContent = new SidebarScrollPane(this.categoryView, new SidebarSpacer());

        this.setTop(searchBar);
        this.setCenter(centerContent);
        this.setBottom(listWidgetSelector);
    }

    /**
     * This method binds the given category list <code>categories</code> to the categories toggle group.
     *
     * @param categories The to be bound category list
     */
    public void bindCategories(ObservableList<InstallationCategoryDTO> categories) {
        Bindings.bindContent(installationCategories, categories);
    }

    /**
     * This method populates the searchbar
     */
    private void populateSearchBar() {
        this.searchBar = new SearchBox(new DelayedFilterTextConsumer(this::search), this::clearSearch);
    }

    private void populateCategories() {
        this.installationCategories = FXCollections.observableArrayList();
        this.filteredinstallationCategories = new PhoenicisFilteredList<>(this.installationCategories, filter::filter);
        this.filter.addOnFilterChanged(filteredinstallationCategories::trigger);

        this.categoryView = new InstallationsSidebarToggleGroup(tr("Categories"));

        this.categoryView.setOnAllCategorySelection(() -> onAllCategorySelection.run());
        this.categoryView.setOnCategorySelection(category -> onCategorySelection.accept(category));

        Bindings.bindContent(categoryView.getElements(), filteredinstallationCategories);
    }

    /**
     * This method populates the list widget chooser
     *
     * @param activeInstallations The managed CombinedListWidget
     */
    private void populateListWidgetChooser(CombinedListWidget<InstallationDTO> activeInstallations) {
        this.listWidgetSelector = new ListWidgetSelector();
        this.listWidgetSelector.setSelected(this.javaFxSettingsManager.getInstallationsListType());
        this.listWidgetSelector.setOnSelect(type -> {
            activeInstallations.showList(type);

            this.javaFxSettingsManager.setInstallationsListType(type);
            this.javaFxSettingsManager.save();
        });
    }

    public void search(String searchTerm) {
        this.filter.setSearchTerm(searchTerm);
    }

    public void clearSearch() {
        this.filter.clearSearchTerm();
    }

    /**
     * This method sets the consumer, that is called after a category has been selected
     *
     * @param onAllCategorySelection The new consumer to be used
     */
    public void setOnAllCategorySelection(Runnable onAllCategorySelection) {
        this.onAllCategorySelection = onAllCategorySelection;
    }

    /**
     * This method sets the consumer, that is called after the "All" categories toggle button has been selected
     *
     * @param onCategorySelection The new consumer to be used
     */
    public void setOnCategorySelection(Consumer<InstallationCategoryDTO> onCategorySelection) {
        this.onCategorySelection = onCategorySelection;
    }
}
