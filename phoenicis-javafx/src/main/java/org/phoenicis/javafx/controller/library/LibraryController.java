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
import org.phoenicis.javafx.dialogs.ConfirmDialog;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.javafx.views.common.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.library.LibraryView;
import org.phoenicis.library.LibraryManager;
import org.phoenicis.library.ShortcutManager;
import org.phoenicis.library.ShortcutRunner;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutCreationDTO;
import org.phoenicis.library.dto.ShortcutDTO;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class LibraryController {
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LibraryController.class);
    @Value("${application.user.containers}")
    private String containersPath;

    private final LibraryView libraryView;
    private final LibraryManager libraryManager;
    private final ShortcutRunner shortcutRunner;
    private final ShortcutManager shortcutManager;
    private final ScriptInterpreter scriptInterpreter;
    private final RepositoryManager repositoryManager;
    private final ThemeManager themeManager;

    private boolean firstViewSelection = true;

    public LibraryController(LibraryView libraryView, ConsoleController consoleController,
            LibraryManager libraryManager, ShortcutRunner shortcutRunner, ShortcutManager shortcutManager,
            ScriptInterpreter scriptInterpreter, RepositoryManager repositoryManager, ThemeManager themeManager) {
        this.libraryView = libraryView;
        this.libraryManager = libraryManager;
        this.shortcutRunner = shortcutRunner;
        this.shortcutManager = shortcutManager;
        this.scriptInterpreter = scriptInterpreter;
        this.repositoryManager = repositoryManager;
        this.themeManager = themeManager;

        libraryManager.setOnUpdate(this::updateLibrary);

        this.libraryView.setOnShortcutCreate(this::createShortcut);
        this.libraryView.setOnShortcutRun(this::runShortcut);
        this.libraryView.setOnShortcutDoubleClicked(this::runShortcut);
        this.libraryView.setOnShortcutStop(
                shortcutDTO -> shortcutRunner.stop(shortcutDTO, e -> {
                    final ErrorDialog errorDialog = ErrorDialog.builder()
                            .withMessage(tr("Error"))
                            .withException(e)
                            .withOwner(this.libraryView.getContent().getScene().getWindow())
                            .build();

                    errorDialog.showAndWait();
                }));

        this.libraryView.setOnShortcutUninstall(shortcutDTO -> {
            final String shortcutName = shortcutDTO.getInfo().getName();

            final ConfirmDialog confirmMessage = ConfirmDialog.builder()
                    .withTitle(tr("Uninstall {0}", shortcutName))
                    .withMessage(tr("Are you sure you want to uninstall {0}?", shortcutName))
                    .withOwner(libraryView.getContent().getScene().getWindow())
                    .withResizable(true)
                    .withYesCallback(() -> shortcutManager.uninstallFromShortcut(shortcutDTO, e -> {
                        final ErrorDialog errorDialog = ErrorDialog.builder()
                                .withMessage(tr("Error while uninstalling {0}", shortcutName))
                                .withException(e)
                                .withOwner(this.libraryView.getContent().getScene().getWindow())
                                .build();

                        errorDialog.showAndWait();
                    }))
                    .build();

            confirmMessage.showAndCallback();
        });

        this.libraryView.setOnShortcutChanged(shortcutDTO -> this.shortcutManager.updateShortcut(shortcutDTO));

        this.libraryView.setOnOpenConsole(() -> {
            libraryView.createNewTab(consoleController.createConsole());
        });

        this.libraryView.setOnScriptRun(file -> {
            scriptInterpreter.runScript(file, e -> Platform.runLater(() -> {
                final ErrorDialog errorDialog = ErrorDialog.builder()
                        .withMessage(tr("Error while running script"))
                        .withException(e)
                        .withOwner(this.libraryView.getContent().getScene().getWindow())
                        .build();

                errorDialog.showAndWait();
            }));
        });

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
     * creates a new shortcut
     *
     * @param shortcutCreationDTO DTO describing the new shortcut
     */
    private void createShortcut(ShortcutCreationDTO shortcutCreationDTO) {
        // get container
        // TODO: smarter way using container manager
        final String executablePath = shortcutCreationDTO.getExecutable().getAbsolutePath();
        final String pathInContainers = executablePath.replace(containersPath, "");
        final String[] split = pathInContainers.split("/");
        final String engineContainer = split[0];
        final String engine = (Character.toUpperCase(engineContainer.charAt(0)) + engineContainer.substring(1))
                .replace("prefix", "");
        // TODO: better way to get engine ID
        final String engineId = engine.toLowerCase();
        final String container = split[1];

        /*
         * final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();
         * 
         * interactiveScriptSession.eval(
         * "include([\"engines\", \"" + engineId + "\", \"shortcuts\", \"" + engineId + "\"]);",
         * ignored -> interactiveScriptSession.eval("new " + engine + "Shortcut()", output -> {
         * final ScriptObjectMirror shortcutObject = (ScriptObjectMirror) output;
         * shortcutObject.callMember("name", shortcutCreationDTO.getName());
         * shortcutObject.callMember("category", shortcutCreationDTO.getCategory());
         * shortcutObject.callMember("description", shortcutCreationDTO.getDescription());
         * shortcutObject.callMember("miniature", shortcutCreationDTO.getMiniature());
         * shortcutObject.callMember("search", shortcutCreationDTO.getExecutable().getName());
         * shortcutObject.callMember("prefix", container);
         * shortcutObject.callMember("create");
         * }, e -> this.showErrorMessage(e, tr("Error while creating shortcut"))),
         * e -> this.showErrorMessage(e, tr("Error while creating shortcut")));
         */
    }

    private void runShortcut(ShortcutDTO shortcutDTO) {
        shortcutRunner.run(shortcutDTO, Collections.emptyList(), e -> Platform.runLater(() -> {
            final ErrorDialog errorDialog = ErrorDialog.builder()
                    .withMessage(tr("Error"))
                    .withException(e)
                    .withOwner(this.libraryView.getContent().getScene().getWindow())
                    .build();

            errorDialog.showAndWait();
        }));
    }

    /**
     * shows an error message
     *
     * @param e exception that caused the error
     * @param message error message
     */
    private void showErrorMessage(Exception e, String message) {
        Platform.runLater(() -> {
            final ErrorDialog errorDialog = ErrorDialog.builder()
                    .withMessage(message)
                    .withException(e)
                    .withOwner(this.libraryView.getContent().getScene().getWindow())
                    .build();

            errorDialog.showAndWait();
        });
    }

    public void setOnTabOpened(Runnable onTabOpened) {
        this.libraryView.setOnTabOpened(onTabOpened);
    }

    /**
     * update library with translations from repository
     *
     * @param repositoryDTO
     */
    public void updateLibrary(RepositoryDTO repositoryDTO) {
        Platform.runLater(() -> {
            List<CategoryDTO> categoryDTOS = repositoryDTO.getTypes().get(0).getCategories();
            setDefaultCategoryIcons(categoryDTOS);
        });

        this.updateLibrary();
    }

    public void updateLibrary() {
        final List<ShortcutCategoryDTO> categories = libraryManager.fetchShortcuts();

        Platform.runLater(() -> this.libraryView.populate(categories));
    }

    public LibraryView getView() {
        return libraryView;
    }

    private void setDefaultCategoryIcons(List<CategoryDTO> categoryDTOS) {
        try {
            StringBuilder cssBuilder = new StringBuilder();
            for (CategoryDTO category : categoryDTOS) {
                cssBuilder.append("#" + category.getId().toLowerCase() + "Button{\n");
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
            Path temp = Files.createTempFile("defaultCategoryIcons", ".css").toAbsolutePath();
            File tempFile = temp.toFile();
            tempFile.deleteOnExit();
            Files.write(temp, css.getBytes());
            String defaultCategoryIconsCss = temp.toUri().toString();
            themeManager.setDefaultCategoryIconsCss(defaultCategoryIconsCss);
        } catch (IOException e) {
            LOGGER.warn("Could not set default category icons.", e);
        }
    }
}
