package com.playonlinux.javafx.controller.library;

import com.phoenicis.library.LibraryManager;
import com.playonlinux.javafx.controller.library.console.ConsoleController;
import com.playonlinux.javafx.views.mainwindow.library.ViewLibrary;
import javafx.application.Platform;

public class LibraryController {
    private final ViewLibrary viewLibrary;
    private final ConsoleController consoleController;
    private final LibraryManager libraryManager;

    public LibraryController(ViewLibrary viewLibrary,
                             ConsoleController consoleController, LibraryManager libraryManager) {
        this.consoleController = consoleController;

        this.viewLibrary = viewLibrary;
        this.libraryManager = libraryManager;
        this.viewLibrary.populate(libraryManager.fetchShortcuts());

        libraryManager.setOnUpdate(() -> {
            Platform.runLater(() -> {
                this.viewLibrary.populate(libraryManager.fetchShortcuts());
            });
        });

        this.viewLibrary.setOnOpenConsole(() -> {
            viewLibrary.createNewTab(consoleController.createConsole());
        });
    }

    public ViewLibrary getView() {
        return viewLibrary;
    }
}
