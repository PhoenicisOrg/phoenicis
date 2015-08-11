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

package com.playonlinux.core.comparator;

import org.junit.Test;

import static org.junit.Assert.*;

public class AlphabeticalOrderComparatorTest {

    @Test
    public void testCompare_equals() throws Exception {
        final Nameable namable1 = () -> "a";
        final Nameable namable2 = () -> "a";

        assertEquals(0, new AlphabeticalOrderComparator<>().compare(namable1, namable2));
    }

    @Test
    public void testCompare_inferior() throws Exception {
        final Nameable namable1 = () -> "a";
        final Nameable namable2 = () -> "b";

        assertEquals(-1, new AlphabeticalOrderComparator<>().compare(namable1, namable2));
    }

    @Test
    public void testCompare_after() throws Exception {
        final Nameable namable1 = () -> "b";
        final Nameable namable2 = () -> "a";

        assertEquals(1, new AlphabeticalOrderComparator<>().compare(namable1, namable2));
    }
}