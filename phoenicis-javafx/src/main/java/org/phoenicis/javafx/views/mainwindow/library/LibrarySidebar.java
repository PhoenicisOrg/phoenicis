package org.phoenicis.javafx.views.mainwindow.library;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import org.phoenicis.javafx.components.control.LibrarySidebarToggleGroup;
import org.phoenicis.javafx.components.control.ListWidgetSelector;
import org.phoenicis.javafx.components.control.SearchBox;
import org.phoenicis.javafx.components.control.SidebarGroup;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.common.DelayedFilterTextConsumer;
import org.phoenicis.javafx.views.common.lists.PhoenicisFilteredList;
import org.phoenicis.javafx.views.common.widgets.lists.CombinedListWidget;
import org.phoenicis.javafx.views.mainwindow.ui.Sidebar;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarButton;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarScrollPane;
import org.phoenicis.javafx.views.mainwindow.ui.SidebarSpacer;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutDTO;

import java.io.File;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An instance of this class represents the sidebar of the library tab view.
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
public class LibrarySidebar extends Sidebar {
    // the name of this application
    private final String applicationName;

    private final LibraryFilter filter;

    // the search bar used for filtering
    private SearchBox searchBar;

    // container for the center content of this sidebar
    private SidebarScrollPane centerContent;

    private ObservableList<ShortcutCategoryDTO> shortcutCategories;
    private PhoenicisFilteredList<ShortcutCategoryDTO> filteredShortcutCategories;

    // the toggleable categories
    private LibrarySidebarToggleGroup categoryView;

    // the advanced tools group, containing the run script and run console buttons
    private SidebarGroup<SidebarButton> advancedToolsGroup;

    private SidebarButton runScript;
    private SidebarButton runConsole;

    // widget to switch between the different list widgets in the center view
    private ListWidgetSelector listWidgetSelector;

    // consumers called after a category selection has been made
    private Runnable onAllCategorySelection;
    private Consumer<ShortcutCategoryDTO> onCategorySelection;

    // consumers called when a script should be run or a console be opened
    private Runnable onCreateShortcut;
    private Consumer<File> onScriptRun;
    private Runnable onOpenConsole;

    private final JavaFxSettingsManager javaFxSettingsManager;

    /**
     * Constructor
     *
     * @param applicationName The name of this application (normally "PlayOnLinux")
     * @param availableShortcuts The list widget to be managed by the ListWidgetChooser in the sidebar
     * @param javaFxSettingsManager The settings manager for the JavaFX GUI
     */
    public LibrarySidebar(String applicationName, CombinedListWidget<ShortcutDTO> availableShortcuts,
            LibraryFilter filter,
            JavaFxSettingsManager javaFxSettingsManager) {
        super();

        this.applicationName = applicationName;
        this.filter = filter;
        this.javaFxSettingsManager = javaFxSettingsManager;

        this.populateSearchBar();
        this.populateCategories();
        this.populateAdvancedTools();
        this.populateListWidgetChooser(availableShortcuts);

        this.centerContent = new SidebarScrollPane(this.categoryView, new SidebarSpacer(), this.advancedToolsGroup);

        this.setTop(searchBar);
        this.setCenter(centerContent);
        this.setBottom(listWidgetSelector);
    }

    /**
     * This method binds the given category list <code>categories</code> to the categories toggle group.
     *
     * @param categories The to be bound category list
     */
    public void bindCategories(ObservableList<ShortcutCategoryDTO> categories) {
        Bindings.bindContent(shortcutCategories, categories);
    }

    /**
     * This method populates the searchbar
     */
    private void populateSearchBar() {
        this.searchBar = new SearchBox(new DelayedFilterTextConsumer(this::search), this::clearSearch);
    }

    private void populateCategories() {
        this.shortcutCategories = FXCollections.observableArrayList();
        this.filteredShortcutCategories = new PhoenicisFilteredList<>(this.shortcutCategories, filter::filter);
        this.filter.addOnFilterChanged(filteredShortcutCategories::trigger);

        this.categoryView = new LibrarySidebarToggleGroup(tr("Categories"));

        this.categoryView.setOnAllCategorySelection(() -> onAllCategorySelection.run());
        this.categoryView.setOnCategorySelection(category -> onCategorySelection.accept(category));

        Bindings.bindContent(categoryView.getElements(), filteredShortcutCategories);
    }

    /**
     * This method populates the list widget choose
     *
     * @param availableShortcuts The managed CombinedListWidget
     */
    private void populateListWidgetChooser(CombinedListWidget<ShortcutDTO> availableShortcuts) {
        this.listWidgetSelector = new ListWidgetSelector();
        this.listWidgetSelector.setSelected(this.javaFxSettingsManager.getLibraryListType());
        this.listWidgetSelector.setOnSelect(type -> {
            availableShortcuts.showList(type);

            this.javaFxSettingsManager.setLibraryListType(type);
            this.javaFxSettingsManager.save();
        });
    }

    /**
     * This method populates the advanced tools button group.
     */
    private void populateAdvancedTools() {
        SidebarButton createShortcut = new SidebarButton(tr("Create shortcut"));
        createShortcut.getStyleClass().add("openTerminal");
        createShortcut.setOnMouseClicked(event -> onCreateShortcut.run());

        this.runScript = new SidebarButton(tr("Run a script"));
        this.runScript.getStyleClass().add("scriptButton");
        this.runScript.setOnMouseClicked(event -> {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(tr("Open Script ..."));

            // TODO: use correct owner window
            final File scriptToRun = fileChooser.showOpenDialog(null);

            if (scriptToRun != null) {
                onScriptRun.accept(scriptToRun);
            }
        });

        this.runConsole = new SidebarButton(tr("{0} console", applicationName));
        this.runConsole.getStyleClass().add("consoleButton");
        this.runConsole.setOnMouseClicked(event -> onOpenConsole.run());

        this.advancedToolsGroup = new SidebarGroup<>(tr("Advanced Tools"));
        this.advancedToolsGroup.getComponents().addAll(createShortcut, /* runScript, */runConsole);
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
    public void setOnCategorySelection(Consumer<ShortcutCategoryDTO> onCategorySelection) {
        this.onCategorySelection = onCategorySelection;
    }

    /**
     * This method updates the runnable, that is called when the "Create shortcut" button in the advanced tools section
     * has been clicked.
     *
     * @param onCreateShortcut The new runnable to be called
     */
    public void setOnCreateShortcut(Runnable onCreateShortcut) {
        this.onCreateShortcut = onCreateShortcut;
    }

    /**
     * This method updates the consumer, that is called when the "Run a script" button in the advanced tools section has
     * been clicked.
     *
     * @param onScriptRun The new consumer to be called
     */
    public void setOnScriptRun(Consumer<File> onScriptRun) {
        this.onScriptRun = onScriptRun;
    }

    /**
     * This methdo updates the consumer that is called when the "PlayOnLinux console" button ins the advanced tools
     * section has been clicked.
     *
     * @param onOpenConsole The new consumer to be called
     */
    public void setOnOpenConsole(Runnable onOpenConsole) {
        this.onOpenConsole = onOpenConsole;
    }
}
