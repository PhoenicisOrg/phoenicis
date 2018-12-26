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

package org.phoenicis.containers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.library.LibraryManager;
import org.phoenicis.library.ShortcutManager;
import org.phoenicis.library.dto.ShortcutCategoryDTO;
import org.phoenicis.library.dto.ShortcutDTO;
import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;
import org.phoenicis.tools.config.CompatibleConfigFileFormatFactory;
import org.phoenicis.tools.config.ConfigFile;
import org.phoenicis.tools.files.FileUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * ContainersManager which is agnostic of a specific engine
 */
@Safe
public class GenericContainersManager implements ContainersManager {
    private final Logger LOGGER = LoggerFactory.getLogger(GenericContainersManager.class);
    @Value("${application.user.containers}")
    private String containersPath;

    private final CompatibleConfigFileFormatFactory compatibleConfigFileFormatFactory;
    private final LibraryManager libraryManager;
    private final ShortcutManager shortcutManager;
    private final FileUtilities fileUtilities;
    private final ScriptInterpreter scriptInterpreter;
    private ObjectMapper objectMapper;

    /**
     * constructor
     * @param compatibleConfigFileFormatFactory
     * @param libraryManager
     * @param shortcutManager
     * @param fileUtilities
     * @param scriptInterpreter
     * @param objectMapper
     */
    public GenericContainersManager(CompatibleConfigFileFormatFactory compatibleConfigFileFormatFactory,
            LibraryManager libraryManager,
            ShortcutManager shortcutManager,
            FileUtilities fileUtilities,
            ScriptInterpreter scriptInterpreter,
            ObjectMapper objectMapper) {
        this.compatibleConfigFileFormatFactory = compatibleConfigFileFormatFactory;
        this.libraryManager = libraryManager;
        this.shortcutManager = shortcutManager;
        this.fileUtilities = fileUtilities;
        this.scriptInterpreter = scriptInterpreter;
        this.objectMapper = objectMapper;
    }

    /**
     * {@inheritDoc}
     * @param callback
     * @param errorCallback
     */
    @Override
    public void fetchContainers(Consumer<List<ContainerCategoryDTO>> callback, Consumer<Exception> errorCallback) {
        final File containersFile = new File(containersPath);
        containersFile.mkdirs();

        final File[] engineDirectories = containersFile.listFiles();

        if (engineDirectories == null) {
            callback.accept(Collections.emptyList());
        } else {
            final List<ContainerCategoryDTO> containerCategories = new ArrayList<>();
            for (File engineDirectory : engineDirectories) {
                final List<ContainerDTO> containers = fetchContainers(engineDirectory);

                if (!CollectionUtils.isEmpty(containers)) {
                    containerCategories.add(new ContainerCategoryDTO.Builder().withName(engineDirectory.getName())
                            .withContainers(containers).build());
                }
            }

            callback.accept(containerCategories);
        }
    }

    /**
     * {@inheritDoc}
     * @param container
     * @param errorCallback
     */
    @Override
    public void deleteContainer(ContainerDTO container, Consumer<Exception> errorCallback) {
        try {
            this.fileUtilities.remove(new File(container.getPath()));
        } catch (IOException e) {
            LOGGER.error("Cannot delete container (" + container.getPath() + ")! Exception: " + e.toString());
            errorCallback.accept(e);
        }
        // TODO: better way to get engine ID
        final String engineId = container.getEngine().toLowerCase();

        List<ShortcutCategoryDTO> categories = this.libraryManager.fetchShortcuts();
        /*
         * categories.stream().flatMap(shortcutCategoryDTO -> shortcutCategoryDTO.getShortcuts().stream())
         * .forEach(shortcutDTO -> {
         * final InteractiveScriptSession interactiveScriptSession = this.scriptInterpreter
         * .createInteractiveSession();
         * interactiveScriptSession.eval(
         * "include([\"engines\", \"" + engineId + "\", \"shortcuts\", \"reader\"]);",
         * ignored -> interactiveScriptSession.eval("new ShortcutReader()", output -> {
         * final ScriptObjectMirror shortcutReader = (ScriptObjectMirror) output;
         * shortcutReader.callMember("of", shortcutDTO);
         * final String containerName = (String) shortcutReader.callMember("container");
         * if (containerName.equals(container.getName())) {
         * this.shortcutManager.deleteShortcut(shortcutDTO);
         * }
         * }, errorCallback), errorCallback);
         * });
         */
    }

    /**
     * fetches all containers in a given directory
     * @param directory
     * @return found containers
     */
    private List<ContainerDTO> fetchContainers(File directory) {
        final List<ContainerDTO> containers = new ArrayList<>();

        final File[] containerDirectories = directory.listFiles();

        if (containerDirectories != null) {
            for (File containerDirectory : containerDirectories) {
                final ConfigFile configFile = compatibleConfigFileFormatFactory
                        .open(new File(containerDirectory, "phoenicis.cfg"));
                final String containerPath = containerDirectory.getAbsolutePath();
                final String containerName = containerPath.substring(containerPath.lastIndexOf('/') + 1);

                // find shortcuts which use this container
                List<ShortcutDTO> shortcutDTOS = libraryManager.fetchShortcuts().stream()
                        .flatMap(shortcutCategory -> shortcutCategory.getShortcuts().stream()).filter(shortcut -> {
                            boolean toAdd = false;
                            try {
                                final Map<String, Object> shortcutProperties = objectMapper
                                        .readValue(shortcut.getScript(), new TypeReference<Map<String, Object>>() {
                                        });
                                toAdd = shortcutProperties.get("winePrefix").equals(containerName);
                            } catch (IOException e) {
                                LOGGER.warn("Could not parse shortcut script JSON", e);
                            }
                            return toAdd;
                        }).collect(Collectors.toList());

                if (directory.getName().equals("wineprefix")) {
                    containers.add(new WinePrefixContainerDTO.Builder().withName(containerDirectory.getName())
                            .withPath(containerPath).withInstalledShortcuts(shortcutDTOS)
                            .withArchitecture(configFile.readValue("wineArchitecture", ""))
                            .withDistribution(configFile.readValue("wineDistribution", ""))
                            .withVersion(configFile.readValue("wineVersion", ""))
                            .build());
                }
            }
            containers.sort(ContainerDTO.nameComparator());
        }
        return containers;
    }

}
