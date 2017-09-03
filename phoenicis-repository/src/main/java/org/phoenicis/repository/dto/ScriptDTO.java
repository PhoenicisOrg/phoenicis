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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.phoenicis.entities.OperatingSystem;

import java.net.URI;
import java.util.Comparator;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = ScriptDTO.Builder.class)
public class ScriptDTO {
    private final String scriptName;
    private final URI scriptSource;
    private final List<OperatingSystem> compatibleOperatingSystems;
    private final List<OperatingSystem> testingOperatingSystems;
    private final Boolean free;
    private final Boolean requiresPatch;
    private final String script;
    private final URI icon;

    private ScriptDTO(Builder builder) {
        this.scriptName = builder.scriptName;
        this.scriptSource = builder.scriptSource;
        this.compatibleOperatingSystems = builder.compatibleOperatingSystems;
        this.testingOperatingSystems = builder.testingOperatingSystems;
        this.free = builder.free;
        this.requiresPatch = builder.requiresPatch;
        this.script = builder.script;
        this.icon = builder.icon;
    }

    public String getScriptName() {
        return scriptName;
    }

    public URI getScriptSource() {
        return scriptSource;
    }

    public List<OperatingSystem> getCompatibleOperatingSystems() {
        return compatibleOperatingSystems;
    }

    public Boolean isFree() {
        return free;
    }

    public Boolean isRequiresPatch() {
        return requiresPatch;
    }

    public List<OperatingSystem> getTestingOperatingSystems() {
        return testingOperatingSystems;
    }

    public String getScript() {
        return script;
    }

    public URI getIcon() {
        return icon;
    }

    public static Comparator<ScriptDTO> nameComparator() {
        return (o1, o2) -> o1.getScriptName().compareToIgnoreCase(o2.getScriptName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ScriptDTO scriptDTO = (ScriptDTO) o;

        return new EqualsBuilder()
                .append(scriptName, scriptDTO.scriptName)
                .append(scriptSource, scriptDTO.scriptSource)
                .append(compatibleOperatingSystems, scriptDTO.compatibleOperatingSystems)
                .append(testingOperatingSystems, scriptDTO.testingOperatingSystems)
                .append(free, scriptDTO.free)
                .append(requiresPatch, scriptDTO.requiresPatch)
                .append(script, scriptDTO.script)
                .append(icon, scriptDTO.icon)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(scriptName)
                .append(scriptSource)
                .append(compatibleOperatingSystems)
                .append(testingOperatingSystems)
                .append(free).append(requiresPatch)
                .append(script)
                .append(icon)
                .toHashCode();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String scriptName;
        private URI scriptSource;
        private List<OperatingSystem> compatibleOperatingSystems;
        private List<OperatingSystem> testingOperatingSystems;
        private Boolean free;
        private Boolean requiresPatch;
        private String script;
        private URI icon;

        public Builder() {
            // Default constructor
        }

        public Builder(ScriptDTO scriptDTO) {
            this.withScriptName(scriptDTO.getScriptName())
                    .withScript(scriptDTO.getScript())
                    .withCompatibleOperatingSystems(scriptDTO.getCompatibleOperatingSystems())
                    .withTestingOperatingSystems(scriptDTO.getTestingOperatingSystems())
                    .withFree(scriptDTO.isFree())
                    .withRequiresPatch(scriptDTO.isRequiresPatch())
                    .withIcon(scriptDTO.getIcon());
        }

        public Builder withScriptName(String name) {
            this.scriptName = name;
            return this;
        }

        public Builder withScript(String script) {
            this.script = script;
            return this;
        }

        public Builder withScriptSource(URI scriptSource) {
            this.scriptSource = scriptSource;
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

        public Builder withRequiresPatch(Boolean requiresPatch) {
            this.requiresPatch = requiresPatch;
            return this;
        }

        public Builder withIcon(URI icon) {
            this.icon = icon;
            return this;
        }

        public ScriptDTO build() {
            return new ScriptDTO(this);
        }
    }
}
