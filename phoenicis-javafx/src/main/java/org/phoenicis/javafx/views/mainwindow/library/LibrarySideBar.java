package org.phoenicis.javafx.views.mainwindow.library;

import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.phoenicis.javafx.views.mainwindow.MainWindowView;
import org.phoenicis.javafx.views.mainwindow.ui.*;
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
    public LibrarySideBar(String applicationName, MainWindowView<LibrarySideBar> mainWindow) {
        super(mainWindow);

        this.applicationName = applicationName;

        this.populateSearchBar();
        this.populateShortcut();
        this.populateAdvancedTools();

        this.hideShortcut();
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

        this.getChildren().setAll(this.searchBar, new LeftSpacer(), this.shortcutGroup, new LeftSpacer(), this.advancedToolsGroup);
    }

    /**
     * This method hides the currently shown shortcut.
     * If no shortcut is currently shown, this method does nothing.
     */
    public void hideShortcut() {
        this.shortcut = null;

        this.getChildren().setAll(this.searchBar, new LeftSpacer(), this.advancedToolsGroup);
    }

    /**
     * This method populates the searchbar
     */
    private void populateSearchBar() {
        this.searchBar = new SearchBox(onSearch, () -> {
        });
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
