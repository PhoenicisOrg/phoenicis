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

package com.playonlinux.apps.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.playonlinux.apps.dto.ApplicationDTO;

public class ApplicationDTOTest {

    private ApplicationDTO applicationDTO;

    @Before
    public void setUp() {
        this.applicationDTO = new ApplicationDTO.Builder()
                .withName("Name")
                .withId(13)
                .withDescription("a description")
                .withIconUrl("a icon url")
                .build();
    }
    @Test
    public void testApplicationDTO_CreateDTO_nameIsPopulated() {
        assertEquals("Name", applicationDTO.getName());
    }
    @Test
    public void testApplicationDTO_CreateDTO_idIsPopulated() {
        assertEquals(13, applicationDTO.getId());
    }

    @Test
    public void testApplicationDTO_CreateDTO_descriptionIsPopulated() {
        assertEquals("a description", applicationDTO.getDescription());
    }

    @Test
    public void testApplicationDTO_CreateDTO_iconURLIsPopulated() {
        assertEquals("a icon url", applicationDTO.getIconUrl());
    }
}