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

package com.playonlinux.dto.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.playonlinux.dto.AbstractDTO;
import com.playonlinux.utils.OperatingSystem;
import com.playonlinux.utils.comparator.Nameable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScriptDTO implements AbstractDTO, Nameable {
    private int id;
    private String name;
    private List<OperatingSystem> compatiblesOperatingSystems;
    private List<OperatingSystem> testingOperatingSystems;
    private Boolean free;
    private Boolean requiresNoCD;

    public ScriptDTO() {
        // Kept for the webservice
    }

    private ScriptDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.compatiblesOperatingSystems = builder.compatiblesOperatingSystems;
        this.testingOperatingSystems = builder.testingOperatingSystems;
        this.free = builder.free;
        this.requiresNoCD = builder.requiresNoCD;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<OperatingSystem> getCompatiblesOperatingSystems() {
        return compatiblesOperatingSystems;
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


    public static class Builder {
        private int id;
        private String name;
        private List<OperatingSystem> compatiblesOperatingSystems;
        private List<OperatingSystem> testingOperatingSystems;
        private Boolean free;
        private Boolean requiresNoCD;

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCompatibleOperatingSystems(List<OperatingSystem> compatiblesOperatingSystems) {
            this.compatiblesOperatingSystems = compatiblesOperatingSystems;
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

        public Builder withRequiresNoCd(Boolean requiresNoCD) {
            this.requiresNoCD = requiresNoCD;
            return this;
        }

        public ScriptDTO build() {
            return new ScriptDTO(this);
        }
    }
}
