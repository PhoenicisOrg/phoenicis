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
import org.phoenicis.javafx.components.library.control.LibraryFeaturePanel;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.library.LibraryManager;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.repository.RepositoryManager;

import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class LibraryController {
    private final LibraryFeaturePanel libraryView;
    private final LibraryManager libraryManager;

    public LibraryController(LibraryFeaturePanel libraryView, LibraryManager libraryManager,
            RepositoryManager repositoryManager) {
        super();

        this.libraryView = libraryView;
        this.libraryManager = libraryManager;

        this.libraryManager.setOnUpdate(this::populate);

        repositoryManager.addCallbacks(repository -> this.populate(), this::showError);
        repositoryManager.triggerCallbacks();
    }

    private void populate() {
        final List<ShortcutCategoryDTO> categories = libraryManager.fetchShortcuts();

        Platform.runLater(() -> {
            getView().getCategories().setAll(categories);

            getView().setInitialized(true);
        });
    }

    private void showError(Exception e) {
        Platform.runLater(() -> {
            final ErrorDialog errorDialog = ErrorDialog.builder()
                    .withOwner(getView().getScene().getWindow())
                    .withException(e)
                    .withMessage(tr("Unable to load library, please try again."))
                    .build();

            errorDialog.showAndWait();
        });
    }

    public LibraryFeaturePanel getView() {
        return libraryView;
    }
}
