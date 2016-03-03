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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.playonlinux.core.dto.DTO;
import com.playonlinux.core.utils.OperatingSystem;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ScriptDTO.Builder.class)
public class ScriptDTO implements DTO {
    private final int id;
    private final String scriptName;
    private final List<OperatingSystem> compatibleOperatingSystems;
    private final List<OperatingSystem> testingOperatingSystems;
    private final Boolean free;
    private final Boolean requiresNoCD;
    private final String url;

    private ScriptDTO(Builder builder) {
        this.id = builder.id;
        this.scriptName = builder.scriptName;
        this.compatibleOperatingSystems = builder.compatibleOperatingSystems;
        this.testingOperatingSystems = builder.testingOperatingSystems;
        this.free = builder.free;
        this.requiresNoCD = builder.requiresNoCD;
        this.url = builder.url;
    }

    public int getId() {
        return id;
    }

    public String getScriptName() {
        return scriptName;
    }

    public String getName() {
        return scriptName;
    }

    public List<OperatingSystem> getCompatibleOperatingSystems() {
        return compatibleOperatingSystems;
    }

    public Boolean isFree() {
        return free;
    }

    public Boolean isRequiresNoCD() {
        return requiresNoCD;
    }

    public List<OperatingSystem> getTestingOperatingSystems() {
        return testingOperatingSystems;
    }

    public String getUrl() {
        return url;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private int id;
        private String scriptName;
        private List<OperatingSystem> compatibleOperatingSystems;
        private List<OperatingSystem> testingOperatingSystems;
        private Boolean free;
        private Boolean requiresNoCD;
        private String url;

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withScriptName(String name) {
            this.scriptName = name;
            return this;
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withCompatibleOperatingSystems(List<OperatingSystem> compatibleOperatingSystems) {
            this.compatibleOperatingSystems = compatibleOperatingSystems;
            return this;
        }

        public Builder withTestingOperatingSystems(List<OperatingSystem> testingOperatingSystems) {
            this.testingOperatingSystems = testingOperatingSystems;
            return this;
        }

        public Builder withFree(Boolean free) {
            this.free = free;
            return this;
        }

        public Builder withRequiresNoCD(Boolean requiresNoCD) {
            this.requiresNoCD = requiresNoCD;
            return this;
        }

        public ScriptDTO build() {
            return new ScriptDTO(this);
        }
    }
}
