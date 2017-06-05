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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.*;

/**
 * Represents a translation file
 */
@JsonDeserialize(builder = TranslationDTO.Builder.class)
public class TranslationDTO {
    private final String language;
    private final String json;

    private TranslationDTO(Builder builder) {
        this.language = builder.language;
        this.json = builder.json;
    }

    public String getLanguage() {
        return language;
    }

    public String getJson() {
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranslationDTO that = (TranslationDTO) o;

        return new EqualsBuilder().append(language, that.language).append(json, that.json).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(language).append(json).toHashCode();
    }

    public static Comparator<TranslationDTO> nameComparator() {
        return (o1, o2) -> o1.getLanguage().compareToIgnoreCase(o2.getLanguage());
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String language;
        private String json;

        public Builder() {
            // Default constructor
        }

        public Builder(TranslationDTO repositoryDTO) {
            this.withLanguage(repositoryDTO.getLanguage()).withJson(repositoryDTO.getJson());
        }

        public Builder withLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder withJson(String json) {
            this.json = json;
            return this;
        }

        public TranslationDTO build() {
            return new TranslationDTO(this);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(language).toString();
    }

}
