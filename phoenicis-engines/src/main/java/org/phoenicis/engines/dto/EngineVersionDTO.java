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

@JsonDeserialize(builder = EngineVersionDTO.Builder.class)
public class EngineVersionDTO {
    private final String version;
    private final String url;
    private final String sha1sum;
    private final String geckoUrl;
    private final String geckoMd5;
    private final String monoUrl;
    private final String monoMd5;
    private final String monoFile;
    private final String geckoFile;

    private EngineVersionDTO(Builder builder) {
        version = builder.version;
        url = builder.url;
        sha1sum = builder.sha1sum;
        geckoUrl = builder.geckoUrl;
        geckoMd5 = builder.geckoMd5;
        monoUrl = builder.monoUrl;
        monoMd5 = builder.monoMd5;
        monoFile = builder.monoFile;
        geckoFile = builder.geckoFile;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public String getSha1sum() {
        return sha1sum;
    }

    public String getGeckoMd5() {
        return geckoMd5;
    }

    public String getGeckoUrl() {
        return geckoUrl;
    }

    public String getMonoMd5() {
        return monoMd5;
    }

    public String getMonoUrl() {
        return monoUrl;
    }

    public String getMonoFile() {
        return monoFile;
    }

    public String getGeckoFile() {
        return geckoFile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(EngineVersionDTO.class).append("version", version).append("url", url)
                .append("sha1sum", sha1sum).append("geckoUrl", geckoUrl).append("geckoMd5", geckoMd5)
                .append("monoUrl", monoUrl).append("monoMd5", monoMd5).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EngineVersionDTO that = (EngineVersionDTO) o;

        return new EqualsBuilder()
                .append(version, that.version)
                .append(url, that.url)
                .append(sha1sum, that.sha1sum)
                .append(geckoUrl, that.geckoUrl)
                .append(geckoMd5, that.geckoMd5)
                .append(monoUrl, that.monoUrl)
                .append(monoMd5, that.monoMd5)
                .append(monoFile, that.monoFile)
                .append(geckoFile, that.geckoFile)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(version)
                .append(url)
                .append(sha1sum)
                .append(geckoUrl)
                .append(geckoMd5)
                .append(monoUrl)
                .append(monoMd5)
                .append(monoFile)
                .append(geckoFile)
                .toHashCode();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String version;
        private String url;
        private String sha1sum;
        private String geckoUrl;
        private String geckoMd5;
        private String monoUrl;
        private String monoMd5;
        private String monoFile;
        private String geckoFile;

        public Builder withGeckoMd5(String geckoMd5) {
            this.geckoMd5 = geckoMd5;
            return this;
        }

        public Builder withGeckoUrl(String geckoUrl) {
            this.geckoUrl = geckoUrl;
            return this;
        }

        public Builder withMonoMd5(String monoMd5) {
            this.monoMd5 = monoMd5;
            return this;
        }

        public Builder withMonoUrl(String monoUrl) {
            this.monoUrl = monoUrl;
            return this;
        }

        public Builder withSha1sum(String sha1sum) {
            this.sha1sum = sha1sum;
            return this;
        }

        public Builder withUrl(String url) {
            this.url = url;
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

        public EngineVersionDTO build() {
            return new EngineVersionDTO(this);
        }

    }
}
