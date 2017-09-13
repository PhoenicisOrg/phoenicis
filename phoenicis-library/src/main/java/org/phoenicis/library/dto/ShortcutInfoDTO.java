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

package org.phoenicis.library.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

/**
 * contains general information about a shortcut
 */
@JsonDeserialize(builder = ShortcutInfoDTO.Builder.class)
public class ShortcutInfoDTO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortcutInfoDTO.class);
    private final String name;
    private final String category;
    private final String description;

    private ShortcutInfoDTO(Builder builder) {
        name = builder.name;
        category = builder.category;
        description = builder.description;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public static Comparator<ShortcutInfoDTO> nameComparator() {
        return (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShortcutInfoDTO that = (ShortcutInfoDTO) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(category, that.category)
                .append(description, that.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(category)
                .append(description)
                .toHashCode();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String name;
        private String category;
        private String description;

        public Builder() {
            // Default constructor
        }

        public Builder(ShortcutInfoDTO shortcutDTO) {
            this.name = shortcutDTO.name;
            this.category = shortcutDTO.category;
            this.description = shortcutDTO.description;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ShortcutInfoDTO build() {
            return new ShortcutInfoDTO(this);
        }

        public String getName() {
            return this.name;
        }

        public String getCategory() {
            return this.category;
        }
    }

}
