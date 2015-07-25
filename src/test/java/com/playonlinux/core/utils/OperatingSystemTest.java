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

package com.playonlinux.core.utils;

import com.playonlinux.app.PlayOnLinuxException;
import org.junit.Test;

import static org.junit.Assert.*;


public class OperatingSystemTest {
    @Test
    public void testFromString_generateFromStrings_EnumIsCorrect() throws PlayOnLinuxException {
        OperatingSystem linux = OperatingSystem.fromString("Linux");
        assertEquals(OperatingSystem.LINUX, linux);

        OperatingSystem macosx = OperatingSystem.fromString("Mac OS X");
        assertEquals(OperatingSystem.MACOSX, macosx);

        OperatingSystem freeBSD = OperatingSystem.fromString("FreeBSD");
        assertEquals(OperatingSystem.FREEBSD, freeBSD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromString_generateFromUnknownString_ExceptionIsThrown() {
        OperatingSystem linux = OperatingSystem.fromString("Invalid operating system");
    }

    @Test
    public void testFetchShortName_fetchShortNames_namesAreCorrect() throws PlayOnLinuxException {
        assertEquals("LINUX", OperatingSystem.LINUX.fetchShortName());
        assertEquals("MACOSX", OperatingSystem.MACOSX.fetchShortName());
        assertEquals("FREEBSD", OperatingSystem.FREEBSD.fetchShortName());
    }

    @Test
    public void testFetchCurrentOperationSystem_noErrorsAreThrown() throws PlayOnLinuxException {
        OperatingSystem currentOperatingSystem = OperatingSystem.fetchCurrentOperationSystem();

        assertTrue(currentOperatingSystem == OperatingSystem.LINUX ||
                currentOperatingSystem == OperatingSystem.MACOSX ||
                currentOperatingSystem == OperatingSystem.FREEBSD);
    }

    @Test
    public void testToString_fetchStrings_stringssAreCorrect() throws PlayOnLinuxException {
        assertEquals("Linux", OperatingSystem.LINUX.toString());
        assertEquals("Mac OS X", OperatingSystem.MACOSX.toString());
        assertEquals("FreeBSD", OperatingSystem.FREEBSD.toString());
    }

}