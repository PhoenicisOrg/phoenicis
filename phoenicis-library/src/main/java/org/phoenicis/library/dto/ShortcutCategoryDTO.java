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
import org.apache.commons.lang.builder.ToStringBuilder;
import org.phoenicis.configuration.localisation.Translatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Represents a category of application
 */
@JsonDeserialize(builder = ShortcutCategoryDTO.Builder.class)
public class ShortcutCategoryDTO implements Translatable<ShortcutCategoryDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortcutCategoryDTO.class);

    private final String id;
    private final String name;
    private final String description;
    private final List<ShortcutDTO> shortcuts;
    private URI icon;

    private ShortcutCategoryDTO(Builder builder) {
        if (builder.id != null) {
            if (builder.id.matches("^[a-zA-Z0-9]+$")) {
                this.id = builder.id;
            } else {
                LOGGER.warn(String.format("Shortcut category ID (%s) contains invalid characters, will remove them.",
                        builder.id));
                this.id = builder.id.replaceAll("[^a-zA-Z0-9]", "");
            }
        } else {
            this.id = null;
        }

        this.name = builder.name == null ? builder.id : builder.name;
        this.description = builder.description;
        this.shortcuts = Collections.unmodifiableList(builder.shortcuts);
        this.icon = builder.icon;
    }

    public String getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShortcutCategoryDTO that = (ShortcutCategoryDTO) o;

        return new EqualsBuilder().append(id, that.id).append(name, that.name).append(description, that.description)
                .append(shortcuts, that.shortcuts).append(icon, that.icon).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(description).append(shortcuts).append(icon)
                .toHashCode();
    }

    @Override
    public ShortcutCategoryDTO translate() {
        return new ShortcutCategoryDTO.Builder(this).withName(tr(this.name)).withDescription(tr(this.description))
                .build();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String id;
        private String name;
        private String description;
        private List<ShortcutDTO> shortcuts = new ArrayList<>();
        private URI icon;

        public Builder() {
            // Default constructor
        }

        public Builder(ShortcutCategoryDTO categoryDTO) {
            this.withId(categoryDTO.getId()).withName(categoryDTO.getName())
                    .withDescription(categoryDTO.getDescription()).withShortcuts(categoryDTO.getShortcuts())
                    .withIcon(categoryDTO.getIcon());
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
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
