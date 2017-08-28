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
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.phoenicis.javafx.controller.library.console.ConsoleController;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.mainwindow.library.ViewLibrary;
import org.phoenicis.library.LibraryManager;
import org.phoenicis.library.ShortcutManager;
import org.phoenicis.library.ShortcutRunner;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutCreationDTO;
import org.phoenicis.library.dto.ShortcutDTO;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class LibraryController {
    @Value("${application.user.containers}")
    private String containersPath;

    private final ViewLibrary viewLibrary;
    private final ConsoleController consoleController;
    private final LibraryManager libraryManager;
    private final ShortcutRunner shortcutRunner;
    private final ShortcutManager shortcutManager;
    private final ScriptInterpreter scriptInterpreter;
    private final RepositoryManager repositoryManager;

    private String keywords = "";

    public LibraryController(ViewLibrary viewLibrary, ConsoleController consoleController,
            LibraryManager libraryManager, ShortcutRunner shortcutRunner, ShortcutManager shortcutManager,
            ScriptInterpreter scriptInterpreter, RepositoryManager repositoryManager) {
        this.consoleController = consoleController;

        this.viewLibrary = viewLibrary;
        this.libraryManager = libraryManager;
        this.shortcutRunner = shortcutRunner;
        this.shortcutManager = shortcutManager;
        this.scriptInterpreter = scriptInterpreter;

        this.repositoryManager = repositoryManager;
        this.repositoryManager.addCallbacks(this::updateLibrary, e -> {
        });

        libraryManager.setOnUpdate(this::updateLibrary);

        this.viewLibrary.setOnSearch(searchKeyword -> {
            keywords = searchKeyword;
            this.updateLibrary();
        });

        this.viewLibrary.setOnShortcutCreate(this::createShortcut);

        this.viewLibrary.setOnShortcutRun(this::runShortcut);
        this.viewLibrary.setOnShortcutDoubleClicked(this::runShortcut);
        this.viewLibrary.setOnShortcutStop(
                shortcutDTO -> shortcutRunner.stop(shortcutDTO, e -> new ErrorMessage(tr("Error"), e)));

        this.viewLibrary.setOnShortcutUninstall(shortcutDTO -> {
            new ConfirmMessage(tr("Uninstall {0}", shortcutDTO.getName()),
                    tr("Are you sure you want to uninstall {0}?", shortcutDTO.getName()))
                            .ask(() -> shortcutManager.uninstallFromShortcut(shortcutDTO,
                                    e -> new ErrorMessage("Error while uninstalling " + shortcutDTO.getName(), e)));
        });

        this.viewLibrary.setOnShortcutChanged(shortcutDTO -> this.shortcutManager.updateShortcut(shortcutDTO));

        this.viewLibrary.setOnOpenConsole(() -> {
            viewLibrary.createNewTab(consoleController.createConsole());
        });

        this.viewLibrary.setOnScriptRun(file -> {
            scriptInterpreter.runScript(file,
                    e -> Platform.runLater(() -> new ErrorMessage(tr("Error while running script"), e)));
        });
    }

    /**
     * creates a new shortcut
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
        final String container = split[1];

        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"Engines\", \"" + engine + "\", \"Shortcuts\", \"" + engine + "\"]);",
                ignored -> interactiveScriptSession.eval("new " + engine + "Shortcut()", output -> {
                    final ScriptObjectMirror shortcutObject = (ScriptObjectMirror) output;
                    shortcutObject.callMember("name", shortcutCreationDTO.getName());
                    shortcutObject.callMember("category", shortcutCreationDTO.getCategory());
                    shortcutObject.callMember("description", shortcutCreationDTO.getDescription());
                    ;
                    shortcutObject.callMember("miniature", shortcutCreationDTO.getMiniature());
                    shortcutObject.callMember("search", shortcutCreationDTO.getExecutable().getName());
                    shortcutObject.callMember("prefix", container);
                    shortcutObject.callMember("create");
                }, e -> this.showErrorMessage(e, tr("Error while creating shortcut"))),
                e -> this.showErrorMessage(e, tr("Error while creating shortcut")));
    }

    private void runShortcut(ShortcutDTO shortcutDTO) {
        shortcutRunner.run(shortcutDTO, Collections.emptyList(), e -> new ErrorMessage(tr("Error"), e));
    }

    /**
     * shows an error message
     * @param e exception that caused the error
     * @param message error message
     */
    private void showErrorMessage(Exception e, String message) {
        Platform.runLater(() -> new ErrorMessage(message, e));
    }

    public void setOnTabOpened(Runnable onTabOpened) {
        this.viewLibrary.setOnTabOpened(onTabOpened);
    }

    /**
     * update library with translations from repository
     * @param repositoryDTO
     */
    public void updateLibrary(RepositoryDTO repositoryDTO) {
        this.updateLibrary();
    }

    public void updateLibrary() {
        final List<ShortcutCategoryDTO> categories = libraryManager.fetchShortcuts();
        final List<ShortcutCategoryDTO> shortcutsCorrespondingToKeywords = categories.stream()
                .filter(shortcutDTO -> shortcutDTO.getName().toLowerCase().contains(keywords.toLowerCase().trim()))
                .collect(Collectors.toList());

        Platform.runLater(() -> this.viewLibrary.populate(shortcutsCorrespondingToKeywords));
    }

    public ViewLibrary getView() {
        return viewLibrary;
    }
}
