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
import javafx.scene.Node;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Comparator;

/**
 * Represents an installation
 * e.g. of an app or an engine
 */
@JsonDeserialize(builder = InstallationDTO.Builder.class)
public class InstallationDTO {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstallationDTO.class);

    private final String id;
    private final String name;
    private final InstallationType category;
    private final String description;
    private final URI miniature;
    private final Node node;

    private InstallationDTO(Builder builder) {
        if (builder.id != null) {
            if (builder.id.matches("^[a-zA-Z0-9]+$")) {
                this.id = builder.id;
            } else {
                LOGGER.warn(
                        String.format("Installation ID (%s) contains invalid characters, will remove them.",
                                builder.id));
                this.id = builder.id.replaceAll("[^a-zA-Z0-9_]", "");
            }
        } else {
            this.id = null;
        }

        name = builder.name;
        category = builder.category;
        description = builder.description;
        miniature = builder.miniature;
        node = builder.node;
    }

    /**
     * type of the installation
     */
    public enum InstallationType {
        APPS("Apps"), ENGINES("Engines");

        private String displayName;

        InstallationType(String displayName) {
            this.displayName = displayName;
        }

        public String toString() {
            return this.displayName;
        }
    }

    public String getId() {
        return id;
    }

    public URI getMiniature() {
        return miniature;
    }

    public String getName() {
        return name;
    }

    public InstallationType getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Node getNode() {
        return node;
    }

    public static Comparator<InstallationDTO> nameComparator() {
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

        InstallationDTO that = (InstallationDTO) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(name, that.name)
                .append(category, that.category)
                .append(description, that.description)
                .append(miniature, that.miniature)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(category)
                .append(description)
                .append(miniature)
                .append(node)
                .toHashCode();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String id;
        private String name;
        private InstallationType category;
        private String description;
        private URI miniature;
        private Node node;

        public Builder() {
            // Default constructor
        }

        public Builder(InstallationDTO installationDTO) {
            this.id = installationDTO.id;
            this.name = installationDTO.name;
            this.category = installationDTO.category;
            this.description = installationDTO.description;
            this.miniature = installationDTO.miniature;
            this.node = installationDTO.node;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCategory(InstallationType category) {
            this.category = category;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withMiniature(URI miniature) {
            this.miniature = miniature;
            return this;
        }

        public Builder withNode(Node node) {
            this.node = node;
            return this;
        }

        public InstallationDTO build() {
            return new InstallationDTO(this);
        }

        public String getName() {
            return name;
        }
    }

}
