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

package com.playonlinux.engines.wine.packages;


import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.engines.wine.dto.WineVersionDTO;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@Scan
public class GeckoWinePackage implements WinePackage {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;
    private final WineVersionDTO wineVersionDTO;

    public GeckoWinePackage(WineVersionDTO wineVersionDTO) {
        this.wineVersionDTO = wineVersionDTO;
    }

    @Override
    public File getPackageDestination() {
        return playOnLinuxContext.makeLocalGeckoPath();
    }

    @Override
    public String getPackageFileName() {
        return wineVersionDTO.getGeckoFile();
    }

    @Override
    public URL getPackageUrl() throws MalformedURLException {
        return new URL(wineVersionDTO.getGeckoUrl());
    }

    @Override
    public String getPackageChecksum() {
        return wineVersionDTO.getGeckoMd5();
    }

    @Override
    public String getPackageTypeName() {
        return "gecko";
    }
}
