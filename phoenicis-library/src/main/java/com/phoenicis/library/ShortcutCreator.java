package com.phoenicis.library;

import com.phoenicis.library.dto.ShortcutDTO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ShortcutCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortcutCreator.class);
    private static final String ENCODING = "UTF-8";
    private final String shortcutDirectory;
    private final LibraryManager libraryManager;

    public ShortcutCreator(String shortcutDirectory,
                           LibraryManager libraryManager) {
        this.shortcutDirectory = shortcutDirectory;
        this.libraryManager = libraryManager;
    }

    public void createShortcut(ShortcutDTO shortcutDTO) {
        final String baseName = shortcutDTO.getName();
        final File shortcutDirectory = new File(this.shortcutDirectory);

        final File scriptFile = new File(shortcutDirectory, baseName + ".shortcut");
        final File iconFile = new File(shortcutDirectory, baseName + ".icon");
        final File miniatureFile = new File(shortcutDirectory, baseName + ".miniature");
        final File descriptionFile = new File(shortcutDirectory, baseName + ".description");


        if(!shortcutDirectory.exists()) {
            shortcutDirectory.mkdirs();
        }

        try {
            FileUtils.writeStringToFile(scriptFile, shortcutDTO.getScript(), ENCODING);
            if (shortcutDTO.getDescription() != null) {
                FileUtils.writeStringToFile(descriptionFile, shortcutDTO.getDescription(), ENCODING);
            }
            if (shortcutDTO.getIcon() != null) {
                FileUtils.writeByteArrayToFile(iconFile, shortcutDTO.getIcon());
            }
            if (shortcutDTO.getMiniature() != null) {
                FileUtils.writeByteArrayToFile(miniatureFile, shortcutDTO.getMiniature());
            }
        } catch(IOException e) {
            LOGGER.warn("Error while creating shortcut", e);
        } finally {
            libraryManager.refresh();
        }

    }
}
