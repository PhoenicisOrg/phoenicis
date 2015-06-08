/*
 * Copyright (C) 2015 Markus Ebner
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

package com.playonlinux.common.list;

import com.playonlinux.common.api.filter.Filter;
import com.playonlinux.common.api.filter.Filterable;

import java.util.ArrayList;
import java.util.List;

public class FilterableList<T> extends ObservableArrayList<T> implements Filterable<T> {

    @Override
    public List<T> getFiltered(Filter<T> filter) {
        List<T> filtered = new ArrayList<>();
        for (T item : this) {
            if (filter.apply(item)) {
                filtered.add(item);
            }
        }
        return filtered;
    }

}
