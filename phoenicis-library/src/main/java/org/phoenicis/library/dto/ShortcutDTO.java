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

import java.net.URI;
import java.util.Comparator;

@JsonDeserialize(builder = ShortcutDTO.Builder.class)
public class ShortcutDTO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortcutDTO.class);
    private final String id;
    private final ShortcutInfoDTO info;
    private final URI icon;
    /*
     * it is necessary to store the category icon per shortcut, otherwise category icons in the library could be
     * overwritten if multiple shortcuts from different sources use the same category
     * this could lead to a strange behavior in the following case:
     * 1. an app A with category C and category icon I(A) from repository R1 is installed
     * if the theme does not specify an icon, I(A) is shown in the library
     * 2. an app B with category C and category icon I(B) from repository R2 is installed such that I(A) != I(B) (even
     * though the category C is the same)
     * if the theme does not specify an icon, I(B) is shown in the library
     * 3. app B is uninstalled
     * if the theme does not specify an icon, I(B) is still shown in the library even though the situation is the same
     * as after installing app A
     */
    private final URI categoryIcon;
    private final URI miniature;
    private final String script;

    private ShortcutDTO(Builder builder) {
        if (builder.id != null) {
            if (builder.id.matches("^[a-zA-Z0-9 ]+$")) {
                this.id = builder.id;
            } else {
                LOGGER.warn(String.format("Shortcut ID (%s) contains invalid characters, will remove them.",
                        builder.id));
                this.id = builder.id.replaceAll("[^a-zA-Z0-9 ]", "");
            }
        } else {
            this.id = null;
        }

        info = builder.info;
        icon = builder.icon;
        categoryIcon = builder.categoryIcon;
        miniature = builder.miniature;
        script = builder.script;
    }

    public ShortcutInfoDTO getInfo() {
        return info;
    }

    public URI getIcon() {
        return icon;
    }

    public URI getCategoryIcon() {
        return categoryIcon;
    }

    public URI getMiniature() {
        return miniature;
    }

    public String getId() {
        return id;
    }

    public String getScript() {
        return script;
    }

    public static Comparator<ShortcutDTO> nameComparator() {
        return (o1, o2) -> o1.getId().compareToIgnoreCase(o2.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShortcutDTO that = (ShortcutDTO) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(info, that.info)
                .append(icon, that.icon)
                .append(categoryIcon, that.categoryIcon)
                .append(miniature, that.miniature)
                .append(script, that.script)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(info)
                .append(icon)
                .append(categoryIcon)
                .append(miniature)
                .append(script)
                .toHashCode();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String id;
        private ShortcutInfoDTO info;
        private URI icon;
        private URI categoryIcon;
        private URI miniature;
        private String script;

        public Builder() {
            // Default constructor
        }

        public Builder(ShortcutDTO shortcutDTO) {
            this.info = shortcutDTO.info;
            this.id = shortcutDTO.id;
            this.icon = shortcutDTO.icon;
            this.categoryIcon = shortcutDTO.categoryIcon;
            this.miniature = shortcutDTO.miniature;
            this.script = shortcutDTO.script;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withInfo(ShortcutInfoDTO info) {
            this.info = info;
            return this;
        }

        public Builder withIcon(URI icon) {
            this.icon = icon;
            return this;
        }

        public Builder withCategoryIcon(URI categoryIcon) {
            this.categoryIcon = categoryIcon;
            return this;
        }

        public Builder withMiniature(URI miniature) {
            this.miniature = miniature;
            return this;
        }

        public Builder withScript(String script) {
            this.script = script;
            return this;
        }

        public ShortcutDTO build() {
            return new ShortcutDTO(this);
        }
    }

}
