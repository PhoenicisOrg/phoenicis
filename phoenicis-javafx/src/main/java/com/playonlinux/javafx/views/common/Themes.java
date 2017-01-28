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

package com.playonlinux.javafx.views.common;

public enum Themes {
    DEFAULT("Default theme", "default"),
    DARK("Dark theme", "dark"),
    BREEZE_DARK("Breeze Dark theme", "breezeDark"),
    UNITY("Unity theme", "unity"),
    HIDPI("HiDPI theme", "hidpi");

    private final String name;
    private final String shortName;

    Themes(String themeName, String themeShortName) {
        name = themeName;
        shortName = themeShortName;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getShortName() {
        return shortName;
    }

    public static Themes fromShortName(String shortName) {
        for(Themes theme: Themes.values()) {
            if(theme.shortName.equals(shortName)) {
                return theme;
            }
        }

        return DEFAULT;
    }
}
