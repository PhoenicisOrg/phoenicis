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

package org.phoenicis.containers.dto;

import org.phoenicis.containers.wine.parameters.*;
import org.phoenicis.library.dto.ShortcutDTO;

import java.util.List;

public class WinePrefixContainerDTO extends ContainerDTO {
    private final String architecture;
    private final String distribution;
    private final String version;
    private final UseGLSL useGlslValue;
    private final DirectDrawRenderer directDrawRenderer;
    private final VideoMemorySize videoMemorySize;
    private final OffscreenRenderingMode offscreenRenderingMode;
    private final Multisampling multisampling;
    private final AlwaysOffscreen alwaysOffscreen;
    private final StrictDrawOrdering strictDrawOrdering;
    private final RenderTargetModeLock renderTargetModeLock;
    private final MouseWarpOverride mouseWarpOverride;

    private WinePrefixContainerDTO(Builder builder) {
        super(builder.name, builder.path, ContainerType.WINEPREFIX, builder.installedShortcuts);
        this.architecture = builder.architecture;
        this.distribution = builder.distribution;
        this.version = builder.version;
        this.useGlslValue = builder.useGlslValue;
        this.directDrawRenderer = builder.directDrawRenderer;
        this.videoMemorySize = builder.videoMemorySize;
        this.offscreenRenderingMode = builder.offscreenRenderingMode;
        this.multisampling = builder.multisampling;
        this.alwaysOffscreen = builder.alwaysOffscreen;
        this.strictDrawOrdering = builder.strictDrawOrdering;
        this.renderTargetModeLock = builder.renderTargetModeLock;
        this.mouseWarpOverride = builder.mouseWarpOverride;
    }

    public String getArchitecture() {
        return architecture;
    }

    public String getDistribution() {
        return distribution;
    }

    public String getVersion() {
        return version;
    }

    public UseGLSL getUseGlslValue() {
        return useGlslValue;
    }

    public DirectDrawRenderer getDirectDrawRenderer() {
        return directDrawRenderer;
    }

    public VideoMemorySize getVideoMemorySize() {
        return videoMemorySize;
    }

    public OffscreenRenderingMode getOffscreenRenderingMode() {
        return offscreenRenderingMode;
    }

    public Multisampling getMultisampling() {
        return multisampling;
    }

    public AlwaysOffscreen getAlwaysOffscreen() {
        return alwaysOffscreen;
    }

    public StrictDrawOrdering getStrictDrawOrdering() {
        return strictDrawOrdering;
    }

    public RenderTargetModeLock getRenderTargetModeLock() {
        return renderTargetModeLock;
    }

    public MouseWarpOverride getMouseWarpOverride() {
        return mouseWarpOverride;
    }

    public static class Builder {
        private String name;
        private String path;
        private String architecture;
        private String distribution;
        private String version;
        private UseGLSL useGlslValue;
        private DirectDrawRenderer directDrawRenderer;
        private VideoMemorySize videoMemorySize;
        private OffscreenRenderingMode offscreenRenderingMode;
        private Multisampling multisampling;
        private AlwaysOffscreen alwaysOffscreen;
        private StrictDrawOrdering strictDrawOrdering;
        private RenderTargetModeLock renderTargetModeLock;
        private MouseWarpOverride mouseWarpOverride;
        private List<ShortcutDTO> installedShortcuts;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withInstalledShortcuts(List<ShortcutDTO> installedShortcuts) {
            this.installedShortcuts = installedShortcuts;
            return this;
        }

        public Builder withArchitecture(String architecture) {
            this.architecture = architecture;
            return this;
        }

        public Builder withDistribution(String distribution) {
            this.distribution = distribution;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder withGlslValue(UseGLSL useGlslValue) {
            this.useGlslValue = useGlslValue;
            return this;
        }

        public Builder withDirectDrawRenderer(DirectDrawRenderer directDrawRenderer) {
            this.directDrawRenderer = directDrawRenderer;
            return this;
        }

        public Builder withVideoMemorySize(VideoMemorySize videoMemorySize) {
            this.videoMemorySize = videoMemorySize;
            return this;
        }

        public Builder withOffscreenRenderingMode(OffscreenRenderingMode offscreenRenderingMode) {
            this.offscreenRenderingMode = offscreenRenderingMode;
            return this;
        }

        public Builder withMultisampling(Multisampling multisampling) {
            this.multisampling = multisampling;
            return this;
        }

        public Builder withAlwaysOffscreen(AlwaysOffscreen alwaysOffscreen) {
            this.alwaysOffscreen = alwaysOffscreen;
            return this;
        }

        public Builder withStrictDrawOrdering(StrictDrawOrdering strictDrawOrdering) {
            this.strictDrawOrdering = strictDrawOrdering;
            return this;
        }

        public Builder withRenderTargetModeLock(RenderTargetModeLock renderTargetModeLock) {
            this.renderTargetModeLock = renderTargetModeLock;
            return this;
        }

        public Builder withMouseWarpOverride(MouseWarpOverride mouseWarpOverride) {
            this.mouseWarpOverride = mouseWarpOverride;
            return this;
        }

        public ContainerDTO build() {
            return new WinePrefixContainerDTO(this);
        }
    }
}
