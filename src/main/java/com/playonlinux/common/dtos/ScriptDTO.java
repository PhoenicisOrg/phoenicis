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
public class ScriptDTO {

    int id;
    String name;
    String description;

    public String getIconUrl() {
        return iconUrl;
    }

    public List<String> getMiniaturesUrls() {
        return miniaturesUrls;
    }

    String iconUrl;
    List<String> miniaturesUrls;
    ScriptInformationsDTO scriptInformations;


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
}
