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

import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;


public class MimeTypeTest {
    final URL inputUrl = MimeTypeTest.class.getResource("./archive");

    @Test
    public void testGetMimetype_GZFile() throws PlayOnLinuxException {
        assertEquals("application/x-gzip", MimeType.getMimetype(new File(inputUrl.getPath(), "pol.txt.gz")));
    }

    public void testGetMimetype_BZ2File() throws PlayOnLinuxException {
        assertEquals("application/x-bzip2", MimeType.getMimetype(new File(inputUrl.getPath(), "pol.txt.bz2")));
    }

    public void testGetMimetype_TarGZFile() throws PlayOnLinuxException {
        assertEquals("application/x-gzip", MimeType.getMimetype(new File(inputUrl.getPath(), "test2.tar.gz")));
    }

    public void testGetMimetype_TarBZ2File() throws PlayOnLinuxException {
        assertEquals("application/x-bzip2", MimeType.getMimetype(new File(inputUrl.getPath(), "test3.tar.bz2")));
    }

    public void testGetMimetype_TarFile() throws PlayOnLinuxException {
        assertEquals("application/octet-stream", MimeType.getMimetype(new File(inputUrl.getPath(), "test1.tar")));
    }
}