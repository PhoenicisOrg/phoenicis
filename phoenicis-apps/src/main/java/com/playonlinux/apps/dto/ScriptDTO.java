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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.phoenicis.entities.OperatingSystem;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ScriptDTO.Builder.class)
public class ScriptDTO {
    private final String scriptName;
    private final List<OperatingSystem> compatibleOperatingSystems;
    private final List<OperatingSystem> testingOperatingSystems;
    private final Boolean free;
    private final Boolean requiresNoCD;
    private final String script;

    private ScriptDTO(Builder builder) {
        this.scriptName = builder.scriptName;
        this.compatibleOperatingSystems = builder.compatibleOperatingSystems;
        this.testingOperatingSystems = builder.testingOperatingSystems;
        this.free = builder.free;
        this.requiresNoCD = builder.requiresNoCD;
        this.script = builder.script;
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

    public String getScript() {
        return script;
    }


    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String scriptName;
        private List<OperatingSystem> compatibleOperatingSystems;
        private List<OperatingSystem> testingOperatingSystems;
        private Boolean free;
        private Boolean requiresNoCD;
        private String script;

        public Builder() {
            // Default constructor
        }

        public Builder(ScriptDTO scriptDTO) {
            this.withScriptName(scriptDTO.getName())
                    .withScript(scriptDTO.getScript())
                    .withCompatibleOperatingSystems(scriptDTO.getCompatibleOperatingSystems())
                    .withTestingOperatingSystems(scriptDTO.getTestingOperatingSystems())
                    .withFree(scriptDTO.isFree())
                    .withRequiresNoCD(scriptDTO.requiresNoCD);
        }

        public Builder withScriptName(String name) {
            this.scriptName = name;
            return this;
        }

        public Builder withScript(String script) {
            this.script = script;
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

        public String getScriptName() {
            return scriptName;
        }
    }
}
