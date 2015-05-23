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

package com.playonlinux.common.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Comparator;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScriptDTO implements AbstractDTO  {

    private int id;
    private String name;
    private String description;
    private String iconUrl;
    private List<String> miniaturesUrls;
    private ScriptInformationsDTO scriptInformations;

    public ScriptDTO() {
        // Kept for the webservice
    }
    private ScriptDTO(Builder builder) {
        id = builder.id;
        name = builder.name;
        description = builder.description;
        iconUrl = builder.iconURL;
        miniaturesUrls = builder.miniaturesUrls;
        scriptInformations = builder.scriptInformations;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public List<String> getMiniaturesUrls() {
        return miniaturesUrls;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ScriptInformationsDTO getScriptInformations() {
        return scriptInformations;
    }

    public static class AlphabeticalOrderComparator implements Comparator<ScriptDTO> {
        @Override
        public int compare(ScriptDTO script1, ScriptDTO script2) {
            return script1.getName().compareTo(script2.getName());
        }
    }

    public static class Builder {
        private String name;
        private String description;
        private String iconURL;
        private List<String> miniaturesUrls;
        private ScriptInformationsDTO scriptInformations;
        private int id;

        public Builder() {
            // We need a public builder to be able to create a ScriptDTO from scratch
        }

        public Builder(ScriptDTO scriptDTO) {
            this.name = scriptDTO.name;
            this.description = scriptDTO.description;
            this.iconURL = scriptDTO.iconUrl;
            this.miniaturesUrls = scriptDTO.miniaturesUrls;
            this.scriptInformations = scriptDTO.scriptInformations;
            this.id = scriptDTO.id;
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

        public Builder withIconURL(String iconURL) {
            this.iconURL = iconURL;
            return this;
        }

        public Builder withMiniaturesUrls(List<String> miniaturesUrls) {
            this.miniaturesUrls = miniaturesUrls;
            return this;
        }

        public Builder withScriptInformations(ScriptInformationsDTO scriptInformations) {
            this.scriptInformations = scriptInformations;
            return this;
        }

        public ScriptDTO build() {
            return new ScriptDTO(this);
        }
    }


}
