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

package org.phoenicis.tools.archive;

import com.google.common.io.Files;
import org.phoenicis.tools.files.FileAnalyser;
import org.phoenicis.tools.files.FileUtilities;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ExtractorTest {
    final URL inputUrl = ExtractorTest.class.getResource(".");
    private Tar tar = new Tar(new FileUtilities());
    private Zip zip = new Zip();

    private Extractor extractor = new Extractor(new FileAnalyser(), tar, zip);

    @Test
    public void testUncompressTarFile() throws IOException {
        testUncompress("test1.tar");
    }

    @Test
    public void testUncompressTarGzFile() throws IOException {
        testUncompress("test2.tar.gz");
    }

    @Test
    public void testUncompressTarBz2File() throws IOException {
        testUncompress("test3.tar.bz2");
    }

    @Test
    public void testUncompressZipFile() throws IOException {
        testUncompress("test4.zip");
    }

    @Test
    public void testUncompress_withSymbolicLinks() throws IOException {
        File inputFile = null;
        try {
            inputFile = new File(inputUrl.toURI().getPath(), "tarLink.tar.gz");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        final File temporaryDirectory = Files.createTempDir();

        temporaryDirectory.deleteOnExit();

        final List<File> extractedFiles = extractor.uncompress(inputFile, temporaryDirectory, e -> {});

        final File file1 = new File(temporaryDirectory, "file1.txt");
        final File file2 = new File(temporaryDirectory, "file1_link.txt");

        assertTrue(file1.exists());
        assertTrue(file2.exists());

        assertEquals("file1content", new String(FileUtils.readFileToByteArray(file1)));
        assertEquals("file1content", new String(FileUtils.readFileToByteArray(file2)));

        assertTrue(java.nio.file.Files.isSymbolicLink(Paths.get(file2.getPath())));
    }

    private void testUncompress(String fileName) throws IOException {
        File inputFile = null;
        try {
            inputFile = new File(inputUrl.toURI().getPath(), fileName);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        final File temporaryDirectory = Files.createTempDir();

        temporaryDirectory.deleteOnExit();

        final List<File> extractedFiles = extractor.uncompress(inputFile, temporaryDirectory, e -> {});

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

        System.out.println(extractedFiles);;
        assertEquals(4, extractedFiles.size());
    }

    @Test
    public void testGunzip() throws IOException {
        File inputFile = null;
        try {
            inputFile = new File(inputUrl.toURI().getPath(), "pol.txt.gz");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        final File outputFile = File.createTempFile("output", "txt");

        tar.gunzip(inputFile, outputFile);

        assertEquals("PlayOnLinux", new String(FileUtils.readFileToByteArray(outputFile)));
    }

    @Test
    public void testBunzip2() throws IOException {
        File inputFile = null;
        try {
            inputFile = new File(inputUrl.toURI().getPath(), "pol.txt.bz2");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        final File outputFile = File.createTempFile("output", "txt");

        tar.bunzip2(inputFile, outputFile);

        assertEquals("PlayOnLinux", new String(FileUtils.readFileToByteArray(outputFile)));
    }

    @Test(expected = ArchiveException.class)
    public void testBunzip2_extractGzip() throws IOException {
        File inputFile = null;
        try {
            inputFile = new File(inputUrl.toURI().getPath(), "pol.txt.gz");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        final File outputFile = File.createTempFile("output", "txt");
        outputFile.deleteOnExit();
        tar.bunzip2(inputFile, outputFile);
    }

    @Test(expected = ArchiveException.class)
    public void tesGunzip_extractBzip2() throws IOException {
        File inputFile = null;
        try {
            inputFile = new File(inputUrl.toURI().getPath(), "pol.txt.bz2");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        final File outputFile = File.createTempFile("output", "txt");
        outputFile.deleteOnExit();
        tar.gunzip(inputFile, outputFile);

    }
}