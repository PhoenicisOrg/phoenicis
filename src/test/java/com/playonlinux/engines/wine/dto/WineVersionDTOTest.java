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

package com.playonlinux.engines.wine.dto;

import org.junit.Test;

import static org.junit.Assert.*;


public class WineVersionDTOTest {
    final WineVersionDTO wineVersionDTO = new WineVersionDTO.Builder()
            .withVersion("Version")
            .withGeckoFile("GeckoFile")
            .withGeckoMd5("GeckoMD5")
            .withGeckoUrl("GeckoUrl")
            .withMonoFile("GeckoFile")
            .withMonoFile("MonoFile")
            .withMonoMd5("MonoMD5")
            .withSha1sum("Sha1Sum")
            .withMonoUrl("MonoUrl")
            .withUrl("URL").build();

    @Test
    public void testGetVersion() {
        assertEquals("Version", wineVersionDTO.getVersion());
    }

    @Test
    public void testGetUrl() {
        assertEquals("URL", wineVersionDTO.getUrl());
    }

    @Test
    public void testGetSha1sum() {
        assertEquals("Sha1Sum", wineVersionDTO.getSha1sum());
    }

    @Test
    public void testGetGeckoMd5() {
        assertEquals("GeckoMD5", wineVersionDTO.getGeckoMd5());
    }

    @Test
    public void testGetGeckoUrl() {
        assertEquals("GeckoUrl", wineVersionDTO.getGeckoUrl());
    }

    @Test
    public void testGetMonoMd5() {
        assertEquals("MonoMD5", wineVersionDTO.getMonoMd5());
    }

    @Test
    public void testGetMonoUrl() {
        assertEquals("MonoUrl", wineVersionDTO.getMonoUrl());
    }

    @Test
    public void testGetMonoFile() {
        assertEquals("MonoFile", wineVersionDTO.getMonoFile());
    }

    @Test
    public void testGetGeckoFile() {
        assertEquals("GeckoFile", wineVersionDTO.getGeckoFile());
    }
}