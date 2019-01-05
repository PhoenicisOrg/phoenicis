/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.javafx.controller.library;

import javafx.application.Platform;
import org.phoenicis.javafx.controller.library.console.ConsoleController;
import org.phoenicis.javafx.views.mainwindow.library.LibraryView;
import org.phoenicis.library.LibraryManager;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.RepositoryDTO;

import java.util.List;

public class LibraryController {
    private final LibraryView libraryView;
    private final LibraryManager libraryManager;
    private final RepositoryManager repositoryManager;

    private boolean firstViewSelection = true;

    public LibraryController(LibraryView libraryView, ConsoleController consoleController,
            LibraryManager libraryManager, RepositoryManager repositoryManager) {
        super();

        this.libraryView = libraryView;
        this.libraryManager = libraryManager;
        this.repositoryManager = repositoryManager;

        this.libraryManager.setOnUpdate(this::updateLibrary);

        this.libraryView.setOnOpenConsole(() -> libraryView.createNewTab(consoleController.createConsole()));

        this.libraryView.setOnSelectionChanged(event -> {
            if (this.libraryView.isSelected() && this.firstViewSelection) {
                this.repositoryManager.addCallbacks(this::updateLibrary, e -> {
                });
                this.repositoryManager.triggerCallbacks();
                this.firstViewSelection = false;
            }
        });
    }

    /**
     * update library with translations from repository
     *
     * @param repositoryDTO
     */
    public void updateLibrary(RepositoryDTO repositoryDTO) {
        this.updateLibrary();
    }

    public void updateLibrary() {
        final List<ShortcutCategoryDTO> categories = libraryManager.fetchShortcuts();

        Platform.runLater(() -> this.libraryView.populate(categories));
    }

    public LibraryView getView() {
        return libraryView;
    }
}
