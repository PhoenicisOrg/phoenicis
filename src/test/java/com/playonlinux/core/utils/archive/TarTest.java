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

package com.playonlinux.core.utils.archive;

import com.google.common.io.Files;
import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.app.PlayOnLinuxException;
import com.playonlinux.core.injection.AbstractConfiguration;
import com.playonlinux.core.injection.Bean;
import com.playonlinux.core.injection.InjectionException;
import com.playonlinux.core.lang.FallbackLanguageBundle;
import com.playonlinux.core.lang.LanguageBundle;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

public class TarTest {
    final URL inputUrl = TarTest.class.getResource(".");

    static class TestContextConfig extends AbstractConfiguration {
        @Bean
        protected PlayOnLinuxContext playOnLinuxContext() throws PlayOnLinuxException, IOException {
            return new PlayOnLinuxContext();
        }

        @Bean
        protected LanguageBundle languageBundle() {
            return FallbackLanguageBundle.getInstance();
        }

        @Override
        protected String definePackage() {
            return "com.playonlinux";
        }

        @Override
        public void close() throws Exception {

        }
    }

    @Before
    public void setUp() throws InjectionException {
        TestContextConfig testContextConfig = new TestContextConfig();
        testContextConfig.setStrictLoadingPolicy(false);
        testContextConfig.load();
    }


    @Test
    public void testUncompressTarFile() throws IOException, ArchiveException {
        testUncompress("test1.tar");
    }

    @Test
    public void testUncompressTarGzFile() throws IOException, ArchiveException {
        testUncompress("test2.tar.gz");
    }

    @Test
    public void testUncompressTarBz2File() throws IOException, ArchiveException {
        testUncompress("test3.tar.bz2");
    }

    private void testUncompress(String fileName) throws IOException, ArchiveException {
        final File inputFile = new File(inputUrl.getPath(), fileName);
        final File temporaryDirectory = Files.createTempDir();

        temporaryDirectory.deleteOnExit();

        final List<File> extractedFiles = new Extractor().uncompress(inputFile, temporaryDirectory);

        assertTrue(new File(temporaryDirectory, "directory1").isDirectory());
        final File file1 = new File(temporaryDirectory, "file1.txt");
        final File file2 = new File(temporaryDirectory, "file2.txt");
        final File file0 = new File(new File(temporaryDirectory, "directory1"), "file0.txt");

        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file0.exists());

        assertEquals("file1content", new String(FileUtils.readFileToByteArray(file1)));
        assertEquals("file2content", new String(FileUtils.readFileToByteArray(file2)));
        assertEquals("file0content", new String(FileUtils.readFileToByteArray(file0)));

        assertEquals(5, extractedFiles.size());
    }

    @Test
    public void testGunzip() throws IOException, ArchiveException {
        final File inputFile = new File(inputUrl.getPath(), "pol.txt.gz");
        final File outputFile = File.createTempFile("output", "txt");

        new Tar().gunzip(inputFile, outputFile);

        assertEquals("PlayOnLinux", new String(FileUtils.readFileToByteArray(outputFile)));
    }

    @Test
    public void testBunzip2() throws IOException, ArchiveException {
        final File inputFile = new File(inputUrl.getPath(), "pol.txt.bz2");
        final File outputFile = File.createTempFile("output", "txt");

        new Tar().bunzip2(inputFile, outputFile);

        assertEquals("PlayOnLinux", new String(FileUtils.readFileToByteArray(outputFile)));
    }

    @Test(expected = ArchiveException.class)
    public void testBunzip2_extractGzip() throws IOException, ArchiveException {
        final File inputFile = new File(inputUrl.getPath(), "pol.txt.gz");
        final File outputFile = File.createTempFile("output", "txt");

        new Tar().bunzip2(inputFile, outputFile);
    }

    @Test(expected = ArchiveException.class)
    public void tesGunzip_extractBzip2() throws IOException, ArchiveException {
        final File inputFile = new File(inputUrl.getPath(), "pol.txt.bz2");
        final File outputFile = File.createTempFile("output", "txt");

        new Tar().gunzip(inputFile, outputFile);

    }

}