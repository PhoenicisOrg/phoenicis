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
import com.playonlinux.wine.registry.AbstractRegistryNode;
import com.playonlinux.wine.registry.RegistryKey;
import com.playonlinux.wine.registry.RegistryValue;

public class RegistryWinePrefixDisplayConfiguration implements WinePrefixDisplayConfiguration {
    private static final String READTEX = "readtex";
    private static final String READDRAW = "readdraw";
    private static final String PBUFFER = "pbuffer";
    private static final String BACKBUFFER = "backbuffer";
    private static final String FBO = "fbo";
    private static final String OPENGL = "opengl";
    private static final String GDI = "gdi";
    private static final String DISABLED = "disabled";
    private static final String ENABLED = "enabled";
    private static final String ALWAYS_OFFSCREEN = "AlwaysOffscreen";
    private static final String DIRECT3D = "Direct3D";
    private static final String DIRECT_DRAW_RENDERER = "DirectDrawRenderer";
    private static final String OFFSCREEN_RENDERING_MODE = "OffscreenRenderingMode";
    private static final String MULTISAMPLING = "Multisampling";
    private static final String RENDER_TARGET_MODE_LOCK = "RenderTargetModeLock";
    private static final String STRICT_DRAW_ORDERING = "StrictDrawOrdering";
    private static final String USE_GLSL = "UseGLSL";
    private static final String VIDEO_MEMORY_SIZE = "VideoMemorySize";
    private static final String WINE = "Wine";
    private static final String SOFTWARE = "Software";

    private final RegistryKey userRegsitry;

    public RegistryWinePrefixDisplayConfiguration(RegistryKey userRegistry) {
        this.userRegsitry = userRegistry;
    }

    @Override
    public GLSL getGLSL() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild(SOFTWARE, WINE, DIRECT3D, USE_GLSL);
        if (registryChild instanceof RegistryValue) {
            switch (((RegistryValue<?>) registryChild).getText()) {
                case ENABLED:
                    return GLSL.ENABLED;
                case DISABLED:
                    return GLSL.DISABLED;
                default:
                    return GLSL.DEFAULT;
            }
        }
        return GLSL.DEFAULT;
    }

    @Override
    public DirectDrawRenderer getDirectDrawRenderer() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild(SOFTWARE, WINE, DIRECT3D,
                DIRECT_DRAW_RENDERER);
        if (registryChild instanceof RegistryValue) {
            switch (((RegistryValue<?>) registryChild).getText()) {
                case GDI:
                    return DirectDrawRenderer.GDI;
                case OPENGL:
                    return DirectDrawRenderer.OPENGL;
                default:
                    return DirectDrawRenderer.DEFAULT;
            }
        }
        return DirectDrawRenderer.DEFAULT;
    }

    @Override
    public Multisampling getMultisampling() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild(SOFTWARE, WINE, DIRECT3D, MULTISAMPLING);
        if (registryChild instanceof RegistryValue) {
            switch (((RegistryValue<?>) registryChild).getText()) {
                case ENABLED:
                    return Multisampling.ENABLED;
                case DISABLED:
                    return Multisampling.DISABLED;
                default:
                    return Multisampling.DEFAULT;
            }
        }
        return Multisampling.DEFAULT;
    }

    @Override
    public OffscreenRenderingMode getOffscreenRenderingMode() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild(SOFTWARE, WINE, DIRECT3D,
                OFFSCREEN_RENDERING_MODE);
        if (registryChild instanceof RegistryValue) {
            switch (((RegistryValue<?>) registryChild).getText()) {
                case FBO:
                    return OffscreenRenderingMode.FBO;
                case BACKBUFFER:
                    return OffscreenRenderingMode.BACKBUFFER;
                case PBUFFER:
                    return OffscreenRenderingMode.PBUFFER;
                default:
                    return OffscreenRenderingMode.DEFAULT;
            }
        }
        return OffscreenRenderingMode.DEFAULT;
    }

    @Override
    public RenderTargetModeLock getRenderTargetModeLock() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild(SOFTWARE, WINE, DIRECT3D,
                RENDER_TARGET_MODE_LOCK);
        if (registryChild instanceof RegistryValue) {
            switch (((RegistryValue<?>) registryChild).getText()) {
                case DISABLED:
                    return RenderTargetModeLock.DISABLED;
                case READDRAW:
                    return RenderTargetModeLock.READDRAW;
                case READTEX:
                    return RenderTargetModeLock.READTEX;
                default:
                    return RenderTargetModeLock.DEFAULT;
            }
        }
        return RenderTargetModeLock.DEFAULT;
    }

    @Override
    public StrictDrawOrdering getStrictDrawOrdering() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild(SOFTWARE, WINE, DIRECT3D,
                STRICT_DRAW_ORDERING);
        if (registryChild instanceof RegistryValue) {
            switch (((RegistryValue<?>) registryChild).getText()) {
                case ENABLED:
                    return StrictDrawOrdering.ENABLED;
                case DISABLED:
                    return StrictDrawOrdering.DISABLED;
                default:
                    return StrictDrawOrdering.DEFAULT;
            }
        }
        return StrictDrawOrdering.DEFAULT;
    }

    @Override
    public AlwaysOffscreen getAlwaysOffscreen() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild(SOFTWARE, WINE, DIRECT3D,
                ALWAYS_OFFSCREEN);
        if (registryChild instanceof RegistryValue) {
            switch (((RegistryValue<?>) registryChild).getText()) {
                case ENABLED:
                    return AlwaysOffscreen.ENABLED;
                case DISABLED:
                    return AlwaysOffscreen.DISABLED;
                default:
                    return AlwaysOffscreen.DEFAULT;
            }
        }
        return AlwaysOffscreen.DEFAULT;
    }

    @Override
    public VideoMemorySize getVideoMemorySize() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild(SOFTWARE, WINE, DIRECT3D,
                VIDEO_MEMORY_SIZE);
        if (registryChild instanceof RegistryValue) {
            try {
                int videoMemorySize = Integer.valueOf(((RegistryValue<?>) registryChild).getText());
                return new VideoMemorySize(false, videoMemorySize);
            } catch (NumberFormatException e) {
                return new VideoMemorySize(true, 0);
            }
        }
        return new VideoMemorySize(true, 0);
    }
}
