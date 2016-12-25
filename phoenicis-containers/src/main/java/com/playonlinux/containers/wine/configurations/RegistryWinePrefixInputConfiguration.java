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

import com.playonlinux.containers.wine.parameters.MouseWarpOverride;
import com.playonlinux.win32.registry.AbstractRegistryNode;
import com.playonlinux.win32.registry.RegistryParser;
import com.playonlinux.win32.registry.RegistryValue;

import java.io.File;

public class RegistryWinePrefixInputConfiguration implements WinePrefixInputConfiguration {
    private final RegistryParser registryParser;
    private final WinePrefixInputConfiguration defaultConfiguration;

    public RegistryWinePrefixInputConfiguration(RegistryParser registryParser, WinePrefixInputConfiguration defaultConfiguration) {
        this.registryParser = registryParser;
        this.defaultConfiguration = defaultConfiguration;
    }

    @Override
    public MouseWarpOverride getMouseWarpOverride(File registryFile) {
        if(!registryFile.exists()) {
            return defaultConfiguration.getMouseWarpOverride(registryFile);
        }
        final AbstractRegistryNode registryChild = registryParser.parseFile(registryFile, "HKEY_CURRENT_USER").getChild("Software", "Wine", "DirectInput",
                "MouseWarpOverride");
        if (registryChild instanceof RegistryValue) {
            switch (((RegistryValue<?>) registryChild).getText()) {
                case "enable":
                    return MouseWarpOverride.ENABLED;
                case "disable":
                    return MouseWarpOverride.DISABLED;
                case "force":
                    return MouseWarpOverride.FORCE;
                default:
                    return MouseWarpOverride.DEFAULT;
            }
        }
        return defaultConfiguration.getMouseWarpOverride(registryFile);
    }
}
