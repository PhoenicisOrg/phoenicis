package com.playonlinux.javafx.controller.library;

import com.phoenicis.library.LibraryManager;
import com.phoenicis.library.ShortcutManager;
import com.phoenicis.library.ShortcutRunner;
import com.phoenicis.library.dto.ShortcutDTO;
import com.playonlinux.javafx.controller.library.console.ConsoleController;
import com.playonlinux.javafx.views.common.ConfirmMessage;
import com.playonlinux.javafx.views.common.ErrorMessage;
import com.playonlinux.javafx.views.mainwindow.library.ViewLibrary;
import javafx.application.Platform;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryController {
    private final ViewLibrary viewLibrary;
    private final ConsoleController consoleController;
    private final LibraryManager libraryManager;
    private final ShortcutRunner shortcutRunner;
    private final ShortcutManager shortcutManager;
    private String keywords = "";

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

        libraryManager.setOnUpdate(this::updateLibrary);

        this.viewLibrary.setOnSearch(searchKeyword -> {
            keywords = searchKeyword;
            this.updateLibrary();
        });

        this.viewLibrary.setOnShortcutRun(shortcutDTO -> shortcutRunner.run(shortcutDTO, Collections.emptyList(), e -> new ErrorMessage("Error", e)));
        this.viewLibrary.setOnShortcutStop(shortcutDTO -> shortcutRunner.stop(shortcutDTO, e -> new ErrorMessage("Error", e)));

        this.viewLibrary.setOnShortcutUninstall(shortcutDTO -> {
            new ConfirmMessage("Uninstall " + shortcutDTO.getName(), "Are you sure you want to uninstall " + shortcutDTO.getName() + "?")
                    .ask(() -> shortcutManager.deleteShortcut(shortcutDTO));
        });

        this.viewLibrary.setOnOpenConsole(() -> {
            viewLibrary.createNewTab(consoleController.createConsole());
        });
    }

    private void updateLibrary() {
        final List<ShortcutDTO> shortcuts = libraryManager.fetchShortcuts();
        final List<ShortcutDTO> shortcutsCorrespondingToKeywords =
                shortcuts.stream()
                        .filter(shortcutDTO ->
                                shortcutDTO
                                        .getName()
                                        .toLowerCase()
                                        .contains(keywords.toLowerCase().trim())
                        ).collect(Collectors.toList());


        Platform.runLater(() -> {
            this.viewLibrary.populate(shortcutsCorrespondingToKeywords);
        });
    }

    public ViewLibrary getView() {
        return viewLibrary;
    }
}
