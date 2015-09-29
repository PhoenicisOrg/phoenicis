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

package com.playonlinux.engines.wine.entities;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class WineVersionsWindowEntityTest {
    private final List<WineVersionDistributionItemEntity> wineVersionDistributionItemEntities = mockList();

    private List<WineVersionDistributionItemEntity> mockList() {
        final List<WineVersionDistributionItemEntity> wineVersionDistributionItemEntities = new ArrayList<>();
        wineVersionDistributionItemEntities.add(mock(WineVersionDistributionItemEntity.class));
        wineVersionDistributionItemEntities.add(mock(WineVersionDistributionItemEntity.class));
        return wineVersionDistributionItemEntities;
    }

    private final WineVersionsWindowEntity wineVersionsWindowEntityUnderTest = new WineVersionsWindowEntity(wineVersionDistributionItemEntities, true, false);

    @Test
    public void testGetDistributions() {
        assertEquals(2, wineVersionsWindowEntityUnderTest.getDistributions().size());
        assertSame(wineVersionDistributionItemEntities, wineVersionsWindowEntityUnderTest.getDistributions());
    }

    @Test
    public void testIsDownloading() {
        assertTrue(wineVersionsWindowEntityUnderTest.isDownloading());
    }

    @Test
    public void testIsDownloadFailed() {
        assertFalse(wineVersionsWindowEntityUnderTest.isDownloadFailed());
    }
}