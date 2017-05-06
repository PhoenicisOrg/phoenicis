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

package org.phoenicis.tools.files;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class FileAnalyserTest {
    private static final String CLRF_STRICT = "#!/bin/bash\r\n" + "[ \"$PHOENICIS\" = \"\" ] && exit 0\r\n"
            + "source \"$PHOENICIS/lib/sources\"\r\n" + "\r\n" + "TITLE=\"Legacy script\"\r\n" + "\r\n"
            + "POL_SetupWindow_Init\n" + "POL_SetupWindow_message \"Test\"\r\n" + "POL_SetupWindow_Close\r\n" + "\n\n"
            + "exit ";

    private static final String LF_STRICT = "#!/bin/bash\n" + "[ \"$PHOENICIS\" = \"\" ] && exit 0\n"
            + "source \"$PHOENICIS/lib/sources\"\n" + "\n" + "TITLE=\"Legacy script\"\n" + "\n"
            + "POL_SetupWindow_Init\n" + "POL_SetupWindow_message \"Test\"\n" + "POL_SetupWindow_Close\n" + "\n"
            + "exit ";

    final URL archiveUrl = FileAnalyserTest.class.getResource("/org/phoenicis/tools/archive");
    private final FileAnalyser fileAnalyser = new FileAnalyser();

    @Test
    public void testGetMimetype_GZFile() throws URISyntaxException {
        assertEquals("application/x-gzip",
                fileAnalyser.getMimetype(new File(archiveUrl.toURI().getPath(), "pol.txt.gz")));
    }

    @Test
    public void testGetMimetype_BZ2File() throws URISyntaxException {
        assertEquals("application/x-bzip2",
                fileAnalyser.getMimetype(new File(archiveUrl.toURI().getPath(), "pol.txt.bz2")));
    }

    @Test
    public void testGetMimetype_TarGZFile() throws URISyntaxException {
        assertEquals("application/x-gzip",
                fileAnalyser.getMimetype(new File(archiveUrl.toURI().getPath(), "test2.tar.gz")));
    }

    @Test
    public void testGetMimetype_TarBZ2File() throws URISyntaxException {
        assertEquals("application/x-bzip2",
                fileAnalyser.getMimetype(new File(archiveUrl.toURI().getPath(), "test3.tar.bz2")));
    }

    @Test
    @Ignore
    public void testGetMimetype_TarFile() throws URISyntaxException {
        assertEquals("application/octet-stream",
                fileAnalyser.getMimetype(new File(archiveUrl.toURI().getPath(), "test1.tar")));
    }

    @Test
    public void testLineSeparatorCRLF() {
        assertEquals("\r\n", fileAnalyser.identifyLineDelimiter(CLRF_STRICT));
    }

    @Test
    public void testLineSeparatorLF() {
        assertEquals("\n", fileAnalyser.identifyLineDelimiter(LF_STRICT));
    }

}