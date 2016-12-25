package com.phoenicis.library;

import com.phoenicis.library.dto.ShortcutDTO;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LibraryManager {
    private final String shortcutDirectory;
    private Runnable onUpdate = () -> {};

    public LibraryManager(String shortcutDirectory) {
        this.shortcutDirectory = shortcutDirectory;
    }

    public List<ShortcutDTO> fetchShortcuts() {
        final File shortcutDirectory = new File(this.shortcutDirectory);

        if(!shortcutDirectory.exists()) {
            shortcutDirectory.mkdirs();
            return Collections.emptyList();
        }

        final File[] directoryContent = shortcutDirectory.listFiles();
        if(directoryContent == null) {
            return Collections.emptyList();
        }

        final List<ShortcutDTO> shortcuts = new ArrayList<>();
        for (File file : directoryContent) {
            if("shortcut".equals(FilenameUtils.getExtension(file.getName()))) {
                shortcuts.add(fetchShortcutDTO(shortcutDirectory, file));
            }
        }

        return shortcuts;
    }

    public ShortcutDTO fetchShortcutsFromName(String name) {
        for (ShortcutDTO shortcutDTO : fetchShortcuts()) {
            if(name.equals(shortcutDTO.getName())) {
                return shortcutDTO;
            }
        }

        return null;
    }


    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    private ShortcutDTO fetchShortcutDTO(File shortcutDirectory, File file) {
        final String baseName = FilenameUtils.getBaseName(file.getName());
        final File iconFile = new File(shortcutDirectory, baseName + ".icon");
        final File miniatureFile = new File(shortcutDirectory, baseName + ".miniature");
        final File descriptionFile = new File(shortcutDirectory, baseName + ".description");

        try {
            final byte[] icon = IOUtils.toByteArray(iconFile.exists() ? new FileInputStream(iconFile) : getClass().getResourceAsStream("playonlinux.png"));
            final byte[] miniature = IOUtils.toByteArray(miniatureFile.exists() ? new FileInputStream(miniatureFile) : getClass().getResourceAsStream("defaultMiniature.png"));
            final String description = descriptionFile.exists() ? IOUtils.toString(new FileInputStream(descriptionFile), "UTF-8") : "";
            return new ShortcutDTO.Builder()
                    .withName(baseName)
                    .withScript(IOUtils.toString(new FileInputStream(file), "UTF-8"))
                    .withIcon(icon)
                    .withMiniature(miniature)
                    .withDescription(description)
                    .build();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }


    }

    public void refresh() {
        onUpdate.run();
    }
}
