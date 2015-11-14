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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class ChecksumCalculatorTest {

    @Test
    public void testChecksumCalculate_generateAFile_CheckMD5() throws IOException, NoSuchAlgorithmException {
        File temporaryFile = File.createTempFile("testHash", "txt");
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile);

        fileOutputStream.write("TEST".getBytes());
        assertEquals("033bd94b1168d7e4f0d644c3c95e35bf", new ChecksumCalculator().calculate(temporaryFile, "MD5"));
    }

    @Test
    public void testChecksumCalculate_generateAFile_CheckSHA1() throws IOException, NoSuchAlgorithmException {
        File temporaryFile = File.createTempFile("testHash", "txt");
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile);

        fileOutputStream.write("TEST".getBytes());
        assertEquals("984816fd329622876e14907634264e6f332e9fb3",
                new ChecksumCalculator().calculate(temporaryFile, "SHA1"));
    }
}
