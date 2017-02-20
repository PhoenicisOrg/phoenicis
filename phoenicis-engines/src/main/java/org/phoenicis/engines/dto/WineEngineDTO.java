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

@JsonDeserialize(builder = WineEngineDTO.Builder.class)
public class WineEngineDTO {
    private final String architecture;
    private final String distribution;
    private final String version;
    private final String monoFile;
    private final String geckoFile;

    private WineEngineDTO(Builder builder) {
        architecture = builder.architecture;
        distribution = builder.distribution;
        version = builder.version;
        monoFile = builder.monoFile;
        geckoFile = builder.geckoFile;
    }

    public String getArchitecture() {
        return architecture;
    }

    public String getDistribution() {
        return distribution;
    }

    public String getVersion() {
        return version;
    }

    public String getMonoFile() { return monoFile; }

    public String getGeckoFile() { return geckoFile; }

    @Override
    public String toString() {
        return new ToStringBuilder(WineEngineDTO.class)
                .append("architecture", architecture)
                .append("distribution", distribution)
                .append("version", version)
                .append("mono", monoFile)
                .append("gecko", geckoFile)
                .toString();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String architecture;
        private String distribution;
        private String version;
        private String monoFile;
        private String geckoFile;

        public Builder withArchitecture(String architecture) {
            this.architecture = architecture;
            return this;
        }

        public Builder withDistribution(String distribution) {
            this.distribution = distribution;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder withMonoFile(String monoFile) {
            this.monoFile = monoFile;
            return this;
        }

        public Builder withGeckoFile(String geckoFile) {
            this.geckoFile = geckoFile;
            return this;
        }

        public WineEngineDTO build() {
            return new WineEngineDTO(this);
        }

    }
}
