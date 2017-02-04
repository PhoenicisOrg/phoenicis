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

package com.phoenicis.containers.wine.parameters;

import com.phoenicis.win32.registry.*;

public class VideoMemorySize implements RegistryParameter {
    private final boolean isDefault;
    private final int videoSize;

    public VideoMemorySize(boolean isDefault, int videoSize) {
        this.isDefault = isDefault;
        this.videoSize = videoSize;
    }

    public static VideoMemorySize[] possibleValues() {
        return new VideoMemorySize[]{
                new VideoMemorySize(true, 0),
                new VideoMemorySize(false, 32),
                new VideoMemorySize(false, 64),
                new VideoMemorySize(false, 128),
                new VideoMemorySize(false, 256),
                new VideoMemorySize(false, 384),
                new VideoMemorySize(false, 512),
                new VideoMemorySize(false, 768),
                new VideoMemorySize(false, 1024),
                new VideoMemorySize(false, 2048),
                new VideoMemorySize(false, 3072),
                new VideoMemorySize(false, 4096),
                new VideoMemorySize(false, 5120),
                new VideoMemorySize(false, 6144),
                new VideoMemorySize(false, 7168),
                new VideoMemorySize(false, 8192)
        };
    }

    public int getVideoSize() {
        return videoSize;
    }

    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        if (isDefault) {
            return "Default";
        } else {
            return Integer.toString(videoSize);
        }
    }

    @Override
    public AbstractRegistryNode toRegistryPatch() {
        final RegistryKey registryNode
                = new RegistryKey("HKEY_CURRENT_USER")
                .addDeepChildren("Software", "Wine", "Direct3D");

        if (isDefault) {
            registryNode.addChild(new RegistryValue<>("VideoMemorySize", new RemoveValueType()));
        } else {
            registryNode.addChild(new RegistryValue<>("VideoMemorySize", new StringValueType(Integer.toString(videoSize))));
        }

        return registryNode;
    }
}
