package com.playonlinux.engines.dto;

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


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.playonlinux.tools.version.Version;
import com.playonlinux.tools.version.VersionComparator;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Comparator;
import java.util.List;

public class WineVersionDistributionDTO {
    private final String name;
    private final String description;
    private final List<WineVersionDTO> packages;

    @JsonCreator
    public WineVersionDistributionDTO(@JsonProperty("name") String name,
                                      @JsonProperty("description") String description,
                                      @JsonProperty("packages") List<WineVersionDTO> packages) {
        this.name = name;
        this.description = description;
        this.packages = packages;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public List<WineVersionDTO> getPackages() {
        return packages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(WineVersionDistributionDTO.class)
                .append("name", name)
                .append("description", description)
                .append("packages", packages).toString();
    }

    public static Comparator<? super WineVersionDTO> Comparator() {
        return (Comparator<WineVersionDTO>) (o1, o2) -> new VersionComparator().compare(
                new Version(o1.getVersion()), new Version(o2.getVersion())
        );
    }
}
