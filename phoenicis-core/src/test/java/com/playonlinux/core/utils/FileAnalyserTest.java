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
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.playonlinux.app.PlayOnLinuxException;


public class FileAnalyserTest {
    private static final String CLRF_STRICT = "#!/bin/bash\r\n" +
            "[ \"$PLAYONLINUX\" = \"\" ] && exit 0\r\n" +
            "source \"$PLAYONLINUX/lib/sources\"\r\n" +
            "\r\n" +
            "TITLE=\"Legacy script\"\r\n" +
            "\r\n" +
            "POL_SetupWindow_Init\n" +
            "POL_SetupWindow_message \"Test\"\r\n" +
            "POL_SetupWindow_Close\r\n" +
            "\n\n" +
            "exit ";

    private static final String LF_STRICT = "#!/bin/bash\n" +
            "[ \"$PLAYONLINUX\" = \"\" ] && exit 0\n" +
            "source \"$PLAYONLINUX/lib/sources\"\n" +
            "\n" +
            "TITLE=\"Legacy script\"\n" +
            "\n" +
            "POL_SetupWindow_Init\n" +
            "POL_SetupWindow_message \"Test\"\n" +
            "POL_SetupWindow_Close\n" +
            "\n" +
            "exit ";

    final URL archiveUrl = FileAnalyserTest.class.getResource("./archive");
    final URL scriptUrl = FileAnalyserTest.class.getResource("../scripts");

    @Test
    public void testGetMimetype_GZFile() throws PlayOnLinuxException {
        assertEquals("application/x-gzip", FileAnalyser.getMimetype(new File(archiveUrl.getPath(), "pol.txt.gz")));
    }

    @Test
    public void testGetMimetype_BZ2File() throws PlayOnLinuxException {
        assertEquals("application/x-bzip2", FileAnalyser.getMimetype(new File(archiveUrl.getPath(), "pol.txt.bz2")));
    }

    @Test
    public void testGetMimetype_TarGZFile() throws PlayOnLinuxException {
        assertEquals("application/x-gzip", FileAnalyser.getMimetype(new File(archiveUrl.getPath(), "test2.tar.gz")));
    }

    @Test
    public void testGetMimetype_TarBZ2File() throws PlayOnLinuxException {
        assertEquals("application/x-bzip2", FileAnalyser.getMimetype(new File(archiveUrl.getPath(), "test3.tar.bz2")));
    }

    @Test
    public void testGetMimetype_TarFile() throws PlayOnLinuxException {
        assertEquals("application/octet-stream", FileAnalyser.getMimetype(new File(archiveUrl.getPath(), "test1.tar")));
    }

    @Test
    public void testLineSeparatorCRLF() throws IOException {
        assertEquals("\r\n", FileAnalyser.identifyLineDelimiter(CLRF_STRICT));
    }

    @Test
    public void testLineSeparatorLF() throws IOException {
        assertEquals("\n", FileAnalyser.identifyLineDelimiter(LF_STRICT));
    }

}