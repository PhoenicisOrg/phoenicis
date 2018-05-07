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

import java.util.List;

public class WinePrefixContainerDTO extends ContainerDTO {
    private final String architecture;
    private final String distribution;
    private final String version;

    private WinePrefixContainerDTO(Builder builder) {
        super(builder.name, builder.path, ContainerType.WINEPREFIX, "Wine", builder.installedShortcuts);
        this.architecture = builder.architecture;
        this.distribution = builder.distribution;
        this.version = builder.version;
    }

    public String getArchitecture() {
        return architecture;
    }

    public String getDistribution() {
        return distribution;
    }

    public String getVersion() {
        return version;
    }

    public static class Builder {
        private String name;
        private String path;
        private String architecture;
        private String distribution;
        private String version;
        private List<ShortcutDTO> installedShortcuts;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withInstalledShortcuts(List<ShortcutDTO> installedShortcuts) {
            this.installedShortcuts = installedShortcuts;
            return this;
        }

        public Builder withArchitecture(String architecture) {
            this.architecture = architecture;
            return this;
        }

        public Builder withDistribution(String distribution) {
            this.distribution = distribution;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public ContainerDTO build() {
            return new WinePrefixContainerDTO(this);
        }
    }
}
