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

import java.util.List;

import com.playonlinux.core.entities.Entity;

public class WineVersionsWindowEntity implements Entity {
    private final List<WineVersionDistributionItemEntity> distributions;
    private final boolean downloading;
    private final boolean downloadFailed;

    public WineVersionsWindowEntity(List<WineVersionDistributionItemEntity> distributions, boolean downloading,
            boolean downloadFailed) {
        this.distributions = distributions;
        this.downloading = downloading;
        this.downloadFailed = downloadFailed;
    }

    public List<WineVersionDistributionItemEntity> getDistributions() {
        return distributions;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public boolean isDownloadFailed() {
        return downloadFailed;
    }
}
