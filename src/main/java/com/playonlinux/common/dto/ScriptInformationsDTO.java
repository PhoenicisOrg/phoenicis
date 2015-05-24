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

package com.playonlinux.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.playonlinux.common.api.dto.AbstractDTO;
import com.playonlinux.utils.OperatingSystem;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScriptInformationsDTO implements AbstractDTO {
    private List<OperatingSystem> compatiblesOperatingSystems;
    private List<OperatingSystem> testingOperatingSystems;
    private Boolean free;
    private Boolean requiresNoCD;

    public ScriptInformationsDTO() {
        // Needed for the webservice class
    }

    public ScriptInformationsDTO(Builder builder) {
        compatiblesOperatingSystems = builder.compatibleOperatingSystems;
        testingOperatingSystems = builder.compatibleOperatingSystems;
        free = builder.free;
        requiresNoCD = builder.requiresNoCd;
    }

    public List<OperatingSystem> getCompatiblesOperatingSystems() {
        return compatiblesOperatingSystems;
    }

    public List<OperatingSystem> getTestingOperatingSystems() {
        return testingOperatingSystems;
    }

    public Boolean isFree() {
        return free;
    }

    public Boolean isRequiresNoCD() {
        return requiresNoCD;
    }


    public static class Builder {
        private List<OperatingSystem> compatibleOperatingSystems;
        private List<OperatingSystem> testingOperatingSystems;
        private Boolean free;
        private Boolean requiresNoCd;

        public Builder() {
            // Empty default constructor to start from scratch
        }

        public Builder(ScriptInformationsDTO scriptInformationsDTO) {
            this.compatibleOperatingSystems = scriptInformationsDTO.compatiblesOperatingSystems;
            this.testingOperatingSystems = scriptInformationsDTO.testingOperatingSystems;
            this.free = scriptInformationsDTO.free;
            this.requiresNoCd = scriptInformationsDTO.requiresNoCD;
        }

        public Builder withCompatibleOperatingSystems(List<OperatingSystem> compatibleOperatingSystems) {
            this.compatibleOperatingSystems = compatibleOperatingSystems;
            return this;
        }

        public Builder withTestingOperatingSystems(List<OperatingSystem> testingOperatingSystems) {
            this.testingOperatingSystems = testingOperatingSystems;
            return this;
        }

        public Builder withFree(boolean free) {
            this.free = free;
            return this;
        }

        public Builder withRequiresNoCd(boolean requiresNoCd) {
            this.requiresNoCd = requiresNoCd;
            return this;
        }

        public ScriptInformationsDTO build() {
            return new ScriptInformationsDTO(this);
        }
    }

}
