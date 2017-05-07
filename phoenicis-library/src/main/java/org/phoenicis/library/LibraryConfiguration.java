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

import org.phoenicis.scripts.ScriptsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LibraryConfiguration {
    @Autowired
    private ScriptsConfiguration scriptsConfiguration;

    @Value("${application.user.shortcuts}")
    private String shortcutDirectory;

    @Value("${application.user.desktopShortcuts}")
    private String desktopShortcutDirectory;

    @Bean
    public LibraryManager libraryManager() {
        return new LibraryManager(shortcutDirectory);
    }

    @Bean
    public ShortcutManager shortcutManager() {
        return new ShortcutManager(shortcutDirectory, desktopShortcutDirectory, libraryManager(),
                scriptsConfiguration.scriptInterpreter());
    }

    @Bean
    public ShortcutRunner shortcutRunner() {
        return new ShortcutRunner(scriptsConfiguration.scriptInterpreter(), libraryManager());
    }
}
