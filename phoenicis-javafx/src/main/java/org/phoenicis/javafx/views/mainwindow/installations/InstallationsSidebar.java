package org.phoenicis.javafx.views.mainwindow.installations;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;
import org.phoenicis.javafx.views.mainwindow.ui.*;

import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An instance of this class represents the left sidebar of the installations tab view.
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
public class InstallationsSidebar extends LeftSidebar {

    // the search bar used for filtering
    private SearchBox searchBar;

    // container for the center content of this sidebar
    private LeftScrollPane centerContent;

    // the toggleable categories
    private LeftToggleGroup<InstallationCategoryDTO> categoryView;

    // consumer called after a text has been entered in the search box
    private Consumer<String> onSearch = (searchKeyword) -> {
    };

    // widget to switch between the different list widgets in the center view
    private LeftListWidgetChooser<InstallationDTO> listWidgetChooser;

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
    public InstallationsSidebar(CombinedListWidget<InstallationDTO> activeInstallations,
            JavaFxSettingsManager javaFxSettingsManager) {
        super();

        this.javaFxSettingsManager = javaFxSettingsManager;

        this.populateSearchBar();
        this.populateCategories();
        this.populateListWidgetChooser(activeInstallations);

        this.centerContent = new LeftScrollPane(this.categoryView, new LeftSpacer());

        this.setTop(searchBar);
        this.setCenter(centerContent);
        this.setBottom(listWidgetChooser);
    }

    /**
     * This method selects the "All" application category
     */
    public void selectAllCategories() {
        this.categoryView.selectAll();
    }

    /**
     * This method binds the given category list <code>categories</code> to the categories toggle group.
     *
     * @param categories The to be bound category list
     */
    public void bindCategories(ObservableList<InstallationCategoryDTO> categories) {
        Bindings.bindContent(categoryView.getElements(), categories);
    }

    /**
     * This method populates the searchbar
     */
    private void populateSearchBar() {
        this.searchBar = new SearchBox(onSearch, () -> {
        });
    }

    private void populateCategories() {
        this.categoryView = LeftToggleGroup.create(tr("Categories"), this::createAllCategoriesToggleButton,
                this::createCategoryToggleButton);
    }

    /**
     * This method populates the list widget chooser
     *
     * @param activeInstallations The managed CombinedListWidget
     */
    private void populateListWidgetChooser(CombinedListWidget<InstallationDTO> activeInstallations) {
        this.listWidgetChooser = new LeftListWidgetChooser<>(activeInstallations);
        this.listWidgetChooser.setAlignment(Pos.BOTTOM_LEFT);
        this.listWidgetChooser.choose(this.javaFxSettingsManager.getInstallationsListType());
        this.listWidgetChooser.setOnChoose(type -> {
            this.javaFxSettingsManager.setInstallationsListType(type);
            this.javaFxSettingsManager.save();
        });
    }

    /**
     * This method is responsible for creating the "All" categories toggle button.
     *
     * @return The newly created "All" categories toggle button
     */
    private ToggleButton createAllCategoriesToggleButton() {
        final LeftToggleButton allCategoryButton = new LeftToggleButton(tr("All"));

        allCategoryButton.setSelected(true);
        allCategoryButton.setId("allButton");
        allCategoryButton.setOnMouseClicked(event -> onAllCategorySelection.run());

        return allCategoryButton;
    }

    /**
     * This method is responsible for creating a toggle button for a given category.
     *
     * @param category The category for which a toggle button should be created
     * @return The newly created toggle button
     */
    private ToggleButton createCategoryToggleButton(InstallationCategoryDTO category) {
        final LeftToggleButton categoryButton = new LeftToggleButton(category.getName());

        categoryButton.setId(String.format("%sButton", category.getId().toLowerCase()));
        categoryButton.setOnMouseClicked(event -> onCategorySelection.accept(category));

        return categoryButton;
    }

    /**
     * This method updates the consumer, that is called when a search term has been entered.
     *
     * @param onSearch The new consumer to be called
     */
    public void setOnSearch(Consumer<String> onSearch) {
        this.onSearch = onSearch;
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
