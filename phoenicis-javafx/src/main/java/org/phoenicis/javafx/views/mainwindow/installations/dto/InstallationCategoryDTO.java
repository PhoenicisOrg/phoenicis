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

package org.phoenicis.javafx.views.mainwindow.installations.dto;

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
 * Represents a category of application
 */
@JsonDeserialize(builder = InstallationCategoryDTO.Builder.class)
@Translatable
public class InstallationCategoryDTO {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstallationCategoryDTO.class);
    private final String id;
    private final String name;
    private final String description;
    private final List<InstallationDTO> installations;
    private URI icon;

    private InstallationCategoryDTO(Builder builder) {
        if (builder.id != null) {
            if (builder.id.matches("^[a-zA-Z0-9]+$")) {
                this.id = builder.id;
            } else {
                LOGGER.warn(
                        String.format("Installation category ID (%s) contains invalid characters, will remove them.",
                                builder.id));
                this.id = builder.id.replaceAll("[^a-zA-Z0-9_]", "");
            }
        } else {
            this.id = null;
        }

        this.name = builder.name == null ? builder.id : builder.name;
        this.description = builder.description;
        this.installations = Collections.unmodifiableList(builder.installations);
        this.icon = builder.icon;
    }

    public String getId() {
        return id;
    }

    @Translate
    public String getName() {
        return name;
    }

    @Translate
    public String getDescription() {
        return description;
    }

    public List<InstallationDTO> getInstallations() {
        return installations;
    }

    public URI getIcon() {
        return icon;
    }

    public static Comparator<InstallationCategoryDTO> nameComparator() {
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

        InstallationCategoryDTO that = (InstallationCategoryDTO) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(name, that.name)
                .append(description, that.description)
                .append(installations, that.installations)
                .append(icon, that.icon)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(description)
                .append(installations)
                .append(icon)
                .toHashCode();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    @TranslatableBuilder
    public static class Builder {
        private String id;
        private String name;
        private String description;
        private List<InstallationDTO> installations = new ArrayList<>();
        private URI icon;

        public Builder() {
            // Default constructor
        }

        public Builder(InstallationCategoryDTO categoryDTO) {
            this.withId(categoryDTO.getId())
                    .withName(categoryDTO.getName())
                    .withDescription(categoryDTO.getDescription())
                    .withInstallations(categoryDTO.getInstallations())
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

        public Builder withInstallations(List<InstallationDTO> installations) {
            this.installations = installations;
            return this;
        }

        public Builder withIcon(URI iconPath) {
            this.icon = iconPath;
            return this;
        }

        public InstallationCategoryDTO build() {
            return new InstallationCategoryDTO(this);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(name).toString();
    }

}
