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

import com.playonlinux.apps.entities.AppEntity;

public class AppsItemDTOTest {

    private AppEntity appsItemDTO;

    @Before
    public void setUp() {
        this.appsItemDTO = new AppEntity.Builder().withName("Name").withCategoryName("Category")
                .withDescription("a description").withCommercial(true).withRequiresNoCd(true).withTesting(true).build();
    }

    @Test
    public void testAppsItemScriptDTO_CreateDTO_nameIsPopulated() {
        assertEquals("Name", appsItemDTO.getName());
    }

    @Test
    public void testAppsItemScriptDTO_CreateDTO_categoryIsPopulated() {
        assertEquals("Category", appsItemDTO.getCategoryName());
    }

    @Test
    public void testAppsItemScriptDTO_CreateDTO_descrtiptionIsPopulated() {
        assertEquals("a description", appsItemDTO.getDescription());
    }

    @Test
    public void testAppsItemScriptDTO_CreateDTO_commercialIsSet() {
        assertEquals(true, appsItemDTO.isCommercial());
    }

    @Test
    public void testAppsItemScriptDTO_CreateDTO_testingIsSet() {
        assertEquals(true, appsItemDTO.isRequiresNoCd());
    }

    @Test
    public void testAppsItemScriptDTO_CreateDTO_noCdIsSet() {
        assertEquals(true, appsItemDTO.isTesting());
    }
}