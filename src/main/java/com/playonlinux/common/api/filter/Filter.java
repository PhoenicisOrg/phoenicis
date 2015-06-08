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

package com.playonlinux.common.api.filter;

import java.util.Observer;

/**
 * Defines how a list (used in filterables) should look and behave like.
 *
 * @param <T> Type of the item stored within the filterable list.
 */
public interface Filter<T> {

    void addObserver(Observer o);

    void deleteObserver(Observer o);

    /**
     * Test the given item against the list rules defined within this list.
     *
     * @param item Item to test against the list rules.
     * @return {@code true} if the given item matches the list rules, {@code false} otherwise.
     */
    boolean apply(T item);

}
