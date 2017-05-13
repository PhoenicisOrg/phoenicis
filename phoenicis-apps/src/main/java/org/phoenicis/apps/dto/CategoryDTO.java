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

package org.phoenicis.apps.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a category of application
 */
@JsonDeserialize(builder = CategoryDTO.Builder.class)
public class CategoryDTO {
    private final CategoryType type;
    private final String name;
    private final List<ApplicationDTO> applications;
    private final URI icon;

    private CategoryDTO(Builder builder) {
        this.type = builder.type;
        this.name = builder.name;
        this.applications = Collections.unmodifiableList(builder.applications);
        this.icon = builder.icon;
    }

    public URI getIcon() {
        return icon;
    }

    public enum CategoryType {
        INSTALLERS, FUNCTIONS
    }

    public CategoryType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<ApplicationDTO> getApplications() {
        return applications;
    }

    public static Comparator<CategoryDTO> nameComparator() {
        return (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private CategoryType type;
        private String name;
        private List<ApplicationDTO> applications = new ArrayList<>();
        private URI icon;

        public Builder() {
            // Default constructor
        }

        public Builder(CategoryDTO categoryDTO) {
            this.withName(categoryDTO.getName()).withApplications(categoryDTO.getApplications())
                    .withIcon(categoryDTO.getIcon()).withType(categoryDTO.getType());
        }

        public Builder withType(CategoryType type) {
            this.type = type;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withApplications(List<ApplicationDTO> applications) {
            this.applications = applications;
            return this;
        }

        public Builder withIcon(URI iconPath) {
            this.icon = iconPath;
            return this;
        }

        public CategoryDTO build() {
            return new CategoryDTO(this);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(name).append(type).toString();
    }

}
