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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutDTO;
import org.phoenicis.multithreading.functional.NullRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LibraryManager {
    private final String shortcutDirectory;
    private Runnable onUpdate = new NullRunnable();

    public LibraryManager(String shortcutDirectory) {
        this.shortcutDirectory = shortcutDirectory;
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
        for (String categoryId : categoryMap.keySet()) {
            categoryMap.get(categoryId).sort(ShortcutDTO.nameComparator());
            ShortcutCategoryDTO category = new ShortcutCategoryDTO.Builder().withId(categoryId).withName(categoryId)
                    .withShortcuts(categoryMap.get(categoryId)).build();
            shortcuts.add(category);
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
        final File categoryFile = new File(shortcutDirectory, baseName + ".category");
        final File iconFile = new File(shortcutDirectory, baseName + ".icon");
        final File miniatureFile = new File(shortcutDirectory, baseName + ".miniature");
        final File descriptionFile = new File(shortcutDirectory, baseName + ".description");

        try {
            final URI icon = iconFile.exists() ? iconFile.toURI() : getClass().getResource("phoenicis.png").toURI();
            final URI miniature = miniatureFile.exists() ? miniatureFile.toURI()
                    : getClass().getResource("defaultMiniature.png").toURI();

            String category = "Other";
            if (categoryFile.exists()) {
                category = IOUtils.toString(new FileInputStream(categoryFile), "UTF-8");
                category = category.replace("\n", "");
            }

            final String description = descriptionFile.exists()
                    ? IOUtils.toString(new FileInputStream(descriptionFile), "UTF-8") : "";

            return new ShortcutDTO.Builder().withName(baseName).withCategory(category)
                    .withScript(IOUtils.toString(new FileInputStream(file), "UTF-8")).withIcon(icon)
                    .withMiniature(miniature).withDescription(description).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void refresh() {
        onUpdate.run();
    }
}
