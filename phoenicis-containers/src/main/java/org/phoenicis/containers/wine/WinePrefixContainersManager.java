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

import org.phoenicis.containers.ContainersManager;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.wine.configurations.WinePrefixContainerDisplayConfiguration;
import org.phoenicis.containers.wine.configurations.WinePrefixContainerInputConfiguration;
import org.phoenicis.tools.config.CompatibleConfigFileFormatFactory;
import org.phoenicis.tools.config.ConfigFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class WinePrefixContainersManager implements ContainersManager {
    @Value("${application.user.wineprefix}")
    private String winePrefixPath;

    private final CompatibleConfigFileFormatFactory compatibleConfigFileFormatFactory;
    private final WinePrefixContainerDisplayConfiguration winePrefixContainerDisplayConfiguration;
    private final WinePrefixContainerInputConfiguration winePrefixContainerInputConfiguration;

    public WinePrefixContainersManager(CompatibleConfigFileFormatFactory compatibleConfigFileFormatFactory,
            WinePrefixContainerDisplayConfiguration winePrefixContainerDisplayConfiguration,
            WinePrefixContainerInputConfiguration winePrefixContainerInputConfiguration) {
        this.compatibleConfigFileFormatFactory = compatibleConfigFileFormatFactory;
        this.winePrefixContainerDisplayConfiguration = winePrefixContainerDisplayConfiguration;
        this.winePrefixContainerInputConfiguration = winePrefixContainerInputConfiguration;
    }

    @Override
    public void fetchContainers(Consumer<List<ContainerCategoryDTO>> callback, Consumer<Exception> errorCallback) {
        final File winePrefixesFile = new File(winePrefixPath);
        winePrefixesFile.mkdirs();

        final File[] winePrefixes = winePrefixesFile.listFiles();

        if (winePrefixes == null) {
            callback.accept(Collections.emptyList());
        } else {
            final List<ContainerCategoryDTO> containerCategories = new ArrayList<>();
            final List<ContainerDTO> containers = new ArrayList<>();
            for (File winePrefix : winePrefixes) {
                final ConfigFile configFile = compatibleConfigFileFormatFactory
                        .open(new File(winePrefix, "phoenicis.cfg"));
                final File userRegistryFile = new File(winePrefix, "user.reg");

                containers.add(new WinePrefixContainerDTO.Builder().withName(winePrefix.getName())
                        .withPath(winePrefix.getAbsolutePath())
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
                        .withMultisampling(winePrefixContainerDisplayConfiguration.getMultisampling(userRegistryFile))
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

            containers.sort(ContainerDTO.nameComparator());

            containerCategories
                    .add(new ContainerCategoryDTO.Builder().withName("Wine").withContainers(containers).build());

            callback.accept(containerCategories);
        }
    }

}
