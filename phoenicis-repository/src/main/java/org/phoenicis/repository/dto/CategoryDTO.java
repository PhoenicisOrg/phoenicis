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

package org.phoenicis.repository.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Represents a category of application
 */
@JsonDeserialize(builder = CategoryDTO.Builder.class)
public class CategoryDTO {
    private final CategoryType type;
    private final String name;
    private final String id;
    private final List<ApplicationDTO> applications;
    private final URI icon;

    private CategoryDTO(Builder builder) {
        this.type = builder.type;
        this.name = builder.name.isEmpty() ? builder.id : tr(builder.name);
        this.id = builder.id;
        this.applications = Collections.unmodifiableList(builder.applications);
        this.icon = builder.icon;
    }

    public URI getIcon() {
        return icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CategoryDTO that = (CategoryDTO) o;

        return new EqualsBuilder().append(type, that.type).append(name, that.name).append(id, that.id)
                .append(applications, that.applications).append(icon, that.icon).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(type).append(name).append(id).append(applications).append(icon)
                .toHashCode();
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

    public String getId() {
        return id;
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
        private String name = "";
        private String id = "";
        private List<ApplicationDTO> applications = new ArrayList<>();
        private URI icon;

        public Builder() {
            // Default constructor
        }

        public Builder(CategoryDTO categoryDTO) {
            this.withName(categoryDTO.getName()).withId(categoryDTO.getId())
                    .withApplications(categoryDTO.getApplications()).withIcon(categoryDTO.getIcon())
                    .withType(categoryDTO.getType());
        }

        public Builder withType(CategoryType type) {
            this.type = type;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
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
