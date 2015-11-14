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

package com.playonlinux.containers;

import java.io.File;

import com.playonlinux.containers.entities.WinePrefixContainerEntity;
import com.playonlinux.wine.WinePrefix;

public class WinePrefixContainer extends AbstractContainer<WinePrefixContainerEntity> {
    private final WinePrefix underlyingWinePrefix;

    public WinePrefixContainer(File containerPath) {
        super(containerPath);
        this.underlyingWinePrefix = new WinePrefix(containerPath);
    }

    @Override
    public WinePrefixContainerEntity createEntity() {
        return new WinePrefixContainerEntity.Builder().withName(this.containerPath.getName())
                .withPath(this.containerPath.getAbsolutePath())
                .withWineArchitecture(this.underlyingWinePrefix.fetchArchitecture().name())
                .withWineDistribution(this.underlyingWinePrefix.fetchDistribution().getDistributionCode())
                .withWineVersion(this.underlyingWinePrefix.fetchVersion().toString())
                .withAutomaticallyUpdated(this.underlyingWinePrefix.isAutomaticallyUpdated())
                .withGLSL(this.underlyingWinePrefix.getDisplayConfiguration().getGLSL())
                .withMultisampling(this.underlyingWinePrefix.getDisplayConfiguration().getMultisampling())
                .withOffscreenRenderingMode(
                        this.underlyingWinePrefix.getDisplayConfiguration().getOffscreenRenderingMode())
                .withStrictDrawOrdering(this.underlyingWinePrefix.getDisplayConfiguration().getStrictDrawOrdering())
                .withRenderTargetModeLock(this.underlyingWinePrefix.getDisplayConfiguration().getRenderTargetModeLock())
                .withVideoMemorySize(this.underlyingWinePrefix.getDisplayConfiguration().getVideoMemorySize())
                .withDirectDrawRenderer(this.underlyingWinePrefix.getDisplayConfiguration().getDirectDrawRenderer())
                .withAlwaysOffscreen(this.underlyingWinePrefix.getDisplayConfiguration().getAlwaysOffscreen())
                .withMouseWarpOverride(this.underlyingWinePrefix.getInputConfiguration().getMouseWarpOverride())
                .withWinePrefixDirectory(this.underlyingWinePrefix.getWinePrefixDirectory()).build();
    }

}
