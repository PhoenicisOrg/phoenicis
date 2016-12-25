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

package com.playonlinux.containers.wine.configurations;

import com.playonlinux.containers.wine.parameters.*;

import java.io.File;

public class DefaultWinePrefixDisplayConfiguration implements WinePrefixDisplayConfiguration {
    @Override
    public GLSL getGLSL(File registryFile) {
        return GLSL.DEFAULT;
    }

    @Override
    public DirectDrawRenderer getDirectDrawRenderer(File registryFile) {
        return DirectDrawRenderer.DEFAULT;
    }

    @Override
    public Multisampling getMultisampling(File registryFile) {
        return Multisampling.DEFAULT;
    }

    @Override
    public OffscreenRenderingMode getOffscreenRenderingMode(File registryFile) {
        return OffscreenRenderingMode.DEFAULT;
    }

    @Override
    public RenderTargetModeLock getRenderTargetModeLock(File registryFile) {
        return RenderTargetModeLock.DEFAULT;
    }

    @Override
    public StrictDrawOrdering getStrictDrawOrdering(File registryFile) {
        return StrictDrawOrdering.DEFAULT;
    }

    @Override
    public AlwaysOffscreen getAlwaysOffscreen(File registryFile) {
        return AlwaysOffscreen.DEFAULT;
    }

    @Override
    public VideoMemorySize getVideoMemorySize(File registryFile) {
        return new VideoMemorySize(true, 0);
    }
}
