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

package org.phoenicis.engines.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.phoenicis.tools.version.Version;
import org.phoenicis.tools.version.VersionComparator;

import java.util.Comparator;
import java.util.List;

public class EngineSubCategoryDTO {
    private final String name;
    private final String description;
    private final List<EngineVersionDTO> packages;

    @JsonCreator
    public EngineSubCategoryDTO(@JsonProperty("name") String name, @JsonProperty("description") String description,
            @JsonProperty("packages") List<EngineVersionDTO> packages) {
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

    public List<EngineVersionDTO> getPackages() {
        return packages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(EngineSubCategoryDTO.class).append("name", name).append("description", description)
                .append("packages", packages).toString();
    }

    public static Comparator<EngineVersionDTO> comparator() {
        return (o1, o2) -> new VersionComparator().compare(new Version(o1.getVersion()), new Version(o2.getVersion()));
    }
}
