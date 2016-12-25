package com.playonlinux.containers.dto;

import com.playonlinux.containers.wine.parameters.*;

public class WinePrefixDTO extends ContainerDTO {
    private final String architecture;
    private final String distribution;
    private final String version;
    private final GLSL glslValue;
    private final DirectDrawRenderer directDrawRenderer;
    private final VideoMemorySize videoMemorySize;
    private final OffscreenRenderingMode offscreenRenderingMode;
    private final Multisampling multisampling;
    private final AlwaysOffscreen alwaysOffscreen;
    private final StrictDrawOrdering strictDrawOrdering;
    private final RenderTargetModeLock renderTargetModeLock;
    private final MouseWarpOverride mouseWarpOverride;

    private WinePrefixDTO(Builder builder) {
        super(builder.name, builder.path, ContainerType.WINEPREFIX);
        this.architecture = builder.architecture;
        this.distribution = builder.distribution;
        this.version = builder.version;
        this.glslValue = builder.glslValue;
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
        private GLSL glslValue;
        private DirectDrawRenderer directDrawRenderer;
        private VideoMemorySize videoMemorySize;
        private OffscreenRenderingMode offscreenRenderingMode;
        private Multisampling multisampling;
        private AlwaysOffscreen alwaysOffscreen;
        private StrictDrawOrdering strictDrawOrdering;
        private RenderTargetModeLock renderTargetModeLock;
        private MouseWarpOverride mouseWarpOverride;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
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

        public Builder withGlslValue(GLSL glslValue) {
            this.glslValue = glslValue;
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

        public WinePrefixDTO build() {
            return new WinePrefixDTO(this);
        }
    }
}
