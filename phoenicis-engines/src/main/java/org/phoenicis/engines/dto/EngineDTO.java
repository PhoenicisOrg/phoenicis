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
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

@JsonDeserialize(builder = EngineDTO.Builder.class)
public class EngineDTO {
    private final String category;
    private final String subCategory;
    private final String version;
    private final Map<String, String> userData;

    private EngineDTO(Builder builder) {
        category = builder.category;
        subCategory = builder.subCategory;
        version = builder.version;
        userData = builder.userData;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getUserData() { return userData; }

    @Override
    public String toString() {
        return new ToStringBuilder(EngineDTO.class)
                .append("category", category)
                .append("subCategory", subCategory)
                .append("version", version)
                .toString();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String category;
        private String subCategory;
        private String version;
        private Map<String, String> userData;

        public Builder withCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder withSubCategory(String distribution) {
            this.subCategory = distribution;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder withUserData(Map<String, String> userData) {
            this.userData = userData;
            return this;
        }

        public EngineDTO build() {
            return new EngineDTO(this);
        }

    }
}
