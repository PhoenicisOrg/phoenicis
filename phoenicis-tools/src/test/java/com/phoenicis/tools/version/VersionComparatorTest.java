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

package com.phoenicis.tools.version;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VersionComparatorTest {
    VersionComparator versionComparator;

    @Before
    public void setUp() {
        versionComparator = new VersionComparator();
    }

    @Test
    public void testCompare_equalsVersion() {
        assertEquals(0,
                versionComparator.compare(
                        new Version("4.2.1"),
                        new Version("4.2.1")
                )
        );
    }

    @Test
    public void testCompare_HigherVersion() {
        assertTrue(
                versionComparator.compare(
                        new Version("4.2.2"),
                        new Version("4.2.1")
                ) > 0
        );

        assertTrue(
                versionComparator.compare(
                        new Version("4.3.1"),
                        new Version("4.2.1")
                ) > 0
        );

        assertTrue(
                versionComparator.compare(
                        new Version("5.2.1"),
                        new Version("4.2.1")
                ) > 0
        );

        assertTrue(
                versionComparator.compare(
                        new Version("4.2.2"),
                        new Version("4.1.3")
                ) > 0
        );
    }

    @Test
    public void testCompare_WithManyPatches() {
        assertTrue(
                versionComparator.compare(
                        new Version("4.2.1-dev-patch2"),
                        new Version("4.2.1")
                ) > 0
        );

        assertTrue(
                versionComparator.compare(
                        new Version("4.2.1"),
                        new Version("4.2.1-dev-patch2")
                ) < 0
        );
    }

    @Test
    public void testCompare_invalidVersion() {
        assertTrue(
                versionComparator.compare(
                        new Version("1.4.rc6-xliveless-no_xinput2"),
                        new Version("1.4")
                ) > 0
        );

        assertTrue(
                versionComparator.compare(
                        new Version("1.4"),
                        new Version("1.4.rc6-xliveless-no_xinput2")
                ) < 0
        );
    }

    @Test
    public void testCompare_WithPatches() {
        assertTrue(
                versionComparator.compare(
                        new Version("4.2.1-dev"),
                        new Version("4.2.1")
                ) > 0
        );

        assertTrue(
                versionComparator.compare(
                        new Version("4.2.1"),
                        new Version("4.2.1-dev")
                ) < 0
        );
    }

    @Test
    public void testCompare_WithPatchesOnlyTwo() {
        assertTrue(
                versionComparator.compare(
                        new Version("4.2-dev"),
                        new Version("4.2")
                ) > 0
        );

        assertTrue(
                versionComparator.compare(
                        new Version("4.2"),
                        new Version("4.2-rc6")
                ) < 0
        );
    }

    @Test
    public void testCompare_LowerVersion() {
        assertTrue(
                versionComparator.compare(
                        new Version("4.2.1"),
                        new Version("4.2.2")
                ) < 0
        );

        assertTrue(
                versionComparator.compare(
                        new Version("4.2.1"),
                        new Version("4.3.1")
                ) < 0
        );

        assertTrue(
                versionComparator.compare(
                        new Version("4.2.1"),
                        new Version("5.2.1")
                ) < 0
        );

        assertTrue(
                versionComparator.compare(
                        new Version("4.1.3"),
                        new Version("4.2.2")
                ) < 0
        );
    }




}