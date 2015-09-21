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

package com.playonlinux.engines.wine.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.playonlinux.core.dto.DTO;
import org.apache.commons.lang.builder.ToStringBuilder;

public class WineVersionWebDTO implements DTO {
    private final String version;
    private final String url;
    private final String sha1sum;

    @JsonCreator
    public WineVersionWebDTO(@JsonProperty("version") String version,
                             @JsonProperty("url") String url,
                             @JsonProperty("sha1sum") String sha1sum) {
        this.version = version;
        this.url = url;
        this.sha1sum = sha1sum;
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

    @Override
    public String toString() {
        return new ToStringBuilder(WineVersionWebDTO.class)
                .append("version", version)
                .append("url", url)
                .append("sha1sum", sha1sum).toString();
    }
}
