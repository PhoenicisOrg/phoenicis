package com.playonlinux.javafx.controller.library;

import com.phoenicis.library.LibraryManager;
import com.phoenicis.library.ShortcutManager;
import com.phoenicis.library.ShortcutRunner;
import com.playonlinux.javafx.controller.library.console.ConsoleController;
import com.playonlinux.javafx.views.common.ConfirmMessage;
import com.playonlinux.javafx.views.mainwindow.library.ViewLibrary;
import javafx.application.Platform;

public class LibraryController {
    private final ViewLibrary viewLibrary;
    private final ConsoleController consoleController;
    private final LibraryManager libraryManager;
    private final ShortcutRunner shortcutRunner;
    private final ShortcutManager shortcutManager;

    public LibraryController(ViewLibrary viewLibrary,
                             ConsoleController consoleController,
                             LibraryManager libraryManager,
                             ShortcutRunner shortcutRunner,
                             ShortcutManager shortcutManager) {
        this.consoleController = consoleController;

        this.viewLibrary = viewLibrary;
        this.libraryManager = libraryManager;
        this.shortcutRunner = shortcutRunner;
        this.shortcutManager = shortcutManager;
        this.viewLibrary.populate(libraryManager.fetchShortcuts());

        libraryManager.setOnUpdate(() -> {
            Platform.runLater(() -> {
                this.viewLibrary.populate(libraryManager.fetchShortcuts());
            });
        });

        this.viewLibrary.setOnShortcutRun(shortcutRunner::run);
        this.viewLibrary.setOnShortcutUninstall(shortcutDTO -> {
            new ConfirmMessage("Uninstall " + shortcutDTO.getName(), "Are you sure you want to uninstall " + shortcutDTO.getName() + "?")
                    .ask(() -> shortcutManager.deleteShortcut(shortcutDTO));
        });

        this.viewLibrary.setOnOpenConsole(() -> {
            viewLibrary.createNewTab(consoleController.createConsole());
        });
    }

    public ViewLibrary getView() {
        return viewLibrary;
    }
}
