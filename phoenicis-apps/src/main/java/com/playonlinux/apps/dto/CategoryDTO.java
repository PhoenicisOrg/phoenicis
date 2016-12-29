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

package com.playonlinux.apps.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.TreeMap;

/**
 * Represents a category of application
 */
@JsonDeserialize(builder = CategoryDTO.Builder.class)
public class CategoryDTO {
    private final CategoryType type;
    private final String name;
    private final TreeMap<String, ApplicationDTO> applications;
    private final byte[] icon;

    private CategoryDTO(Builder builder) {
        this.type = builder.type;
        this.name = builder.name;
        this.applications = builder.applications;
        this.icon = builder.icon;
    }

    public byte[] getIcon() {
        return icon;
    }

    public enum CategoryType {
        INSTALLERS,
        FUNCTIONS
    }

    public CategoryType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public TreeMap<String, ApplicationDTO> getApplications() {
        return applications;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private CategoryType type;
        private String name;
        private TreeMap<String, ApplicationDTO> applications;
        private byte[] icon;

        public Builder() {
            // Default constructor
        }

        public Builder(CategoryDTO categoryDTO) {
            this.withName(categoryDTO.getName())
                    .withApplications(categoryDTO.getApplications())
                    .withIcon(categoryDTO.getIcon())
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

        public Builder withApplications(TreeMap<String, ApplicationDTO> applications) {
            this.applications = applications;
            return this;
        }

        public Builder withIcon(byte[] icon) {
            this.icon = icon;
            return this;
        }

        public CategoryDTO build() {
            return new CategoryDTO(this);
        }
    }


}
