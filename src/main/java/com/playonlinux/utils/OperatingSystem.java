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

package com.playonlinux.utils;


import com.playonlinux.domain.PlayOnLinuxError;

public enum OperatingSystem {
    MACOSX ("Mac OS X"),
    LINUX ("Linux"),
    FREEBSD ("FreeBSD");

    private String name = "";

    OperatingSystem(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static OperatingSystem fromString(String name) throws PlayOnLinuxError {
        if("Mac OS X".equals(name)) {
            return OperatingSystem.MACOSX;
        }
        if("Linux".equals(name)) {
            return OperatingSystem.LINUX;
        }
        if("FreeBSD".equals(name)) {
            return OperatingSystem.FREEBSD;
        }

        throw new PlayOnLinuxError(String.format("Incompatible operation system \"%s\"", name));
    }

    public String fetchShortName() {
        return this.name();
    }

    public String getNameForWinePackages() {
        switch (this) {
            case FREEBSD:
                return "freebsd";
            case MACOSX:
                return "darwin";
            case LINUX:
            default:
                return "linux";
        }
    }

    public static OperatingSystem fetchCurrentOperationSystem() throws PlayOnLinuxError {
        return OperatingSystem.fromString(System.getProperty("os.name"));
    }


}
