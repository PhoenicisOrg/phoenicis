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

package com.playonlinux.engines.wine.dto.ui;

import com.playonlinux.dto.AbstractDTO;

import java.util.List;

public class WineVersionDistributionItemDTO implements AbstractDTO {
    private final String name;

    public List<WineVersionItemDTO> getAvailablePackages() {
        return availablePackages;
    }

    public String getDescription() {
        return description;
    }

    public List<WineVersionItemDTO> getInstalledPackages() {
        return installedPackages;
    }

    public String getName() {
        return name;
    }

    private final String description;
    private final List<WineVersionItemDTO> availablePackages;
    private final List<WineVersionItemDTO> installedPackages;

    private WineVersionDistributionItemDTO(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.availablePackages = builder.availablePackages;
        this.installedPackages = builder.installedPackages;
    }

    public static class Builder {
        private String name;
        private List<WineVersionItemDTO> installedPackages;
        private String description;
        private List<WineVersionItemDTO> availablePackages;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withInstalledPackages(List<WineVersionItemDTO> installedPackages) {
            this.installedPackages = installedPackages;
            return this;
        }

        public Builder withAvailablePackages(List<WineVersionItemDTO> availablePackages) {
            this.availablePackages = availablePackages;
            return this;
        }

        public WineVersionDistributionItemDTO build() {
            return new WineVersionDistributionItemDTO(this);
        }
    }
}
