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

import java.io.File;
import java.net.URI;
import java.util.Comparator;

@JsonDeserialize(builder = ShortcutCreationDTO.Builder.class)
public class ShortcutCreationDTO {
    private final String name;
    private final String category;
    private final String description;
    private final URI icon;
    private final URI miniature;
    private final File executable;

    private ShortcutCreationDTO(Builder builder) {
        name = builder.name;
        category = builder.category;
        description = builder.description;
        icon = builder.icon;
        miniature = builder.miniature;
        executable = builder.executable;
    }

    public URI getIcon() {
        return icon;
    }

    public URI getMiniature() {
        return miniature;
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

    public File getExecutable() {
        return executable;
    }

    public static Comparator<ShortcutCreationDTO> nameComparator() {
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

        ShortcutCreationDTO that = (ShortcutCreationDTO) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(category, that.category)
                .append(description, that.description)
                .append(icon, that.icon)
                .append(miniature, that.miniature)
                .append(executable, that.executable)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(category)
                .append(description)
                .append(icon)
                .append(miniature)
                .append(executable)
                .toHashCode();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String name;
        private String category;
        private String description;
        private URI icon;
        private URI miniature;
        private File executable;

        public Builder() {
            // Default constructor
        }

        public Builder(ShortcutCreationDTO shortcutDTO) {
            this.name = shortcutDTO.name;
            this.category = shortcutDTO.category;
            this.description = shortcutDTO.description;
            this.icon = shortcutDTO.icon;
            this.miniature = shortcutDTO.miniature;
            this.executable = shortcutDTO.executable;
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

        public Builder withIcon(URI icon) {
            this.icon = icon;
            return this;
        }

        public Builder withMiniature(URI miniature) {
            this.miniature = miniature;
            return this;
        }

        public Builder withExecutable(File executable) {
            this.executable = executable;
            return this;
        }

        public ShortcutCreationDTO build() {
            return new ShortcutCreationDTO(this);
        }

        public String getName() {
            return name;
        }
    }

}
