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

package com.playonlinux.wine.configurations;

import com.playonlinux.wine.parameters.AlwaysOffscreen;
import com.playonlinux.wine.parameters.DirectDrawRenderer;
import com.playonlinux.wine.parameters.GLSL;
import com.playonlinux.wine.parameters.Multisampling;
import com.playonlinux.wine.parameters.OffscreenRenderingMode;
import com.playonlinux.wine.parameters.RenderTargetModeLock;
import com.playonlinux.wine.parameters.StrictDrawOrdering;
import com.playonlinux.wine.parameters.VideoMemorySize;

/**
 * Represents all Display Configuration of a
 * {@link com.playonlinux.wine.WinePrefix}
 */
public interface WinePrefixDisplayConfiguration {
    /**
     *
     * @return The {@link GLSL} value
     */
    GLSL getGLSL();

    /**
     *
     * @return The {@link DirectDrawRenderer} value
     */
    DirectDrawRenderer getDirectDrawRenderer();

    /**
     *
     * @return The {@link Multisampling} value
     */
    Multisampling getMultisampling();

    /**
     *
     * @return The {@link OffscreenRenderingMode} value
     */
    OffscreenRenderingMode getOffscreenRenderingMode();

    /**
     *
     * @return The {@link RenderTargetModeLock} value
     */
    RenderTargetModeLock getRenderTargetModeLock();

    /**
     *
     * @return The {@link StrictDrawOrdering} value
     */
    StrictDrawOrdering getStrictDrawOrdering();

    /**
     *
     * @return The {@link AlwaysOffscreen} value
     */
    AlwaysOffscreen getAlwaysOffscreen();

    /**
     *
     * @return The {@link VideoMemorySize} value
     */
    VideoMemorySize getVideoMemorySize();
}
