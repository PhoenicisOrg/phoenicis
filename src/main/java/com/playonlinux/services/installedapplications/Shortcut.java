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

package com.playonlinux.services.installedapplications;

import com.playonlinux.installer.Script;

import java.io.File;
import java.net.URL;

class Shortcut {

    private final String shortcutName;
    private final URL iconPath;
    private final Script runScript;
    private final File configFile;

    public Shortcut(String shorctName, URL iconPath, Script runScript) {
        this(shorctName, iconPath, runScript, null);
    }
    public Shortcut(String shortcutName, URL iconPath, Script runScript, File configFile) {
        this.shortcutName = shortcutName;
        this.iconPath = iconPath;
        this.runScript = runScript;
        this.configFile = configFile;
    }
    
    public String getShortcutName() {
        return shortcutName;
    }

    public URL getIconPath() {
        return iconPath;
    }
}
