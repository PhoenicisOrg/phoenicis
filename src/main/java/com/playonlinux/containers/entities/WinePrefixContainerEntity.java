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

package com.playonlinux.containers.entities;

public class WinePrefixContainerEntity extends ContainerEntity {
    private final String wineVersion;
    private final String wineDistribution;
    private final String wineArchitecture;
    private final boolean isAutomaticallyUpdated;

    private WinePrefixContainerEntity(Builder builder) {
        super(builder.name, builder.path);
        this.wineArchitecture = builder.wineArchitecture;
        this.wineDistribution = builder.wineDistribution;
        this.wineVersion = builder.wineVersion;
        this.isAutomaticallyUpdated = builder.isAutomaticallyUpdated;
    }

    public String getWineArchitecture() {
        return wineArchitecture;
    }

    public String getWineDistribution() {
        return wineDistribution;
    }

    public String getWineVersion() {
        return wineVersion;
    }


    public static class Builder {
        private String wineVersion;
        private String wineDistribution;
        private String wineArchitecture;
        private String path;
        private String name;
        public boolean isAutomaticallyUpdated;

        public WinePrefixContainerEntity build() {
            return new WinePrefixContainerEntity(this);
        }

        public Builder withWineVersion(String wineVersion) {
            this.wineVersion = wineVersion;
            return this;
        }

        public Builder withWineDistribution(String wineDistribution) {
            this.wineDistribution = wineDistribution;
            return this;
        }

        public Builder withWineArchitecture(String wineArchitecture) {
            this.wineArchitecture = wineArchitecture;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAutomaticallyUpdated(boolean isAutomaticallyUpdated) {
            this.isAutomaticallyUpdated = isAutomaticallyUpdated;
            return this;
        }
    }
}
