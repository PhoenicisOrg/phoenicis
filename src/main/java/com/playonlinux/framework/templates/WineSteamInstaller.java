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

package com.playonlinux.framework.templates;

import com.playonlinux.core.python.PythonAttribute;

import java.util.List;

public abstract class WineSteamInstaller extends Installer {
    @PythonAttribute
    String prefix;

    @PythonAttribute
    String wineversion;

    @PythonAttribute
    int steamId;

    @PythonAttribute
    List<String> packages;

    public void main() {
        System.out.println(String.format("Implementation has to be done, but we have access to prefix (%s), " +
                "wineversion (%s), steamId (%s) and packages (%s). First package (to check that we have " +
                "a list: %s", prefix, wineversion, steamId, packages, packages.get(0)));
    }
}
