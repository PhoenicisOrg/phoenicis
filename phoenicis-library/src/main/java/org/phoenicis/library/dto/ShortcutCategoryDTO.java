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
import org.apache.commons.lang.builder.ToStringBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a category of application
 */
@JsonDeserialize(builder = ShortcutCategoryDTO.Builder.class)
public class ShortcutCategoryDTO {
    private final String name;
    private final String description;
    private final List<ShortcutDTO> shortcuts;
    private URI icon;

    private ShortcutCategoryDTO(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.shortcuts = Collections.unmodifiableList(builder.shortcuts);
        this.icon = builder.icon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ShortcutDTO> getShortcuts() {
        return shortcuts;
    }

    public URI getIcon() {
        return icon;
    }

    public static Comparator<ShortcutCategoryDTO> nameComparator() {
        return (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String name;
        private String description;
        private List<ShortcutDTO> shortcuts = new ArrayList<>();
        private URI icon;

        public Builder() {
            // Default constructor
        }

        public Builder(ShortcutCategoryDTO categoryDTO) {
            this.withName(categoryDTO.getName()).withDescription(categoryDTO.getDescription())
                    .withShortcuts(categoryDTO.getShortcuts()).withIcon(categoryDTO.getIcon());
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withShortcuts(List<ShortcutDTO> shortcuts) {
            this.shortcuts = shortcuts;
            return this;
        }

        public Builder withIcon(URI iconPath) {
            this.icon = iconPath;
            return this;
        }

        public ShortcutCategoryDTO build() {
            return new ShortcutCategoryDTO(this);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(name).toString();
    }

}
