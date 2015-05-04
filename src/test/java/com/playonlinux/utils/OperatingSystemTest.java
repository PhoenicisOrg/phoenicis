package com.playonlinux.utils;

import com.playonlinux.domain.PlayOnLinuxError;
import org.junit.Test;

import static org.junit.Assert.*;


public class OperatingSystemTest {
    @Test
    public void testFromString_generateFromStrings_EnumIsCorrect() throws PlayOnLinuxError {
        OperatingSystem linux = OperatingSystem.fromString("Linux");
        assertEquals(OperatingSystem.LINUX, linux);

        OperatingSystem macosx = OperatingSystem.fromString("Mac OS X");
        assertEquals(OperatingSystem.MACOSX, macosx);

        OperatingSystem freeBSD = OperatingSystem.fromString("FreeBSD");
        assertEquals(OperatingSystem.FREEBSD, freeBSD);
    }

    @Test(expected = PlayOnLinuxError.class)
    public void testFromString_generateFromUnknownString_ExceptionIsThrown() throws PlayOnLinuxError {
        OperatingSystem linux = OperatingSystem.fromString("Invalid operating system");
    }

    @Test
    public void testFetchShortName_fetchShortNames_namesAreCorrect() throws PlayOnLinuxError {
        assertEquals("LINUX", OperatingSystem.LINUX.fetchShortName());
        assertEquals("MACOSX", OperatingSystem.MACOSX.fetchShortName());
        assertEquals("FREEBSD", OperatingSystem.FREEBSD.fetchShortName());
    }

    @Test
    public void testFetchCurrentOperationSystem_noErrorsAreThrown() throws PlayOnLinuxError {
        OperatingSystem currentOperatingSystem = OperatingSystem.fetchCurrentOperationSystem();

        assertTrue(currentOperatingSystem == OperatingSystem.LINUX ||
                currentOperatingSystem == OperatingSystem.MACOSX ||
                currentOperatingSystem == OperatingSystem.FREEBSD);
    }

    @Test
    public void testToString_fetchStrings_stringssAreCorrect() throws PlayOnLinuxError {
        assertEquals("Linux", OperatingSystem.LINUX.toString());
        assertEquals("Mac OS X", OperatingSystem.MACOSX.toString());
        assertEquals("FreeBSD", OperatingSystem.FREEBSD.toString());
    }

}