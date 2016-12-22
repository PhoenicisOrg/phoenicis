package com.playonlinux.javafx.controller.library;

import com.phoenicis.library.LibraryManager;
import com.phoenicis.library.ShortcutRunner;
import com.playonlinux.javafx.controller.library.console.ConsoleController;
import com.playonlinux.javafx.views.mainwindow.library.ViewLibrary;
import javafx.application.Platform;

public class LibraryController {
    private final ViewLibrary viewLibrary;
    private final ConsoleController consoleController;
    private final LibraryManager libraryManager;
    private final ShortcutRunner shortcutRunner;

    public LibraryController(ViewLibrary viewLibrary,
                             ConsoleController consoleController,
                             LibraryManager libraryManager,
                             ShortcutRunner shortcutRunner) {
        this.consoleController = consoleController;

        this.viewLibrary = viewLibrary;
        this.libraryManager = libraryManager;
        this.shortcutRunner = shortcutRunner;
        this.viewLibrary.populate(libraryManager.fetchShortcuts());

        libraryManager.setOnUpdate(() -> {
            Platform.runLater(() -> {
                this.viewLibrary.populate(libraryManager.fetchShortcuts());
            });
        });

        this.viewLibrary.setOnShortcutRun(shortcutRunner::run);

        this.viewLibrary.setOnOpenConsole(() -> {
            viewLibrary.createNewTab(consoleController.createConsole());
        });
    }

    public ViewLibrary getView() {
        return viewLibrary;
    }
}
