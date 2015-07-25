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

package com.playonlinux.apps.entities;

import com.playonlinux.core.entities.Entity;
import com.playonlinux.core.comparator.Nameable;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AppsItemScriptEntity implements Entity, Nameable {

    private final int id;
    private final String name;

    private AppsItemScriptEntity(Builder builder) {
        id = builder.id;
        name = builder.name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this.getClass().getName())
                .append(this.id)
                .append(this.name).toString();
    }

    public static class Builder {
        private String name;
        private int id;

        public Builder() {
            // We need a public builder to be able to create a ScriptDTO from scratch
        }

        public Builder(AppsItemScriptEntity scriptDTO) {
            this.name = scriptDTO.name;
            this.id = scriptDTO.id;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public AppsItemScriptEntity build() {
            return new AppsItemScriptEntity(this);
        }
    }
}
