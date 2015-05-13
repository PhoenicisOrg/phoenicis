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

package com.playonlinux.webservice;

import org.springframework.web.client.RestTemplate;
import com.playonlinux.common.dtos.AvailableCategoriesDTO;

import java.util.Observable;

/**
 * This class download scripts from a playonlinux web service and converts it into DTOs
 */
public class RemoteScripts extends Observable {
    private final String url;
    private AvailableCategoriesDTO categories;

    RemoteScripts(String url) {
        this.url = url;
    }

    void fetchCategories() {
        this.categories = new RestTemplate().getForObject(this.url, AvailableCategoriesDTO.class);
        this.setChanged();
        this.notifyObservers(categories);
    }
}
