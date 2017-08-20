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

package org.phoenicis.containers.wine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.containers.ContainersManager;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.configurations.WinePrefixContainerDisplayConfiguration;
import org.phoenicis.containers.wine.configurations.WinePrefixContainerInputConfiguration;
import org.phoenicis.library.LibraryManager;
import org.phoenicis.library.dto.ShortcutDTO;
import org.phoenicis.tools.config.CompatibleConfigFileFormatFactory;
import org.phoenicis.tools.config.ConfigFile;
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

@Safe
public class GenericContainersManager implements ContainersManager {
    private final Logger LOGGER = LoggerFactory.getLogger(GenericContainersManager.class);
    @Value("${application.user.containers}")
    private String containersPath;

    private final CompatibleConfigFileFormatFactory compatibleConfigFileFormatFactory;
    private final WinePrefixContainerDisplayConfiguration winePrefixContainerDisplayConfiguration;
    private final WinePrefixContainerInputConfiguration winePrefixContainerInputConfiguration;
    private final LibraryManager libraryManager;
    private ObjectMapper objectMapper;

    public GenericContainersManager(CompatibleConfigFileFormatFactory compatibleConfigFileFormatFactory,
            WinePrefixContainerDisplayConfiguration winePrefixContainerDisplayConfiguration,
            WinePrefixContainerInputConfiguration winePrefixContainerInputConfiguration, LibraryManager libraryManager,
            ObjectMapper objectMapper) {
        this.compatibleConfigFileFormatFactory = compatibleConfigFileFormatFactory;
        this.winePrefixContainerDisplayConfiguration = winePrefixContainerDisplayConfiguration;
        this.winePrefixContainerInputConfiguration = winePrefixContainerInputConfiguration;
        this.libraryManager = libraryManager;
        this.objectMapper = objectMapper;
    }

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
                final File userRegistryFile = new File(containerDirectory, "user.reg");
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
                            .withGlslValue(winePrefixContainerDisplayConfiguration.getGLSL(userRegistryFile))
                            .withDirectDrawRenderer(
                                    winePrefixContainerDisplayConfiguration.getDirectDrawRenderer(userRegistryFile))
                            .withVideoMemorySize(
                                    winePrefixContainerDisplayConfiguration.getVideoMemorySize(userRegistryFile))
                            .withOffscreenRenderingMode(
                                    winePrefixContainerDisplayConfiguration.getOffscreenRenderingMode(userRegistryFile))
                            .withMultisampling(
                                    winePrefixContainerDisplayConfiguration.getMultisampling(userRegistryFile))
                            .withAlwaysOffscreen(
                                    winePrefixContainerDisplayConfiguration.getAlwaysOffscreen(userRegistryFile))
                            .withStrictDrawOrdering(
                                    winePrefixContainerDisplayConfiguration.getStrictDrawOrdering(userRegistryFile))
                            .withRenderTargetModeLock(
                                    winePrefixContainerDisplayConfiguration.getRenderTargetModeLock(userRegistryFile))
                            .withMouseWarpOverride(
                                    winePrefixContainerInputConfiguration.getMouseWarpOverride(userRegistryFile))
                            .build());
                }
            }
            containers.sort(ContainerDTO.nameComparator());
        }
        return containers;
    }

}