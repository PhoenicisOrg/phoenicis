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
import org.phoenicis.javafx.components.library.skin.LibrarySidebarToggleGroupSkin;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.javafx.themes.ThemeManager;
import org.phoenicis.library.LibraryManager;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class LibraryController {
    private final Logger LOGGER = LoggerFactory.getLogger(LibraryController.class);
    private final LibraryFeaturePanel libraryView;
    private final LibraryManager libraryManager;
    private final ThemeManager themeManager;

    public LibraryController(LibraryFeaturePanel libraryView,
            LibraryManager libraryManager,
            ThemeManager themeManager) {
        super();

        this.libraryView = libraryView;
        this.libraryManager = libraryManager;
        this.themeManager = themeManager;

        this.libraryManager.setOnUpdate(this::populate);
        this.libraryManager.refresh();
    }

    private void populate() {
        final List<ShortcutCategoryDTO> categories = libraryManager.fetchShortcuts();
        setDefaultLibraryCategoryIcons(categories);

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

    /**
     * sets the default category icons for the library sidebar buttons
     *
     * We cannot use the default category buttons for this purpose for several reasons:
     * - It cannot be guaranteed that the repository which contained the category of a certain app does still exist
     * after the app has been installed (e.g. the repository might have been deleted).
     * - It should not be required to fetch the repository to use the library.
     *
     * Therefore, use the following approach:
     * - When installing an app, store the icon for its category together with the shortcut (i.e. in the library).
     * - Use this icon as fallback if the theme does not specify an icon for this category.
     *
     * @param categories list of shortcut categories
     */
    private void setDefaultLibraryCategoryIcons(List<ShortcutCategoryDTO> categories) {
        Platform.runLater(() -> {
            try {
                StringBuilder cssBuilder = new StringBuilder();
                for (ShortcutCategoryDTO category : categories) {
                    cssBuilder.append("#" + LibrarySidebarToggleGroupSkin.getToggleButtonId(category.getId()) + "{\n");
                    URI categoryIcon = category.getIcon();
                    if (categoryIcon == null) {
                        cssBuilder
                                .append("-fx-background-image: url('/org/phoenicis/javafx/views/common/phoenicis.png');\n");
                    } else {
                        cssBuilder.append("-fx-background-image: url('" + categoryIcon + "');\n");
                    }
                    cssBuilder.append("}\n");
                }
                String css = cssBuilder.toString();
                Path temp = Files.createTempFile("defaultLibraryCategoryIcons", ".css").toAbsolutePath();
                File tempFile = temp.toFile();
                tempFile.deleteOnExit();
                Files.write(temp, css.getBytes());
                String defaultLibraryCategoryIconsCss = temp.toUri().toString();
                this.themeManager.setDefaultLibraryCategoryIconsCss(defaultLibraryCategoryIconsCss);
            } catch (IOException e) {
                LOGGER.warn("Could not set default category icons.", e);
            }
        });
    }

    public LibraryFeaturePanel getView() {
        return libraryView;
    }
}
