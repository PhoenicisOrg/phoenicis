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

package org.phoenicis.javafx;

import org.phoenicis.configuration.PhoenicisGlobalConfiguration;
import org.phoenicis.containers.ContainersConfiguration;
import org.phoenicis.engines.EnginesConfiguration;
import org.phoenicis.javafx.controller.ControllerConfiguration;
import org.phoenicis.javafx.settings.JavaFxSettingsConfiguration;
import org.phoenicis.javafx.views.ViewsConfiguration;
import org.phoenicis.javafx.views.common.ThemeConfiguration;
import org.phoenicis.javafx.views.scriptui.JavaFxUiConfiguration;
import org.phoenicis.library.LibraryConfiguration;
import org.phoenicis.multithreading.MultithreadingConfiguration;
import org.phoenicis.repository.RepositoryConfiguration;
import org.phoenicis.scripts.ScriptsConfiguration;
import org.phoenicis.settings.SettingsConfiguration;
import org.phoenicis.tools.ToolsConfiguration;
import org.phoenicis.win32.Win32Configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ PhoenicisGlobalConfiguration.class,
        ControllerConfiguration.class,
        ScriptsConfiguration.class,
        ThemeConfiguration.class,
        ViewsConfiguration.class,
        RepositoryConfiguration.class,
        EnginesConfiguration.class,
        LibraryConfiguration.class,
        SettingsConfiguration.class,
        JavaFxSettingsConfiguration.class,
        JavaFxUiConfiguration.class,
        MultithreadingConfiguration.class,
        Win32Configuration.class,
        ToolsConfiguration.class,
        ContainersConfiguration.class
})
class AppConfiguration {

}
