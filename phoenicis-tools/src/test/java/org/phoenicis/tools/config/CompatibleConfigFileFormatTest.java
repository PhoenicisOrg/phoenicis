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

package org.phoenicis.tools.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;


public class CompatibleConfigFileFormatTest {
    private File tmpLegacy;
    private File tmpNew;

    @Before
    public void setUp() throws IOException {
        tmpLegacy = File.createTempFile("legacy", "txt");
        tmpLegacy.deleteOnExit();
        try(final PrintWriter printWriterLegacy = new PrintWriter(new FileOutputStream(tmpLegacy))) {
            printWriterLegacy.write("ARCH=x86\nVERSION=1.7.33\nTEST=test=test");
            printWriterLegacy.flush();
        }

        tmpNew = File.createTempFile("new", "txt");
        tmpNew.deleteOnExit();
        try(final PrintWriter printWriterLegacy = new PrintWriter(new FileOutputStream(tmpNew))) {
            printWriterLegacy.write("{\"distributionCode\":\"staging\",\"operatingSystem\":\"MACOSX\",\"version\":\"1.7.35\",\"architecture\":\"I386\"}");
            printWriterLegacy.flush();
        }
    }

    @Test
    public void testWriteValue() {

    }

    @Test
    public void testReadValue_legacyFile() {
        final CompatibleConfigFileFormat compatibleConfigFileFormat = new CompatibleConfigFileFormat(new ObjectMapper(), tmpLegacy);
        assertEquals("x86", compatibleConfigFileFormat.readValue("ARCH"));
        assertEquals("1.7.33", compatibleConfigFileFormat.readValue("VERSION"));
    }

    @Test
    public void testReadValue_legacyFileContainsEquals() {
        final CompatibleConfigFileFormat compatibleConfigFileFormat = new CompatibleConfigFileFormat(new ObjectMapper(), tmpLegacy);
        assertEquals("test=test", compatibleConfigFileFormat.readValue("TEST"));
    }


    @Test
    public void testReadValue_legacyFile_Unexisting() {
        final CompatibleConfigFileFormat compatibleConfigFileFormat = new CompatibleConfigFileFormat(new ObjectMapper(), tmpLegacy);
        assertEquals("", compatibleConfigFileFormat.readValue("unexisting"));
        assertEquals("default", compatibleConfigFileFormat.readValue("unexisting", "default"));
    }

    @Test
    public void testReadValue_jsonFile() {
        final CompatibleConfigFileFormat compatibleConfigFileFormat = new CompatibleConfigFileFormat(new ObjectMapper(), tmpNew);
        assertEquals("I386", compatibleConfigFileFormat.readValue("architecture"));
        assertEquals("1.7.35", compatibleConfigFileFormat.readValue("version"));
    }

    @Test
    public void testReadValue_jsonFile_Unexisting() {
        final CompatibleConfigFileFormat compatibleConfigFileFormat = new CompatibleConfigFileFormat(new ObjectMapper(), tmpNew);
        assertEquals("", compatibleConfigFileFormat.readValue("unexisting"));
        assertEquals("default", compatibleConfigFileFormat.readValue("unexisting", "default"));
    }

    @Test
    public void testDeleteValue_Legacy() throws IOException {
        final CompatibleConfigFileFormat compatibleConfigFileFormat = new CompatibleConfigFileFormat(new ObjectMapper(), tmpLegacy);
        assertEquals("test=test", compatibleConfigFileFormat.readValue("TEST"));
        compatibleConfigFileFormat.deleteValue("TEST");
        assertEquals("", compatibleConfigFileFormat.readValue("TEST"));
        assertEquals("x86", compatibleConfigFileFormat.readValue("ARCH"));
        assertEquals("1.7.33", compatibleConfigFileFormat.readValue("VERSION"));
    }

    @Test
    public void testDeleteValue_jsonFile() throws IOException {
        final CompatibleConfigFileFormat compatibleConfigFileFormat = new CompatibleConfigFileFormat(new ObjectMapper(), tmpNew);
        assertEquals("I386", compatibleConfigFileFormat.readValue("architecture"));
        assertEquals("1.7.35", compatibleConfigFileFormat.readValue("version"));
        assertEquals("staging", compatibleConfigFileFormat.readValue("distributionCode"));
        compatibleConfigFileFormat.deleteValue("distributionCode");
        assertEquals("I386", compatibleConfigFileFormat.readValue("architecture"));
        assertEquals("1.7.35", compatibleConfigFileFormat.readValue("version"));
        assertEquals("", compatibleConfigFileFormat.readValue("distributionCode"));
    }


    @Test
    public void testWriteNewValue_Legacy() throws IOException {
        final CompatibleConfigFileFormat compatibleConfigFileFormat = new CompatibleConfigFileFormat(new ObjectMapper(), tmpLegacy);
        assertEquals("", compatibleConfigFileFormat.readValue("TEST2"));
        compatibleConfigFileFormat.writeValue("TEST2", "Content");
        assertEquals("Content", compatibleConfigFileFormat.readValue("TEST2"));
        assertEquals("x86", compatibleConfigFileFormat.readValue("ARCH"));
        assertEquals("1.7.33", compatibleConfigFileFormat.readValue("VERSION"));
    }

    @Test
    public void testWriteNewValue_jsonFile() throws IOException {
        final CompatibleConfigFileFormat compatibleConfigFileFormat = new CompatibleConfigFileFormat(new ObjectMapper(), tmpNew);
        assertEquals("I386", compatibleConfigFileFormat.readValue("architecture"));
        assertEquals("1.7.35", compatibleConfigFileFormat.readValue("version"));
        compatibleConfigFileFormat.writeValue("TEST3", "Content3");
        assertEquals("I386", compatibleConfigFileFormat.readValue("architecture"));
        assertEquals("1.7.35", compatibleConfigFileFormat.readValue("version"));
        assertEquals("Content3", compatibleConfigFileFormat.readValue("TEST3"));
    }


    @Test
    public void testWriteExistingValue_Legacy() throws IOException {
        final CompatibleConfigFileFormat compatibleConfigFileFormat = new CompatibleConfigFileFormat(new ObjectMapper(), tmpLegacy);
        assertEquals("x86", compatibleConfigFileFormat.readValue("ARCH"));
        compatibleConfigFileFormat.writeValue("ARCH", "amd64");
        assertEquals("amd64", compatibleConfigFileFormat.readValue("ARCH"));
    }

    @Test
    public void testWriteExistingValue_jsonFile() throws IOException {
        final CompatibleConfigFileFormat compatibleConfigFileFormat = new CompatibleConfigFileFormat(new ObjectMapper(), tmpNew);
        assertEquals("I386", compatibleConfigFileFormat.readValue("architecture"));
        compatibleConfigFileFormat.writeValue("architecture", "AMD64");
        assertEquals("AMD64", compatibleConfigFileFormat.readValue("architecture"));
    }

    @Test
    public void testWriteNewValue_unexistingFile() throws IOException {
        final File temporaryFile = File.createTempFile("test", "json");
        temporaryFile.deleteOnExit();
        final CompatibleConfigFileFormat compatibleConfigFileFormat = new CompatibleConfigFileFormat(new ObjectMapper(), temporaryFile);
        assertEquals("", compatibleConfigFileFormat.readValue("TEST3"));
        compatibleConfigFileFormat.writeValue("TEST3", "Content3");
        assertEquals("Content3", compatibleConfigFileFormat.readValue("TEST3"));
    }


}