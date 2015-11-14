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

package com.playonlinux.dto.web;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.playonlinux.apps.dto.ScriptDTO;

public class ScriptDTOTest {

    private ScriptDTO scriptDTO;

    @Before
    public void setUp() {
        this.scriptDTO = new ScriptDTO.Builder().withScriptName("Name").withId(13).build();
    }

    @Test
    public void testScriptDTO_CreateDTO_nameIsPopulated() {
        assertEquals("Name", scriptDTO.getScriptName());
    }

    @Test
    public void testScriptDTO_CreateDTO_iconIsPopulated() {
        assertEquals(13, scriptDTO.getId());
    }
}