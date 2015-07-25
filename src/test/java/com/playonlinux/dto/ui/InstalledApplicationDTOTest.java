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

package com.playonlinux.dto.ui;

import com.playonlinux.library.dto.InstalledApplicationDTO;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class InstalledApplicationDTOTest {

    private InstalledApplicationDTO installedApplicationDto;

    @Before
    public void setUp() throws MalformedURLException {
        this.installedApplicationDto = new InstalledApplicationDTO.Builder()
                .withName("Name")
                .withIcon(new URL("file://"+new File("/tmp/icon").getAbsolutePath()))
                .build();
    }
    @Test
    public void testShortcutDTO_CreateDTO_nameIsPopulated() {
        assertEquals("Name", installedApplicationDto.getName());
    }

    @Test
    public void testShortcutDTO_CreateDTO_iconIsPopulated() {
        assertEquals("file:/tmp/icon", installedApplicationDto.getIcon().toString());
    }
}