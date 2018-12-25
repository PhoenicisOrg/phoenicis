package org.phoenicis.javafx.views.mainwindow.library;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.widgets.control.ListWidgetSelector;
import org.phoenicis.javafx.components.common.control.SearchBox;
import org.phoenicis.javafx.components.common.control.SidebarGroup;
import org.phoenicis.javafx.components.library.control.LibrarySidebarToggleGroup;
import org.phoenicis.javafx.settings.JavaFxSettingsManager;
import org.phoenicis.javafx.views.mainwindow.ui.Sidebar;
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
    private final JavaFxSettingsManager javaFxSettingsManager;

    private final ObservableList<ShortcutCategoryDTO> shortcutCategories;

    // consumers called when a script should be run or a console be opened
    private Runnable onCreateShortcut;
    private Consumer<File> onScriptRun;
    private Runnable onOpenConsole;

    /**
     * Constructor
     *
     * @param applicationName The name of this application (normally "PlayOnLinux")
     * @param availableShortcuts The list widget to be managed by the ListWidgetChooser in the sidebar
     * @param javaFxSettingsManager The settings manager for the JavaFX GUI
     */
    public LibrarySidebar(String applicationName, LibraryFilter filter, JavaFxSettingsManager javaFxSettingsManager,
            ObservableList<ShortcutCategoryDTO> shortcutCategories,
            CombinedListWidget<ShortcutDTO> availableShortcuts) {
        super();

        this.applicationName = applicationName;
        this.filter = filter;
        this.javaFxSettingsManager = javaFxSettingsManager;
        this.shortcutCategories = shortcutCategories;

        SearchBox searchBox = createSearchBox();
        LibrarySidebarToggleGroup sidebarToggleGroup = createSidebarToggleGroup();
        SidebarGroup<Button> advancedToolsGroup = createAdvancedToolsGroup();
        ListWidgetSelector listWidgetSelector = createListWidgetSelector(availableShortcuts);

        setTop(searchBox);
        setCenter(createScrollPane(sidebarToggleGroup, createSpacer(), advancedToolsGroup));
        setBottom(listWidgetSelector);
    }

    /**
     * This method populates the searchbar
     */
    private SearchBox createSearchBox() {
        SearchBox searchBox = new SearchBox();

        filter.searchTermProperty().bind(searchBox.searchTermProperty());

        return searchBox;
    }

    private LibrarySidebarToggleGroup createSidebarToggleGroup() {
        final FilteredList<ShortcutCategoryDTO> filteredShortcutCategories = shortcutCategories
                .filtered(filter::filter);

        filteredShortcutCategories.predicateProperty().bind(
                Bindings.createObjectBinding(() -> filter::filter, filter.searchTermProperty()));

        final LibrarySidebarToggleGroup categoryView = new LibrarySidebarToggleGroup(tr("Categories"),
                filteredShortcutCategories);

        filter.selectedShortcutCategoryProperty().bind(categoryView.selectedElementProperty());

        return categoryView;
    }

    /**
     * This method populates the list widget choose
     *
     * @param availableShortcuts The managed CombinedListWidget
     */
    private ListWidgetSelector createListWidgetSelector(CombinedListWidget<ShortcutDTO> availableShortcuts) {
        ListWidgetSelector listWidgetSelector = new ListWidgetSelector();

        availableShortcuts.selectedListWidgetProperty().bind(listWidgetSelector.selectedProperty());

        listWidgetSelector.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                javaFxSettingsManager.setLibraryListType(newValue);
                javaFxSettingsManager.save();
            }
        });

        listWidgetSelector.setSelected(javaFxSettingsManager.getLibraryListType());

        return listWidgetSelector;
    }

    /**
     * This method populates the advanced tools button group.
     */
    private SidebarGroup<Button> createAdvancedToolsGroup() {
        final Button createShortcut = new Button(tr("Create shortcut"));
        createShortcut.getStyleClass().addAll("sidebarButton", "openTerminal");
        createShortcut.setOnMouseClicked(event -> onCreateShortcut.run());

        final Button runScript = new Button(tr("Run a script"));
        runScript.getStyleClass().addAll("sidebarButton", "scriptButton");
        runScript.setOnMouseClicked(event -> {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(tr("Open Script ..."));

            // TODO: use correct owner window
            final File scriptToRun = fileChooser.showOpenDialog(null);

            if (scriptToRun != null) {
                onScriptRun.accept(scriptToRun);
            }
        });

        final Button runConsole = new Button(tr("{0} console", applicationName));
        runConsole.getStyleClass().addAll("sidebarButton", "consoleButton");
        runConsole.setOnMouseClicked(event -> onOpenConsole.run());

        SidebarGroup<Button> advancedToolsGroup = new SidebarGroup<>(tr("Advanced Tools"));
        advancedToolsGroup.getComponents().addAll(createShortcut, /* runScript, */runConsole);

        return advancedToolsGroup;
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
