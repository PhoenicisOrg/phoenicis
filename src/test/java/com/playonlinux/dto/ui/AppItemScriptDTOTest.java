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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AppItemScriptDTOTest {

    private AppsItemScriptDTO appsItemScriptDTO;

    @Before
    public void setUp() {
        this.appsItemScriptDTO = new AppsItemScriptDTO.Builder()
                .withName("Name")
                .withId(13)
                .build();
    }
    
    @Test
    public void testAppsItemScriptDTO_CreateDTO_nameIsPopulated() {
        assertEquals("Name", appsItemScriptDTO.getName());
    }

    @Test
    public void testAppsItemScriptDTO_CreateDTO_idIsPopulated() {
        assertEquals(13, appsItemScriptDTO.getId());
    }
}