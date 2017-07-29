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
import org.phoenicis.configuration.localisation.Translatable;
import org.phoenicis.configuration.localisation.TranslatableBuilder;
import org.phoenicis.configuration.localisation.Translate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a repository type (e.g. engines, applications ...)
 */
@JsonDeserialize(builder = TypeDTO.Builder.class)
@Translatable
public class TypeDTO {
    private static final Logger LOGGER = LoggerFactory.getLogger(TypeDTO.class);
    private final String id;
    private final String name;
    private final List<CategoryDTO> categories;
    private final URI icon;

    private TypeDTO(Builder builder) {
        if (builder.id != null) {
            if (builder.id.matches("^[a-zA-Z0-9]+$")) {
                this.id = builder.id;
            } else {
                LOGGER.warn(
                        String.format("Type ID (%s) contains invalid characters, will remove them.", builder.id));
                this.id = builder.id.replaceAll("[^a-zA-Z0-9]", "");
            }
        } else {
            this.id = null;
        }

        this.name = builder.name == null ? builder.id : builder.name;
        this.categories = Collections.unmodifiableList(builder.categories);
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

        TypeDTO that = (TypeDTO) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(name, that.name)
                .append(categories, that.categories)
                .append(icon, that.icon)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(categories)
                .append(icon)
                .toHashCode();
    }

    public enum CategoryType {
        INSTALLERS, FUNCTIONS
    }

    public String getId() {
        return id;
    }

    @Translate
    public String getName() {
        return name;
    }

    @Translate
    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public static Comparator<TypeDTO> nameComparator() {
        return (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    @TranslatableBuilder
    public static class Builder {
        private String id;
        private String name;
        private List<CategoryDTO> categories = new ArrayList<>();
        private URI icon;

        public Builder() {
            // Default constructor
        }

        public Builder(TypeDTO categoryDTO) {
            this.withId(categoryDTO.getId()).withName(categoryDTO.getName())
                    .withCategories(categoryDTO.getCategories()).withIcon(categoryDTO.getIcon());
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCategories(List<CategoryDTO> categories) {
            this.categories = categories;
            return this;
        }

        public Builder withIcon(URI iconPath) {
            this.icon = iconPath;
            return this;
        }

        public TypeDTO build() {
            return new TypeDTO(this);
        }

    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(name).toString();
    }

}
