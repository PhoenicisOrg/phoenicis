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

package org.phoenicis.apps.filter;


import org.phoenicis.apps.dto.ApplicationDTO;

import java.util.HashSet;
import java.util.Set;

public class CombinedAppsFilter implements AppsFilter {
    private final Set<AppsFilter> filters;

    private final AppsSearchFilter searchFilter;

    public CombinedAppsFilter() {
        this.searchFilter = new AppsSearchFilter();
        this.filters = new HashSet<>();
    }

    public void remove(AppsFilter filter) {
        filters.remove(filter);
    }

    public void add(AppsFilter filter) {
        filters.add(filter);
    }

    public void clear() {
        this.filters.clear();
    }

    public boolean applies(ApplicationDTO applicationDTO) {
        boolean result = false;

        if (filters.isEmpty()) {
            result = true;
        }

        for (AppsFilter filter : filters) {
            result |= filter.applies(applicationDTO);
        }

        return result && searchFilter.applies(applicationDTO);
    }

    public void setFilterText(String filterText) {
        this.searchFilter.setFilter(filterText);
    }
}
