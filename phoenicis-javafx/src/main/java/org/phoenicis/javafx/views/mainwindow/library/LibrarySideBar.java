package org.phoenicis.javafx.views.mainwindow.library;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;
import javafx.stage.FileChooser;
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

    // the toggleable categories
    private LeftToggleGroup<ShortcutCategoryDTO> categoryView;

    // consumer called after a text has been entered in the search box
    private Consumer<String> onSearch;

    // the shortcut group, containing the run, stop and uninstall buttons
    private LeftGroup shortcutGroup;

    private LeftButton runButton;
    private LeftButton stopButton;
    private LeftButton uninstallButton;

    // the advanced tools group, containing the run script and run console buttons
    private LeftGroup advancedToolsGroup;

    private LeftButton runScript;
    private LeftButton runConsole;

    // the current selected shortcut
    private ShortcutDTO shortcut;

    // consumers called after a category selection has been made
    private Runnable onAllCategorySelection;
    private Consumer<ShortcutCategoryDTO> onCategorySelection;

    // consumers called when a shortcut should be run, stopped or uninstalled
    private Consumer<ShortcutDTO> onShortcutRun;
    private Consumer<ShortcutDTO> onShortcutStop;
    private Consumer<ShortcutDTO> onShortcutUninstall;

    // consumers called when a script should be run or a console be opened
    private Consumer<File> onScriptRun;
    private Runnable onOpenConsole;

    /**
     * Constructor
     *
     * @param applicationName The name of this application (normally "PlayOnLinux")
     */
    public LibrarySideBar(String applicationName) {
        super();

        this.applicationName = applicationName;

        this.populateSearchBar();
        this.populateShortcut();
        this.populateCategories();
        this.populateAdvancedTools();

        this.hideShortcut();
    }

    /**
     * This method selects the "All" application category
     */
    public void selectAllCategories() {
        this.categoryView.selectAll();
    }

    /**
     * This method sets the to be shown shortcut (including its name).
     * Afterwards it updates the sidebar view to show the searchbar, the shortcut related buttons (run, stop and uninstall) and
     * the advanced tool buttons (run a script and open a console).
     * If a shortcut is already shown the shortcut information will be updated to reflect the new shortcut.
     *
     * @param shortcut The new shortcut to be shown to the user
     */
    public void showShortcut(ShortcutDTO shortcut) {
        this.shortcut = shortcut;

        this.shortcutGroup.setTitle(shortcut.getName());

        this.getChildren().setAll(this.searchBar, new LeftSpacer(), this.categoryView, this.shortcutGroup, new LeftSpacer(),
                this.advancedToolsGroup);
    }

    /**
     * This method hides the currently shown shortcut.
     * If no shortcut is currently shown, this method does nothing.
     */
    public void hideShortcut() {
        this.shortcut = null;

        this.getChildren().setAll(this.searchBar, new LeftSpacer(), this.categoryView, this.advancedToolsGroup);
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
        this.runButton = new LeftButton(translate("Run"));
        this.runButton.getStyleClass().add("runButton");

        this.stopButton = new LeftButton(translate("Close"));
        this.stopButton.getStyleClass().add("stopButton");

        this.uninstallButton = new LeftButton(translate("Uninstall"));
        this.uninstallButton.getStyleClass().add("uninstallButton");

        this.runButton.setOnMouseClicked(event -> onShortcutRun.accept(shortcut));
        this.stopButton.setOnMouseClicked(event -> onShortcutStop.accept(shortcut));
        this.uninstallButton.setOnMouseClicked(event -> {
            onShortcutUninstall.accept(shortcut);
            hideShortcut();
        });

        this.shortcutGroup = new LeftGroup("Unknown application", runButton, stopButton, uninstallButton);
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
     * This method updates the consumer, that is called when the "Run" button for the currently selected shortcut has been clicked.
     *
     * @param onShortcutRun The new consumer to be called
     */
    public void setOnShortcutRun(Consumer<ShortcutDTO> onShortcutRun) {
        this.onShortcutRun = onShortcutRun;
    }

    /**
     * This method updates the consumer, that is called when the "Stop" button for the currently selected shortcut has been clicked.
     *
     * @param onShortcutStop The new consumer to be called
     */
    public void setOnShortcutStop(Consumer<ShortcutDTO> onShortcutStop) {
        this.onShortcutStop = onShortcutStop;
    }

    /**
     * This method updates the consumer, that is called when the "Uninstall" button for the currently selected shortcut has been clicked.
     *
     * @param onShortcutUninstall The new consumer to be called
     */
    public void setOnShortcutUninstall(Consumer<ShortcutDTO> onShortcutUninstall) {
        this.onShortcutUninstall = onShortcutUninstall;
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
