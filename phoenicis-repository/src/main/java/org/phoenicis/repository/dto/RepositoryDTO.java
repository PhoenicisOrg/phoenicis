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
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a category of application
 */
@JsonDeserialize(builder = RepositoryDTO.Builder.class)
public class RepositoryDTO {
    private final String name;
    private final List<CategoryDTO> categories;
    private final RepositoryType type;

    private RepositoryDTO(Builder builder) {
        this.name = builder.name;
        this.categories = Collections.unmodifiableList(builder.categories);
        this.type = builder.type;
    }

    public String getName() {
        return name;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public RepositoryType getType() {
        return type;
    }

    public enum RepositoryType {
        APPLICATIONS,
        FUNCTIONS
    }

    public static Comparator<RepositoryDTO> nameComparator() {
        return (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String name;
        private List<CategoryDTO> categories = new ArrayList<>();
        private RepositoryType type;

        public Builder() {
            // Default constructor
        }

        public Builder(RepositoryDTO repositoryDTO) {
            this.withName(repositoryDTO.getName())
                    .withCategories(repositoryDTO.getCategories())
                    .withType(repositoryDTO.getType());
        }


        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCategories(List<CategoryDTO> categories) {
            this.categories = categories;
            return this;
        }


        public Builder withType(RepositoryType type) {
            this.type = type;
            return this;
        }

        public RepositoryDTO build() {
            return new RepositoryDTO(this);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(name)
                .append(type)
                .toString();
    }

}
