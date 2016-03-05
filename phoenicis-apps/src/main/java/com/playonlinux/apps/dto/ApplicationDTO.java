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

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.playonlinux.core.dto.DTO;

import lombok.Data;

/**
 * Represents an application
 */
@Data
@JsonDeserialize(builder = ApplicationDTO.Builder.class)
public class ApplicationDTO implements DTO {
    private final int id;
    private final String name;
    private final String description;
    private final String iconUrl;
    private final List<String> miniaturesUrls;
    private final List<ScriptDTO> scripts;

    private ApplicationDTO(Builder builder) {
        id = builder.id;
        name = builder.name;
        description = builder.description;
        iconUrl = builder.iconUrl;
        miniaturesUrls = builder.miniaturesUrls;
        scripts = builder.scripts;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String name;
        private String description;
        private String iconUrl;
        private List<String> miniaturesUrls;
        private List<ScriptDTO> scripts;
        private int id;

        public Builder() {
            // We need a public builder to be able to createPrefix a ScriptDTO
            // from scratch
        }

        public Builder(ApplicationDTO applicationDTO) {
            this.name = applicationDTO.name;
            this.description = applicationDTO.description;
            this.iconUrl = applicationDTO.iconUrl;
            this.miniaturesUrls = applicationDTO.miniaturesUrls;
            this.scripts = applicationDTO.scripts;
            this.id = applicationDTO.id;
        }

        public Builder withId(int id) {
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

        public Builder withIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
            return this;
        }

        public Builder withMiniaturesUrls(List<String> miniaturesUrls) {
            this.miniaturesUrls = miniaturesUrls;
            return this;
        }

        public Builder withScripts(List<ScriptDTO> scriptDTO) {
            this.scripts = scriptDTO;
            return this;
        }

        public ApplicationDTO build() {
            return new ApplicationDTO(this);
        }
    }

}
