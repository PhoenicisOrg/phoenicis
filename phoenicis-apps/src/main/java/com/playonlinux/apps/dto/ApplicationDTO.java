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

package com.playonlinux.apps.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents an application
 */
@JsonDeserialize(builder = ApplicationDTO.Builder.class)
public class ApplicationDTO {
    private final String name;
    private final String description;
    private final byte[] icon;
    private final List<byte[]> miniatures;
    private final List<ScriptDTO> scripts;
    private final List<ResourceDTO> resources;

    private ApplicationDTO(Builder builder) {
        name = builder.name;
        description = builder.description;
        icon = builder.icon;
        miniatures = builder.miniatures;
        scripts = builder.scripts;
        resources = builder.resources;
    }

    public List<ResourceDTO> getResources() {
        return resources;
    }

    public byte[] getIcon() {
        return icon;
    }

    public List<byte[]> getMiniatures() {
        return miniatures;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ScriptDTO> getScripts() {
        return scripts;
    }

    public static Comparator<ApplicationDTO> nameComparator() {
        return (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String name;
        private String description;
        private byte[] icon;
        private List<byte[]> miniatures = new ArrayList<>();
        private List<ScriptDTO> scripts = new ArrayList<>();
        private List<ResourceDTO> resources = new ArrayList<>();

        public Builder() {
            // Default constructor
        }

        public Builder(ApplicationDTO applicationDTO) {
            this.name = applicationDTO.name;
            this.description = applicationDTO.description;
            this.icon = applicationDTO.icon;
            this.miniatures = applicationDTO.miniatures;
            this.scripts = applicationDTO.scripts;
            this.resources = applicationDTO.resources;
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

        public Builder withIcon(byte[] icon) {
            this.icon = icon;
            return this;
        }

        public Builder withMiniatures(List<byte[]> miniatures) {
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


        public String getName() {
            return name;
        }
    }

}
