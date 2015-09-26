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

import com.playonlinux.wine.parameters.*;
import com.playonlinux.wine.registry.AbstractRegistryNode;
import com.playonlinux.wine.registry.RegistryKey;
import com.playonlinux.wine.registry.RegistryValue;
import com.playonlinux.wine.registry.StringValueType;

public class RegistryWinePrefixDisplayConfiguration implements WinePrefixDisplayConfiguration {
    private final RegistryKey userRegsitry;

    public RegistryWinePrefixDisplayConfiguration(RegistryKey userRegistry) {
        this.userRegsitry = userRegistry;
    }

    @Override
    public GLSL getGLSL() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild("Software", "Wine", "Direct3D", "UseGLSL");
        if(registryChild instanceof RegistryValue) {
            switch (((RegistryValue<StringValueType>) registryChild).getText()) {
                case "enabled":
                    return GLSL.ENABLED;
                case "disabled":
                    return GLSL.DISABLED;
                default:
                    return GLSL.DEFAULT;
            }
        }
        return GLSL.DEFAULT;
    }

    @Override
    public DirectDrawRenderer getDirectDrawRenderer() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild("Software", "Wine", "Direct3D", "DirectDrawRenderer");
        if(registryChild instanceof RegistryValue) {
            switch (((RegistryValue<StringValueType>) registryChild).getText()) {
                case "gdi":
                    return DirectDrawRenderer.GDI;
                case "opengl":
                    return DirectDrawRenderer.OPENGL;
                default:
                    return DirectDrawRenderer.DEFAULT;
            }
        }
        return DirectDrawRenderer.DEFAULT;
    }

    @Override
    public Multisampling getMultisampling() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild("Software", "Wine", "Direct3D", "Multisampling");
        if(registryChild instanceof RegistryValue) {
            switch (((RegistryValue<StringValueType>) registryChild).getText()) {
                case "enabled":
                    return Multisampling.ENABLED;
                case "disabled":
                    return Multisampling.DISABLED;
                default:
                    return Multisampling.DEFAULT;
            }
        }
        return Multisampling.DEFAULT;
    }

    @Override
    public OffscreenRenderingMode getOffscreenRenderingMode() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild("Software", "Wine", "Direct3D", "OffscreenRenderingMode");
        if(registryChild instanceof RegistryValue) {
            switch (((RegistryValue<StringValueType>) registryChild).getText()) {
                case "fbo":
                    return OffscreenRenderingMode.FBO;
                case "backbuffer":
                    return OffscreenRenderingMode.BACKBUFFER;
                case "pbuffer":
                    return OffscreenRenderingMode.PBUFFER;
                default:
                    return OffscreenRenderingMode.DEFAULT;
            }
        }
        return OffscreenRenderingMode.DEFAULT;
    }

    @Override
    public RenderTargetModeLock getRenderTargetModeLock() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild("Software", "Wine", "Direct3D", "RenderTargetModeLock");
        if(registryChild instanceof RegistryValue) {
            switch (((RegistryValue<StringValueType>) registryChild).getText()) {
                case "disabled":
                    return RenderTargetModeLock.DISABLED;
                case "readdraw":
                    return RenderTargetModeLock.READDRAW;
                case "readtex":
                    return RenderTargetModeLock.READTEX;
                default:
                    return RenderTargetModeLock.DEFAULT;
            }
        }
        return RenderTargetModeLock.DEFAULT;
    }

    @Override
    public StrictDrawOrdering getStrictDrawOrdering() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild("Software", "Wine", "Direct3D", "StrictDrawOrdering");
        if(registryChild instanceof RegistryValue) {
            switch (((RegistryValue<StringValueType>) registryChild).getText()) {
                case "enabled":
                    return StrictDrawOrdering.ENABLED;
                case "disabled":
                    return StrictDrawOrdering.DISABLED;
                default:
                    return StrictDrawOrdering.DEFAULT;
            }
        }
        return StrictDrawOrdering.DEFAULT;
    }

    @Override
    public AlwaysOffscreen getAlwaysOffscreen() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild("Software", "Wine", "Direct3D", "AlwaysOffscreen");
        if(registryChild instanceof RegistryValue) {
            switch (((RegistryValue<StringValueType>) registryChild).getText()) {
                case "enabled":
                    return AlwaysOffscreen.ENABLED;
                case "disabled":
                    return AlwaysOffscreen.DISABLED;
                default:
                    return AlwaysOffscreen.DEFAULT;
            }
        }
        return AlwaysOffscreen.DEFAULT;
    }

    @Override
    public VideoMemorySize getVideoMemorySize() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild("Software", "Wine", "Direct3D", "VideoMemorySize");
        if(registryChild instanceof RegistryValue) {
            try {
                int videoMemorySize = Integer.valueOf(((RegistryValue<StringValueType>) registryChild).getText());
                return new VideoMemorySize(false, videoMemorySize);
            } catch (NumberFormatException e) {
                return new VideoMemorySize(true, 0);
            }
        }
        return new VideoMemorySize(true, 0);
    }
}
