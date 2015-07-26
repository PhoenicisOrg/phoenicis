/*
 * Copyright (C) 2015 Kaspar Tint
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
package com.playonlinux.library;

import com.playonlinux.core.filter.Filter;
import com.playonlinux.core.observer.ObservableDefaultImplementation;
import com.playonlinux.library.dto.InstalledApplicationDTO;
import org.apache.commons.lang.StringUtils;

/**
 * Filter for installed applications in the MainWindow
 */
public class LibraryFilter extends ObservableDefaultImplementation implements Filter<InstalledApplicationDTO> {

    private final String name;

    public LibraryFilter(String name) {
        this.name = name;
    }

    @Override
    public boolean apply(InstalledApplicationDTO item) {
        // We want to return the whole list for empty search string. Otherwise compare strings.
        return !StringUtils.isNotBlank(name) || item.getName().toLowerCase().contains(name);
    }


}
