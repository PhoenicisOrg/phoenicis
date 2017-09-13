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
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutDTO;
import org.phoenicis.multithreading.functional.NullRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.phoenicis.configuration.localisation.Localisation.tr;

@Safe
public class LibraryManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(LibraryManager.class);

    private final String shortcutDirectory;
    private ObjectMapper objectMapper;
    private Runnable onUpdate = new NullRunnable();

    public LibraryManager(String shortcutDirectory, ObjectMapper objectMapper) {
        this.shortcutDirectory = shortcutDirectory;
        this.objectMapper = objectMapper;
    }

    public List<ShortcutCategoryDTO> fetchShortcuts() {
        final File shortcutDirectoryFile = new File(this.shortcutDirectory);

        if (!shortcutDirectoryFile.exists()) {
            shortcutDirectoryFile.mkdirs();
            return Collections.emptyList();
        }

        final File[] directoryContent = shortcutDirectoryFile.listFiles();
        if (directoryContent == null) {
            return Collections.emptyList();
        }

        HashMap<String, List<ShortcutDTO>> categoryMap = new HashMap<>();
        for (File file : directoryContent) {
            if ("shortcut".equals(FilenameUtils.getExtension(file.getName()))) {
                ShortcutDTO shortcut = fetchShortcutDTO(shortcutDirectoryFile, file);
                String categoryId = shortcut.getCategory();
                if (!categoryMap.containsKey(categoryId)) {
                    categoryMap.put(categoryId, new ArrayList<>());
                }
                categoryMap.get(categoryId).add(shortcut);
            }
        }

        List<ShortcutCategoryDTO> shortcuts = new ArrayList<>();
        for (Map.Entry<String, List<ShortcutDTO>> entry : categoryMap.entrySet()) {
            entry.getValue().sort(ShortcutDTO.nameComparator());
            ShortcutCategoryDTO category = new ShortcutCategoryDTO.Builder().withId(entry.getKey())
                    .withName(entry.getKey()).withShortcuts(entry.getValue()).build();
            shortcuts.add(tr(category));
        }

        return shortcuts;
    }

    public ShortcutDTO fetchShortcutsFromName(String name) {
        for (ShortcutCategoryDTO shortcutCategoryDTO : fetchShortcuts()) {
            for (ShortcutDTO shortcutDTO : shortcutCategoryDTO.getShortcuts()) {
                if (name.equals(shortcutDTO.getName())) {
                    return shortcutDTO;
                }
            }
        }

        return null;
    }

    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    private ShortcutDTO fetchShortcutDTO(File shortcutDirectory, File file) {
        final String baseName = FilenameUtils.getBaseName(file.getName());
        final File infoFile = new File(shortcutDirectory, baseName + ".info");
        final File iconFile = new File(shortcutDirectory, baseName + ".icon");
        final File miniatureFile = new File(shortcutDirectory, baseName + ".miniature");

        final ShortcutDTO.Builder shortcutDTOBuilder;
        if (infoFile.exists()) {
            final ShortcutDTO shortcutDTOFromJsonFile = unSerializeShortcut(infoFile);
            shortcutDTOBuilder = new ShortcutDTO.Builder(shortcutDTOFromJsonFile);

        } else {
            shortcutDTOBuilder = new ShortcutDTO.Builder();
            shortcutDTOBuilder.withName(baseName);
        }

        if (StringUtils.isBlank(shortcutDTOBuilder.getName())) {
            shortcutDTOBuilder.withName(baseName);
        }

        if (StringUtils.isBlank(shortcutDTOBuilder.getCategory())) {
            shortcutDTOBuilder.withCategory("Other");
        }

        try {
            final URI icon = iconFile.exists() ? iconFile.toURI() : getClass().getResource("phoenicis.png").toURI();
            final URI miniature = miniatureFile.exists() ? miniatureFile.toURI()
                    : getClass().getResource("defaultMiniature.png").toURI();

            return shortcutDTOBuilder
                    .withId(baseName)
                    .withScript(IOUtils.toString(new FileInputStream(file), "UTF-8"))
                    .withIcon(icon)
                    .withMiniature(miniature)
                    .build();
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void refresh() {
        onUpdate.run();
    }

    private ShortcutDTO unSerializeShortcut(File jsonFile) {
        try {
            return this.objectMapper.readValue(jsonFile, ShortcutDTO.class);
        } catch (IOException e) {
            LOGGER.debug("JSON file not found");
            return new ShortcutDTO.Builder().build();
        }
    }
}
