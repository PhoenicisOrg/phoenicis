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

package com.playonlinux.utils.list;

import java.util.Observer;

/**
 * Defines how a list which can be observed for changes should look and behave like.
 *
 * @param <T>
 */
public interface ObservableList<T> extends Iterable<T> {

    void addObserver(Observer o);

    void deleteObserver(Observer o);

    int size();

    /**
     * Get an array containing all items within this observable list.
     *
     * @return An array of items contained within the list.
     */
    T[] toArray();

}
