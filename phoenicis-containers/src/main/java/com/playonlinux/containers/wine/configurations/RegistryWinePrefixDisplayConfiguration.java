/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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
import com.playonlinux.win32.registry.AbstractRegistryNode;
import com.playonlinux.win32.registry.RegistryParser;
import com.playonlinux.win32.registry.RegistryValue;

import java.io.File;

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
    
    private static final String HKEY_CURRENT_USER = "HKEY_CURRENT_USER";

    private final RegistryParser registryParser;
    private final WinePrefixDisplayConfiguration defaultConfiguration;

    public RegistryWinePrefixDisplayConfiguration(RegistryParser registryParser, WinePrefixDisplayConfiguration defaultConfiguration) {
        this.defaultConfiguration = defaultConfiguration;
        this.registryParser = registryParser;
    }

    @Override
    public UseGLSL getGLSL(File registryFile) {
        if(!registryFile.exists()) {
            return defaultConfiguration.getGLSL(registryFile);
        }

        final AbstractRegistryNode registryChild = registryParser.parseFile(registryFile, HKEY_CURRENT_USER).getChild(SOFTWARE, WINE, DIRECT3D, USE_GLSL);
        if (registryChild instanceof RegistryValue) {
            switch (((RegistryValue<?>) registryChild).getText()) {
                case ENABLED:
                    return UseGLSL.ENABLED;
                case DISABLED:
                    return UseGLSL.DISABLED;
                default:
                    return UseGLSL.DEFAULT;
            }
        }

        return defaultConfiguration.getGLSL(registryFile);
    }

    @Override
    public DirectDrawRenderer getDirectDrawRenderer(File registryFile) {
        if(!registryFile.exists()) {
            return defaultConfiguration.getDirectDrawRenderer(registryFile);
        }

        final AbstractRegistryNode registryChild = registryParser.parseFile(registryFile, HKEY_CURRENT_USER).getChild(SOFTWARE, WINE, DIRECT3D,
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
        return defaultConfiguration.getDirectDrawRenderer(registryFile);
    }

    @Override
    public Multisampling getMultisampling(File registryFile) {
        if(!registryFile.exists()) {
            return defaultConfiguration.getMultisampling(registryFile);
        }

        final AbstractRegistryNode registryChild = registryParser.parseFile(registryFile, HKEY_CURRENT_USER).getChild(SOFTWARE, WINE, DIRECT3D, MULTISAMPLING);
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

        return defaultConfiguration.getMultisampling(registryFile);
    }

    @Override
    public OffscreenRenderingMode getOffscreenRenderingMode(File registryFile) {
        if(!registryFile.exists()) {
            return defaultConfiguration.getOffscreenRenderingMode(registryFile);
        }

        final AbstractRegistryNode registryChild = registryParser.parseFile(registryFile, HKEY_CURRENT_USER).getChild(SOFTWARE, WINE, DIRECT3D,
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
        return defaultConfiguration.getOffscreenRenderingMode(registryFile);
    }

    @Override
    public RenderTargetModeLock getRenderTargetModeLock(File registryFile) {
        if(!registryFile.exists()) {
            return defaultConfiguration.getRenderTargetModeLock(registryFile);
        }

        final AbstractRegistryNode registryChild = registryParser.parseFile(registryFile, HKEY_CURRENT_USER).getChild(SOFTWARE, WINE, DIRECT3D,
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

        return defaultConfiguration.getRenderTargetModeLock(registryFile);
    }

    @Override
    public StrictDrawOrdering getStrictDrawOrdering(File registryFile) {
        if(!registryFile.exists()) {
            return defaultConfiguration.getStrictDrawOrdering(registryFile);
        }

        final AbstractRegistryNode registryChild = registryParser.parseFile(registryFile, HKEY_CURRENT_USER).getChild(SOFTWARE, WINE, DIRECT3D,
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
        return defaultConfiguration.getStrictDrawOrdering(registryFile);
    }

    @Override
    public AlwaysOffscreen getAlwaysOffscreen(File registryFile) {
        if(!registryFile.exists()) {
            return defaultConfiguration.getAlwaysOffscreen(registryFile);
        }

        final AbstractRegistryNode registryChild = registryParser.parseFile(registryFile, HKEY_CURRENT_USER).getChild(SOFTWARE, WINE, DIRECT3D,
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
        return defaultConfiguration.getAlwaysOffscreen(registryFile);
    }

    @Override
    public VideoMemorySize getVideoMemorySize(File registryFile) {
        if(!registryFile.exists()) {
            return defaultConfiguration.getVideoMemorySize(registryFile);
        }

        final AbstractRegistryNode registryChild = registryParser.parseFile(registryFile, HKEY_CURRENT_USER).getChild(SOFTWARE, WINE, DIRECT3D,
                VIDEO_MEMORY_SIZE);
        if (registryChild instanceof RegistryValue) {
            try {
                int videoMemorySize = Integer.valueOf(((RegistryValue<?>) registryChild).getText());
                return new VideoMemorySize(false, videoMemorySize);
            } catch (NumberFormatException e) {
                return new VideoMemorySize(true, 0);
            }
        }

        return defaultConfiguration.getVideoMemorySize(registryFile);
    }
}
