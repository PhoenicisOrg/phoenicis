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

package com.playonlinux.win32.pe;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PEReaderTest {
    private PEReader peReader = new PEReader();
    private PEFile peFile;

    @Before
    public void setUp() throws IOException {
        peFile = peReader.parseExecutable(this.getClass().getResourceAsStream("winecfg.exe"));
    }

    @Test
    public void testMagicNumber() {
        assertEquals(0x5A4D, peFile.imageDOSHeader.e_magic.get());
    }

    @Test
    public void testPeHeaderSignature() {
        assertEquals(0x4550, peFile.imageNTHeaders.signature.get());
    }

    @Test
    public void testMachine() {
        assertEquals(0x14C, peFile.imageNTHeaders.fileHeader.machine.get());
    }

    @Test
    public void testNumberOfSections() {
        assertEquals(3, peFile.imageNTHeaders.fileHeader.numberOfSections.get());
    }

    @Test
    public void testNumberOfSymbols() {
        assertEquals(0, peFile.imageNTHeaders.fileHeader.numberOfSymbols.get());
    }

    @Test
    public void testSizeOfOptionalHeader() {
        assertEquals(224, peFile.imageNTHeaders.fileHeader.sizeOfOptionalHeader.get());
    }

    @Test
    public void testCharacteristics() {
        assertEquals(0x102, peFile.imageNTHeaders.fileHeader.characteristics.get());
    }

    @Test
    public void testOptionalHeaderMagic() {
        assertEquals(0x10B, peFile.imageNTHeaders.optionalHeader.magic.get());
    }

    @Test
    public void testOptionalHeaderLinkerVersion() {
        assertEquals(0, peFile.imageNTHeaders.optionalHeader.minorLinkerVersion.intValue());
        assertEquals(0, peFile.imageNTHeaders.optionalHeader.minorLinkerVersion.intValue());
    }

    @Test
    public void testOptionalHeaderSizeOfCode() {
        assertEquals(8, peFile.imageNTHeaders.optionalHeader.sizeOfCode.get());
    }

    @Test
    public void testOptionalHeaderSizeOfHeaders() {
        assertEquals(512, peFile.imageNTHeaders.optionalHeader.sizeOfHeaders.getUnsignedValue());
    }

    @Test
    public void testOptionalHeaderSubsystem() {
        assertEquals(2, peFile.imageNTHeaders.optionalHeader.subsystem.getUnsignedValue());
    }

    @Test
    public void testOptionalHeaderDllCharacteristics() {
        assertEquals(0x100, peFile.imageNTHeaders.optionalHeader.dllCharacteristics.get());
    }

    @Test
    public void testNumberOfDataDirectoryInOptionalHeader() {
        assertEquals(16, peFile.imageNTHeaders.optionalHeader.dataDirectory.length);
    }

    @Test
    public void testRealModeStubProgramSize() {
        assertEquals(96, peFile.imageDOSHeader.e_lfanew.intValue());
    }


    @Test
    public void testRealModeStubProgram() {
        assertArrayEquals(java.util.Arrays.copyOf("Wine placeholder DLL".getBytes(), 32), peFile.realModeStubProgram);
    }
}