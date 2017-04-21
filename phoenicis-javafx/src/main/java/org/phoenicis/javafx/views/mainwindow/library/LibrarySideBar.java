package org.phoenicis.javafx.views.mainwindow.library;

import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.phoenicis.javafx.views.mainwindow.ui.*;
import org.phoenicis.library.dto.ShortcutDTO;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.translate;

/**
 * Created by marc on 15.04.17.
 */
public class LibrarySideBar extends VBox {
    // the search bar used for filtering
    private SearchBox searchBar;

    // the shortcut group, containing the run, stop and uninstall buttons
    private LeftGroup shortcutGroup;

    private LeftButton runButton;
    private LeftButton stopButton;
    private LeftButton uninstallButton;

    // the advanced tools group, containing the run script and run console buttons
    private LeftGroup advancedToolsGroup;

    private LeftButton runScript;
    private LeftButton runConsole;

    // the name of this application
    private final String applicationName;

    // consumer called after a text has been entered in the search box
    private Consumer<String> onSearch;

    // the current selected shortcut
    private ShortcutDTO shortcut;

    // consumers called when a shortcut should be run, stopped or uninstalled
    private Consumer<ShortcutDTO> onShortcutRun;
    private Consumer<ShortcutDTO> onShortcutStop;
    private Consumer<ShortcutDTO> onShortcutUninstall;

    // consumers called when a script should be run or a console be opened
    private Consumer<File> onScriptRun;
    private Runnable onOpenConsole;

    public LibrarySideBar(String applicationName) {
        super();

        this.applicationName = applicationName;

        this.populateSearchBar();
        this.populateShortcut();
        this.populateAdvancedTools();

        this.hideShortcut();
    }

    public void showShortcut(ShortcutDTO shortcut) {
        this.shortcut = shortcut;

        this.shortcutGroup.setName(shortcut.getName());

        this.getChildren().setAll(this.searchBar, new LeftSpacer(), this.shortcutGroup, new LeftSpacer(), this.advancedToolsGroup);
    }

    public void hideShortcut() {
        this.shortcut = null;

        this.getChildren().setAll(this.searchBar, new LeftSpacer(), this.advancedToolsGroup);
    }

    private void populateSearchBar() {
        this.searchBar = new SearchBox(onSearch, () -> {});
    }

    private void populateShortcut() {
        this.runButton = new LeftButton(translate("Run"));
        this.runButton.getStyleClass().add("runButton");

        this.stopButton = new LeftButton(translate("Close"));
        this.stopButton.getStyleClass().add("stopButton");

        this.uninstallButton = new LeftButton(translate("Uninstall"));
        this.uninstallButton.getStyleClass().add("uninstallButton");

        this.runButton.setOnMouseClicked(event -> onShortcutRun.accept(shortcut));
        this.stopButton.setOnMouseClicked(event -> onShortcutStop.accept(shortcut));
        this.uninstallButton.setOnMouseClicked(event -> {onShortcutUninstall.accept(shortcut); hideShortcut();});

        this.shortcutGroup = new LeftGroup("Unknown application", runButton, stopButton, uninstallButton);
    }

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

    public void setOnSearch(Consumer<String> onSearch) {
        this.onSearch = onSearch;
    }

    public void setOnShortcutRun(Consumer<ShortcutDTO> onShortcutRun) {
        this.onShortcutRun = onShortcutRun;
    }

    public void setOnShortcutStop(Consumer<ShortcutDTO> onShortcutStop) {
        this.onShortcutStop = onShortcutStop;
    }

    public void setOnShortcutUninstall(Consumer<ShortcutDTO> onShortcutUninstall) {
        this.onShortcutUninstall = onShortcutUninstall;
    }

    public void setOnScriptRun(Consumer<File> onScriptRun) {
        this.onScriptRun = onScriptRun;
    }

    public void setOnOpenConsole(Runnable onOpenConsole) {
        this.onOpenConsole = onOpenConsole;
    }
}
