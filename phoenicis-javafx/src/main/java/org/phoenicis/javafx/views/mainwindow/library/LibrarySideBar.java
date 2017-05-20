package org.phoenicis.javafx.views.mainwindow.library;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.stage.FileChooser;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.*;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutDTO;

import java.io.File;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

/**
 * An instance of this class represents the left sidebar of the library tab view.
 * This sidebar contains three items:
 * <ul>
 * <li>
 * A searchbar, which enables the user to search for an application inside his application library.
 * </li>
 * <li>
 * A button group containing a run, stop and uninstall button for a selected application in the library.
 * This group is only shown, if an application is currently selected.
 * </li>
 * <li>
 * A button group containing a "Run a script" and "PlayOnLinux console" button.
 * This group is always shown.
 * </li>
 * </ul>
 *
 * @author marc
 * @since 15.04.17
 */
public class LibrarySideBar extends LeftSideBar {
    // the name of this application
    private final String applicationName;

    // the search bar used for filtering
    private SearchBox searchBar;

    // container for the center content of this sidebar
    private LeftScrollPane centerContent;

    // the toggleable categories
    private LeftToggleGroup<ShortcutCategoryDTO> categoryView;

    // consumer called after a text has been entered in the search box
    private Consumer<String> onSearch;

    // the advanced tools group, containing the run script and run console buttons
    private LeftGroup advancedToolsGroup;

    private LeftButton runScript;
    private LeftButton runConsole;

    // widget to switch between the different list widgets in the center view
    private LeftListWidgetChooser<ShortcutDTO> listWidgetChooser;

    // consumers called after a category selection has been made
    private Runnable onAllCategorySelection;
    private Consumer<ShortcutCategoryDTO> onCategorySelection;

    // consumers called when a script should be run or a console be opened
    private Consumer<File> onScriptRun;
    private Runnable onOpenConsole;

    /**
     * Constructor
     *
     * @param applicationName    The name of this application (normally "PlayOnLinux")
     * @param availableShortcuts The list widget to be managed by the ListWidgetChooser in the sidebar
     */
    public LibrarySideBar(String applicationName, CombinedListWidget<ShortcutDTO> availableShortcuts) {
        super();

        this.applicationName = applicationName;

        this.populateSearchBar();
        this.populateShortcut();
        this.populateCategories();
        this.populateAdvancedTools();
        this.populateListWidgetChooser(availableShortcuts);

        this.centerContent = new LeftScrollPane(this.categoryView, new LeftSpacer(), this.advancedToolsGroup);

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
    public void bindCategories(ObservableList<ShortcutCategoryDTO> categories) {
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
        this.categoryView = LeftToggleGroup.create(translate("Categories"), this::createAllCategoriesToggleButton,
                this::createCategoryToggleButton);
    }

    /**
     * This method populates the list widget choose
     *
     * @param availableShortcuts The managed CombinedListWidget
     */
    private void populateListWidgetChooser(CombinedListWidget<ShortcutDTO> availableShortcuts) {
        this.listWidgetChooser = new LeftListWidgetChooser<>(availableShortcuts);
        this.listWidgetChooser.setAlignment(Pos.BOTTOM_LEFT);
    }

    /**
     * This method is responsible for creating the "All" categories toggle button.
     *
     * @return The newly created "All" categories toggle button
     */
    private ToggleButton createAllCategoriesToggleButton() {
        final LeftToggleButton allCategoryButton = new LeftToggleButton("All");

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
    private ToggleButton createCategoryToggleButton(ShortcutCategoryDTO category) {
        final LeftToggleButton categoryButton = new LeftToggleButton(category.getName());

        categoryButton.setId(String.format("%sButton", category.getName().toLowerCase()));
        categoryButton.setOnMouseClicked(event -> onCategorySelection.accept(category));

        return categoryButton;
    }

    /**
     * This method populates the shortcut button group.
     */
    private void populateShortcut() {
    }

    /**
     * This method populates the advanced tools button group.
     */
    private void populateAdvancedTools() {
        this.runScript = new LeftButton(translate("Run a script"));
        this.runScript.getStyleClass().add("scriptButton");

        this.runConsole = new LeftButton(translate(String.format("%s console", applicationName)));
        this.runConsole.getStyleClass().add("consoleButton");

        this.runScript.setOnMouseClicked(event -> {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open a script");

            // TODO: use correct owner window
            final File scriptToRun = fileChooser.showOpenDialog(null);

            if (scriptToRun != null) {
                onScriptRun.accept(scriptToRun);
            }
        });
        this.runConsole.setOnMouseClicked(event -> onOpenConsole.run());

        this.advancedToolsGroup = new LeftGroup("Advanced tools", runScript, runConsole);
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
    public void setOnCategorySelection(Consumer<ShortcutCategoryDTO> onCategorySelection) {
        this.onCategorySelection = onCategorySelection;
    }

    /**
     * This method updates the consumer, that is called when the "Run a script" button in the advanced tools section has been clicked.
     *
     * @param onScriptRun The new consumer to be called
     */
    public void setOnScriptRun(Consumer<File> onScriptRun) {
        this.onScriptRun = onScriptRun;
    }

    /**
     * This methdo updates the consumer that is called when the "PlayOnLinux console" button ins the advanced tools section has been clicked.
     *
     * @param onOpenConsole The new consumer to be called
     */
    public void setOnOpenConsole(Runnable onOpenConsole) {
        this.onOpenConsole = onOpenConsole;
    }
}
