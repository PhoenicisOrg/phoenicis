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

package com.playonlinux.ui.api;

import java.util.Observer;

public interface Filter<DTO> {

    void addObserver(Observer o);

    /**
     * Test the given script against the filter rules.
     * <p>
     * Note: At the moment, this method does not filter categories, since Scripts don't know about their own category.
     * </p>
     *
     * @param dto DTO which should be tested against the filter.
     * @return True if this Script matches the filter criteria, false otherwise.
     */
    boolean apply(DTO dto);

}
