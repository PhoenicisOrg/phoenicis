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

package com.playonlinux.javafx;


import com.phoenicis.library.LibraryConfiguration;
import com.phoenicis.settings.SettingsConfiguration;
import com.playonlinux.apps.AppsConfiguration;
import com.playonlinux.configuration.PlayOnLinuxGlobalConfiguration;
import com.playonlinux.containers.ContainersConfiguration;
import com.playonlinux.engines.EnginesConfiguration;
import com.playonlinux.javafx.controller.ControllerConfiguration;
import com.playonlinux.javafx.views.ViewsConfiguration;
import com.playonlinux.javafx.views.setupwindow.JavaFXSetupWindowUIConfiguration;
import com.playonlinux.multithreading.MultithreadingConfiguration;
import com.playonlinux.scripts.ScriptsConfiguration;
import com.playonlinux.tools.ToolsConfiguration;
import com.playonlinux.win32.Win32Configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        PlayOnLinuxGlobalConfiguration.class,
        ControllerConfiguration.class,
        ScriptsConfiguration.class,
        ViewsConfiguration.class,
        AppsConfiguration.class,
        EnginesConfiguration.class,
        LibraryConfiguration.class,
        SettingsConfiguration.class,
        JavaFXSetupWindowUIConfiguration.class,
        MultithreadingConfiguration.class,
        Win32Configuration.class,
        ToolsConfiguration.class,
        ContainersConfiguration.class
})
class AppConfiguration {


}
