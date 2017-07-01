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

package org.phoenicis.containers.dto;

import org.phoenicis.library.dto.ShortcutDTO;

import java.util.Comparator;
import java.util.List;

public class ContainerDTO {
    private final String name;
    private final String path;
    private final ContainerType type;
    private final List<ShortcutDTO> installedShortcuts;

    public ContainerDTO(String name, String path, ContainerType type, List<ShortcutDTO> installedShortcuts) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.installedShortcuts = installedShortcuts;
    }

    public String getName() {
        return name;
    }

    public ContainerType getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public List<ShortcutDTO> getInstalledShortcuts() {
        return installedShortcuts;
    }

    public static Comparator<ContainerDTO> nameComparator() {
        return (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }

    public enum ContainerType {
        WINEPREFIX
    }
}
