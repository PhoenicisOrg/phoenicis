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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenicis.configuration.localisation.Translatable;
import org.phoenicis.configuration.localisation.TranslatableBuilder;
import org.phoenicis.configuration.localisation.Translate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phoenicis.configuration.localisation.Translate;
import org.phoenicis.configuration.localisation.TranslatableBuilder;

import java.net.URI;
import java.util.*;

/**
 * Represents an application
 */
@JsonDeserialize(builder = ApplicationDTO.Builder.class)
@Translatable
public class ApplicationDTO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationDTO.class);
    private final String id;
    private final String name;
    private final String description;
    private final URI icon;
    private final List<URI> miniatures;
    private final List<ScriptDTO> scripts;
    private final List<ResourceDTO> resources;

    private ApplicationDTO(Builder builder) {
        if (builder.id != null) {
            if (builder.id.matches("^[a-zA-Z0-9]+$")) {
                this.id = builder.id;
            } else {
                LOGGER.warn(String.format("Application ID (%s) contains invalid characters, will remove them.",
                        builder.id));
                this.id = builder.id.replaceAll("[^a-zA-Z0-9]", "");
            }
        } else {
            this.id = null;
        }

        this.name = builder.name == null ? builder.id : builder.name;
        this.description = builder.description;
        this.icon = builder.icon;
        this.miniatures = builder.miniatures;
        this.scripts = builder.scripts;
        this.resources = builder.resources;
    }

    public static Comparator<ApplicationDTO> nameComparator() {
        return (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }

    public List<ResourceDTO> getResources() {
        return resources;
    }

    public URI getIcon() {
        return icon;
    }

    public List<URI> getMiniatures() {
        return miniatures;
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

    public List<ScriptDTO> getScripts() {
        return scripts;
    }

    /**
     * Returns the main miniature belonging to this {@link ApplicationDTO}.
     * The main miniature is the miniature with the file name <code>main.png</code>.
     * If no such miniature exists the first miniature of this {@link ApplicationDTO} is returned.
     * If this {@link ApplicationDTO} contains no miniatures {@link Optional#empty()} is returned.
     *
     * @return An optional with the found URI, or {@link Optional#empty()} if no miniature exists
     */
    @JsonIgnore
    public Optional<URI> getMainMiniature() {
        Optional<URI> result = this.miniatures.stream().filter(uri -> uri.getPath().endsWith("main.png")).findFirst();

        /*
         * Fallback in case no main miniature has been selected but the list contains at least one other miniature
         */
        if (!result.isPresent()) {
            result = this.miniatures.stream().findFirst();
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApplicationDTO that = (ApplicationDTO) o;

        return new EqualsBuilder().append(id, that.id).append(name, that.name).append(description, that.description)
                .append(icon, that.icon).append(miniatures, that.miniatures).append(scripts, that.scripts)
                .append(resources, that.resources).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(description).append(icon).append(miniatures)
                .append(scripts).append(resources).toHashCode();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    @TranslatableBuilder
    public static class Builder {
        private String id;
        private String name;
        private String description;
        private URI icon;
        private List<URI> miniatures = new ArrayList<>();
        private List<ScriptDTO> scripts = new ArrayList<>();
        private List<ResourceDTO> resources = new ArrayList<>();

        public Builder() {
            // Default constructor
        }

        public Builder(ApplicationDTO applicationDTO) {
            this.id = applicationDTO.id;
            this.name = applicationDTO.name;
            this.description = applicationDTO.description;
            this.icon = applicationDTO.icon;
            this.miniatures = applicationDTO.miniatures;
            this.scripts = applicationDTO.scripts;
            this.resources = applicationDTO.resources;
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

        public Builder withResources(List<ResourceDTO> resources) {
            this.resources = Collections.unmodifiableList(resources);
            return this;
        }

        public Builder withIcon(URI icon) {
            this.icon = icon;
            return this;
        }

        public Builder withMiniatures(List<URI> miniatures) {
            this.miniatures = miniatures;
            return this;
        }

        public Builder withScripts(List<ScriptDTO> scriptDTO) {
            this.scripts = scriptDTO;
            return this;
        }

        public ApplicationDTO build() {
            return new ApplicationDTO(this);
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

}
