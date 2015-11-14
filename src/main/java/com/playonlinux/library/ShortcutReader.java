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

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.library.shortcuts.Shortcut;

@Scan
public class ShortcutReader {
    @Inject
    static ObjectMapper objectMapper;

    public Shortcut readShortcut(ShortcutFiles shortcutFiles) throws LibraryException {
        try {
            return objectMapper.readValue(shortcutFiles.getShortcutFile(), Shortcut.class);
        } catch (IOException e) {
            throw new LibraryException(e);
        }
    }
}
