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

import org.phoenicis.containers.wine.WineContainerController;
import org.phoenicis.containers.wine.WinePrefixesManager;
import org.phoenicis.containers.wine.configurations.*;
import org.phoenicis.library.LibraryConfiguration;
import org.phoenicis.multithreading.MultithreadingConfiguration;
import org.phoenicis.scripts.ScriptsConfiguration;
import org.phoenicis.tools.ToolsConfiguration;
import org.phoenicis.win32.Win32Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContainersConfiguration {
    @Autowired
    private LibraryConfiguration libraryConfiguration;

    @Autowired
    private MultithreadingConfiguration multithreadingConfiguration;

    @Autowired
    private ToolsConfiguration toolsConfiguration;

    @Autowired
    private Win32Configuration win32Configuration;

    @Autowired
    private ScriptsConfiguration scriptsConfiguration;

    @Value("${application.user.engines.wine}")
    private String wineEnginesPath;

    @Bean
    public ContainersManager containersManager() {
        return new WinePrefixesManager(
                toolsConfiguration.compatibleConfigFileFormatFactory(),
                winePrefixDisplayConfiguration(),
                winePrefixInputConfiguration()
        );
    }

    @Bean
    public ContainersManager backgroundContainersManager() {
        return new BackgroundContainersManager(containersManager(), multithreadingConfiguration.containersExecutorService());
    }

    @Bean
    public WineContainerController wineContainerController() {
        return new WineContainerController(
                scriptsConfiguration.scriptInterpreter(),
                toolsConfiguration.terminalOpener(),
                wineEnginesPath,
                toolsConfiguration.operatingSystemFetcher(),
                win32Configuration.registryWriter(),
                libraryConfiguration.libraryManager(),
                libraryConfiguration.shortcutManager(),
                toolsConfiguration.fileUtilities()
        );
    }

    @Bean
    WinePrefixInputConfiguration winePrefixInputConfiguration() {
        return new RegistryWinePrefixInputConfiguration(win32Configuration.registryParser(), new DefaultWinePrefixInputConfiguration());
    }

    @Bean
    WinePrefixDisplayConfiguration winePrefixDisplayConfiguration() {
        return new RegistryWinePrefixDisplayConfiguration(win32Configuration.registryParser(), new DefaultWinePrefixDisplayConfiguration());
    }
}
