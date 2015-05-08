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

package com.playonlinux.ui.dtos;


import java.io.File;

public class VirtualDriveDTO {
    private final File icon;
    private final String name;

    public VirtualDriveDTO(Builder builder) {
        this.name = builder.name;
        this.icon = builder.icon;
    }

    public String getName() {
        return name;
    }

    public File getIcon() {
        return icon;
    }

    public static class Builder {
        private String name;
        private File icon;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withIcon(File icon) {
            this.icon = icon;
            return this;
        }

        public VirtualDriveDTO build() {
            return new VirtualDriveDTO(this);
        }
    }
}
