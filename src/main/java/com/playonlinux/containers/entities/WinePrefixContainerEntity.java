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

package com.playonlinux.containers.entities;

import com.playonlinux.wine.parameters.*;

import static com.playonlinux.core.lang.Localisation.translate;

public class WinePrefixContainerEntity extends ContainerEntity {
    private final String wineVersion;
    private final String wineDistribution;
    private final String wineArchitecture;
    private final boolean isAutomaticallyUpdated;
    private final GLSL glslValue;
    private final Multisampling multisampling;
    private final OffscreenRenderingMode offscreenRenderingMode;
    private final RenderTargetModeLock renderTargetModeLock;
    private final StrictDrawOrdering strictDrawOrdering;
    private final VideoMemorySize videoMemorySize;
    private final DirectDrawRenderer directDrawRenderer;
    private final AlwaysOffscreen alwaysOffscreen;
    private final MouseWarpOverride mouseWarpOverride;

    private WinePrefixContainerEntity(Builder builder) {
        super(builder.name, builder.path);
        this.wineArchitecture = builder.wineArchitecture;
        this.wineDistribution = builder.wineDistribution;
        this.wineVersion = builder.wineVersion;
        this.isAutomaticallyUpdated = builder.isAutomaticallyUpdated;
        this.glslValue = builder.glslValue;
        this.multisampling = builder.multisampling;
        this.offscreenRenderingMode = builder.offscreenRenderingMode;
        this.renderTargetModeLock = builder.renderTargetModeLock;
        this.strictDrawOrdering = builder.strictDrawOrdering;
        this.videoMemorySize = builder.videoMemorySize;
        this.directDrawRenderer = builder.directDrawRenderer;
        this.alwaysOffscreen = builder.alwaysOffscreen;
        this.mouseWarpOverride = builder.mouseWarpOverride;
    }

    /* General */
    public String getWineArchitecture() {
        return wineArchitecture;
    }

    public String getWineDistribution() {
        return wineDistribution;
    }

    public String getWineVersion() {
        return wineVersion;
    }

    /* Graphics */
    public GLSL getGlslValue() {
        return glslValue;
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

    public RenderTargetModeLock getRenderTargetModeLock() {
        return renderTargetModeLock;
    }

    public StrictDrawOrdering getStrictDrawOrdering() {
        return strictDrawOrdering;
    }

    public Multisampling getMultisampling() {
        return multisampling;
    }

    public AlwaysOffscreen getAlwaysOffscreen() {
        return alwaysOffscreen;
    }

    /* Input */
    public MouseWarpOverride getMouseWarpOverride() {
        return mouseWarpOverride;
    }

    public static class Builder {
        private String wineVersion;
        private String wineDistribution;
        private String wineArchitecture;
        private String path;
        private String name;
        public boolean isAutomaticallyUpdated;
        private GLSL glslValue = GLSL.DEFAULT;
        private Multisampling multisampling = Multisampling.DEFAULT;
        private OffscreenRenderingMode offscreenRenderingMode = OffscreenRenderingMode.DEFAULT;
        private RenderTargetModeLock renderTargetModeLock = RenderTargetModeLock.DEFAULT;
        private StrictDrawOrdering strictDrawOrdering = StrictDrawOrdering.DEFAULT;
        private VideoMemorySize videoMemorySize = new VideoMemorySize(true, 0);
        private DirectDrawRenderer directDrawRenderer = DirectDrawRenderer.DEFAULT;
        private AlwaysOffscreen alwaysOffscreen = AlwaysOffscreen.DEFAULT;
        private MouseWarpOverride mouseWarpOverride = MouseWarpOverride.DEFAULT;

        public WinePrefixContainerEntity build() {
            return new WinePrefixContainerEntity(this);
        }

        public Builder withWineVersion(String wineVersion) {
            this.wineVersion = wineVersion;
            return this;
        }

        public Builder withWineDistribution(String wineDistribution) {
            this.wineDistribution = wineDistribution;
            return this;
        }

        public Builder withWineArchitecture(String wineArchitecture) {
            this.wineArchitecture = wineArchitecture;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withAutomaticallyUpdated(boolean isAutomaticallyUpdated) {
            this.isAutomaticallyUpdated = isAutomaticallyUpdated;
            return this;
        }

        public Builder withGLSL(GLSL glslValue) {
            this.glslValue = glslValue;
            return this;
        }

        public Builder withMultisampling(Multisampling multisampling) {
            this.multisampling = multisampling;
            return this;
        }

        public Builder withOffscreenRenderingMode(OffscreenRenderingMode offscreenRenderingMode) {
            this.offscreenRenderingMode = offscreenRenderingMode;
            return this;
        }

        public Builder withRenderTargetModeLock(RenderTargetModeLock renderTargetModeLock) {
            this.renderTargetModeLock = renderTargetModeLock;
            return this;
        }

        public Builder withStrictDrawOrdering(StrictDrawOrdering strictDrawOrdering) {
            this.strictDrawOrdering = strictDrawOrdering;
            return this;
        }

        public Builder withVideoMemorySize(VideoMemorySize videoMemorySize) {
            this.videoMemorySize = videoMemorySize;
            return this;
        }

        public Builder withDirectDrawRenderer(DirectDrawRenderer directDrawRenderer) {
            this.directDrawRenderer = directDrawRenderer;
            return this;
        }

        public Builder withAlwaysOffscreen(AlwaysOffscreen alwaysOffscreen) {
            this.alwaysOffscreen = alwaysOffscreen;
            return this;
        }

        public Builder withMouseWarpOverride(MouseWarpOverride mouseWarpOverride) {
            this.mouseWarpOverride = mouseWarpOverride;
            return this;
        }
    }
}
