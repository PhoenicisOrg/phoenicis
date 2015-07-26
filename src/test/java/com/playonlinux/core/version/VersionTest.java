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

package com.playonlinux.core.version;

import org.junit.Test;

import static org.junit.Assert.*;

public class VersionTest {

    @Test
    public void testVersion_toString_givesRightOutput() {
        Version version = new Version("4.1.9");
        assertEquals("4.1.9", version.toString());
    }

    @Test
    public void testVersion_getBig_givesRightOutput() {
        Version version = new Version("4.1.9");
        assertEquals(4, version.getBigNumber());
    }

    @Test
    public void testVersion_getIntermediate_givesRightOutput() {
        Version version = new Version("4.1.9");
        assertEquals(1, version.getIntermediateNumber());
    }

    @Test
    public void testVersion_getLow_givesRightOutput() {
        Version version = new Version("4.1.9");
        assertEquals(9, version.getLowNumber());
    }

    @Test
    public void testEquals_trivialCases() {
        Version version = new Version("4.1.9");
        assertEquals(version, version);
        assertNotEquals(version, null);
        assertNotEquals(version, "");
    }

    @Test
    public void testEquals_SameVersion_versionAreEquals() {
        Version version1 = new Version("4.1.9");
        Version version2 = new Version("4.1.9");

        assertEquals(version1, version2);
    }

    @Test
    public void testEquals_DifferentVersions_versionAreNotEquals() {
        Version version1 = new Version("4.1.9");
        Version version2 = new Version("4.1.8");

        assertNotEquals(version1, version2);
    }

    @Test
    public void testToStringPaddingZero() {
        Version version1 = new Version("4.1.0");
        assertEquals("4.1", version1.toString());
    }

    @Test
    public void testEqualsWithoutLastZero() {
        Version version1 = new Version("4.1.0");
        Version version2 = new Version("4.1");

        assertEquals(version1, version2);
    }

    @Test
    public void testEqualsWithoutLastZeros() {
        Version version1 = new Version("4.0.0");
        Version version2 = new Version("4");

        assertEquals(version1, version2);
    }

    @Test
    public void tesToStringWithCodeName() {
        Version version1 = new Version("4-dev");

        assertEquals("4.0-dev", version1.toString());
    }
}