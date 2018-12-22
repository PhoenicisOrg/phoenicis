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

package org.phoenicis.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.FileUtils;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.library.dto.ShortcutDTO;
import org.phoenicis.library.dto.ShortcutInfoDTO;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.function.Consumer;

@Safe
public class ShortcutManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortcutManager.class);
    private static final String ENCODING = "UTF-8";
    private final String shortcutDirectory;
    private final LibraryManager libraryManager;
    private final ScriptInterpreter scriptInterpreter;
    private ObjectMapper objectMapper;
    private final String desktopShortcutDirectory;

    ShortcutManager(String shortcutDirectory,
            String desktopShortcutDirectory,
            LibraryManager libraryManager,
            ScriptInterpreter scriptInterpreter,
            ObjectMapper objectMapper) {
        this.shortcutDirectory = shortcutDirectory;
        this.desktopShortcutDirectory = desktopShortcutDirectory;
        this.libraryManager = libraryManager;
        this.scriptInterpreter = scriptInterpreter;
        this.objectMapper = objectMapper;
    }

    public void createShortcut(ShortcutDTO shortcutDTO) {
        final ShortcutInfoDTO shortcutInfo = shortcutDTO.getInfo();

        final String baseName = shortcutDTO.getId();
        final File shortcutDirectoryFile = new File(this.shortcutDirectory);

        final File infoFile = new File(shortcutDirectoryFile, baseName + ".info");
        final File scriptFile = new File(shortcutDirectoryFile, baseName + ".shortcut");
        final File iconFile = new File(shortcutDirectoryFile, baseName + ".icon");
        final File miniatureFile = new File(shortcutDirectoryFile, baseName + ".miniature");

        if (!shortcutDirectoryFile.exists()) {
            shortcutDirectoryFile.mkdirs();
        }

        try {
            this.objectMapper.writeValue(infoFile, shortcutInfo);

            FileUtils.writeStringToFile(scriptFile, shortcutDTO.getScript(), ENCODING);

            try {
                if (shortcutDTO.getIcon() != null) {
                    File file = createFileWithFallback(shortcutDTO.getIcon(), "phoenicis.png");
                    if (file.exists()) {
                        FileUtils.copyFile(file, iconFile);
                    }
                }
            } catch (IOException | IllegalArgumentException e) {
                LOGGER.warn("Error while creating shortcut icon", e);
            }
            try {
                if (shortcutDTO.getMiniature() != null) {
                    File file = createFileWithFallback(shortcutDTO.getMiniature(), "defaultMiniature.png");
                    if (file.exists()) {
                        FileUtils.copyFile(file, miniatureFile);
                    }
                }
            } catch (IOException | IllegalArgumentException e) {
                LOGGER.warn("Error while creating miniature", e);
            }
        } catch (IOException e) {
            LOGGER.warn("Error while creating shortcut", e);
        } finally {
            libraryManager.refresh();
        }

        if (this.desktopShortcutDirectory != null) {
            final File desktopShortcutDirectoryFile = new File(this.desktopShortcutDirectory);
            final File desktopShortcutFile = new File(desktopShortcutDirectoryFile, baseName + ".desktop");
            try {
                final String content = "[Desktop Entry]\n" + "Name=" + shortcutInfo.getName() + "\n"
                        + "Type=Application\n" + "Icon=" + miniatureFile.getAbsolutePath() + "\n"
                        + "Exec=phoenicis-cli -run \"" + shortcutInfo.getName() + "\"";
                FileUtils.writeStringToFile(desktopShortcutFile, content, ENCODING);
            } catch (IOException e) {
                LOGGER.warn("Error while creating .desktop", e);
            }
        }
    }

    public void uninstallFromShortcut(ShortcutDTO shortcutDTO, Consumer<Exception> errorCallback) {
        final InteractiveScriptSession interactiveScriptSession = scriptInterpreter.createInteractiveSession();

        interactiveScriptSession.eval("include([\"engines\", \"wine\", \"shortcuts\", \"reader\"]);",
                ignored -> interactiveScriptSession.eval("new ShortcutReader()", output -> {
                    final ScriptObjectMirror shortcutReader = (ScriptObjectMirror) output;
                    shortcutReader.callMember("of", shortcutDTO);
                    shortcutReader.callMember("uninstall");
                }, errorCallback), errorCallback);
    }

    public void deleteShortcut(ShortcutDTO shortcutDTO) {
        final String baseName = shortcutDTO.getId();
        final File shortcutDirectory = new File(this.shortcutDirectory);

        final File infoFile = new File(shortcutDirectory, baseName + ".info");
        final File scriptFile = new File(shortcutDirectory, baseName + ".shortcut");
        final File iconFile = new File(shortcutDirectory, baseName + ".icon");
        final File miniatureFile = new File(shortcutDirectory, baseName + ".miniature");

        if (infoFile.exists()) {
            infoFile.delete();
        }

        if (scriptFile.exists()) {
            scriptFile.delete();
        }

        if (iconFile.exists()) {
            iconFile.delete();
        }

        if (miniatureFile.exists()) {
            miniatureFile.delete();
        }

        if (this.desktopShortcutDirectory != null) {
            final File desktopShortcutDirectoryFile = new File(this.desktopShortcutDirectory);
            final File desktopShortcutFile = new File(desktopShortcutDirectoryFile, baseName + ".desktop");
            if (desktopShortcutFile.exists()) {
                desktopShortcutFile.delete();
            }
        }

        libraryManager.refresh();
    }

    public void updateShortcut(ShortcutDTO shortcutDTO) {
        final String baseName = shortcutDTO.getId();
        final File shortcutDirectory = new File(this.shortcutDirectory);

        // backup icon if it didn't change (deleteShortcut will delete it -> icon lost after shortcut update)
        final File iconFile = new File(shortcutDirectory, baseName + ".icon");
        final File iconBackup = new File(shortcutDirectory, baseName + ".icon_backup");
        final URI shortcutIcon = shortcutDTO.getIcon();

        if (shortcutIcon != null && shortcutIcon.getPath() != null) {
            final boolean keepIcon = shortcutIcon.getPath().equals(iconFile.getPath());
            if (keepIcon) {
                try {
                    Files.move(iconFile.toPath(), iconBackup.toPath());
                    shortcutDTO = new ShortcutDTO.Builder(shortcutDTO).withIcon(iconBackup.toURI()).build();
                } catch (IOException e) {
                    LOGGER.error("Could not backup icon.", e);
                }
            }
        }

        // backup miniature if it didn't change (deleteShortcut will delete it -> miniature lost after shortcut update)
        final File miniatureFile = new File(shortcutDirectory, baseName + ".miniature");
        final File miniatureBackup = new File(shortcutDirectory, baseName + ".miniature_backup");
        final URI shortcutMiniature = shortcutDTO.getMiniature();

        if (shortcutMiniature != null && shortcutMiniature.getPath() != null) {
            final boolean keepMiniature = shortcutMiniature.getPath().equals(miniatureFile.getPath());
            if (keepMiniature) {
                try {
                    Files.move(miniatureFile.toPath(), miniatureBackup.toPath());
                    shortcutDTO = new ShortcutDTO.Builder(shortcutDTO).withMiniature(miniatureBackup.toURI()).build();
                } catch (IOException e) {
                    LOGGER.error("Could not backup miniature.", e);
                }
            }
        }

        deleteShortcut(shortcutDTO);
        createShortcut(shortcutDTO);

        // delete backups
        if (iconBackup.exists()) {
            iconBackup.delete();
        }
        if (miniatureBackup.exists()) {
            miniatureBackup.delete();
        }
    }

    /**
     * creates a file with a resource fallback
     * @param path file path
     * @param resource resource which shall be used if file path cannot be accessed
     * @return created file
     */
    private File createFileWithFallback(URI path, String resource) {
        try {
            return new File(path);
        } catch (IllegalArgumentException e) {
            // fallback
            return new File(getClass().getResource(resource).toExternalForm());
        }
    }
}
