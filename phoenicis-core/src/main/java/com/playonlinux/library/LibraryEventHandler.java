/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.library;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.framework.wizard.WineWizard;
import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.library.shortcuts.Shortcut;

@Scan
public class LibraryEventHandler {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static ObjectMapper objectMapper;

    /**
     * Starts the UI
     * @param applicationName The name of the application
     * @throws PlayOnLinuxException If any error occur
     */
    public void runApplication(WineWizard wineWizard, String applicationName) throws PlayOnLinuxException {
        final File shortcutFile = new File(playOnLinuxContext.makeShortcutsPath(), applicationName);
        try {
            final Shortcut shortcut = objectMapper.readValue(shortcutFile, Shortcut.class);
            shortcut.execute(wineWizard);
        } catch (IOException e) {
            throw new PlayOnLinuxException("Unable to parse the shortcut", e);
        }
    }

}
