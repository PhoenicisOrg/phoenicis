package com.playonlinux.javafx.controller.library;

import com.playonlinux.javafx.controller.library.console.ConsoleController;
import com.playonlinux.javafx.views.mainwindow.library.ViewLibrary;

public class LibraryController {
    private final ViewLibrary viewLibrary;
    private final ConsoleController consoleController;

    public LibraryController(ViewLibrary viewLibrary,
                             ConsoleController consoleController) {
        this.viewLibrary = viewLibrary;
        this.consoleController = consoleController;

        this.viewLibrary.setOnOpenConsole(() -> {
            viewLibrary.createNewTab(consoleController.createConsole());
        });
    }

    public ViewLibrary getView() {
        return viewLibrary;
    }
}
