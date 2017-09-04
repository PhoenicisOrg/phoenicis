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

package org.phoenicis.engines.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.phoenicis.configuration.localisation.Translatable;
import org.phoenicis.configuration.localisation.TranslatableBuilder;
import org.phoenicis.configuration.localisation.Translate;

/**
 * DTO to describe an engine tool which can be run from the "Engine tools" tab in "Containers"
 */
@JsonDeserialize(builder = EngineToolDTO.Builder.class)
@Translatable
public class EngineToolDTO {
    private final String id;
    private final String name;
    // currently: CSS class
    // TODO: change to URI
    private final String miniature;

    private EngineToolDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.miniature = builder.miniature;
    }

    public String getId() {
        return this.id;
    }

    @Translate
    public String getName() {
        return this.name;
    }

    public String getMiniature() {
        return this.miniature;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(EngineToolDTO.class)
                .append("id", this.id)
                .append("name", this.name)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EngineToolDTO engineToolDTO = (EngineToolDTO) o;

        return new EqualsBuilder()
                .append(this.id, engineToolDTO.id)
                .append(this.name, engineToolDTO.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(this.id)
                .append(this.name)
                .toHashCode();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    @TranslatableBuilder
    public static class Builder {
        private String id;
        private String name;
        private String miniature;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withMiniature(String miniature) {
            this.miniature = miniature;
            return this;
        }

        public EngineToolDTO build() {
            return new EngineToolDTO(this);
        }

    }
}
