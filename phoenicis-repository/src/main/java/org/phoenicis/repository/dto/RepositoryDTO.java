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
import org.phoenicis.configuration.localisation.Translatable;
import org.phoenicis.configuration.localisation.TranslatableBuilder;
import org.phoenicis.configuration.localisation.Translate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a repository
 */
@JsonDeserialize(builder = RepositoryDTO.Builder.class)
@Translatable
public final class RepositoryDTO {
    private final String name;
    private final List<TypeDTO> types;
    private final TranslationDTO translations;

    private RepositoryDTO(Builder builder) {
        this.name = builder.name;
        this.types = Collections.unmodifiableList(builder.types);
        this.translations = builder.translations;
    }

    public TranslationDTO getTranslations() {
        return translations;
    }

    public String getName() {
        return name;
    }

    @Translate
    public List<TypeDTO> getTypes() {
        return types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RepositoryDTO that = (RepositoryDTO) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(types, that.types)
                .append(translations, that.translations)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(types)
                .append(translations)
                .toHashCode();
    }

    public static Comparator<RepositoryDTO> nameComparator() {
        return (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    @TranslatableBuilder
    public static class Builder {
        private String name;
        private List<TypeDTO> types = new ArrayList<>();
        private TranslationDTO translations = new TranslationDTO.Builder().build();

        public Builder() {
            // Default constructor
        }

        public Builder(RepositoryDTO repositoryDTO) {
            this.withName(repositoryDTO.getName()).withTypes(repositoryDTO.getTypes())
                    .withTranslations(repositoryDTO.getTranslations());
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withTypes(List<TypeDTO> types) {
            this.types = types;
            return this;
        }

        public RepositoryDTO build() {
            return new RepositoryDTO(this);
        }

        public Builder withTranslations(TranslationDTO translations) {
            this.translations = translations;
            return this;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(name).toString();
    }

}
