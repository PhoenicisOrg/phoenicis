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

package com.playonlinux.utils.filter;

import com.playonlinux.utils.list.ObservableList;

import java.util.List;

/**
 * Defines how a filterable list of items should look and behave like.
 *
 * @param <T> Type of the item stored within this filterable list.
 */
public interface Filterable<T> extends ObservableList<T> {

    /**
     * Get an iterable with the items of this filterable that match the given list.
     *
     * @param filter The list to test the items against.
     * @return An iterable over all items within this filterable that matched the given list.
     */
    List<T> getFiltered(Filter<T> filter);

}
