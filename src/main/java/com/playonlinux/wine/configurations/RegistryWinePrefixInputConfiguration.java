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

import com.playonlinux.wine.parameters.MouseWarpOverride;
import com.playonlinux.wine.registry.AbstractRegistryNode;
import com.playonlinux.wine.registry.RegistryKey;
import com.playonlinux.wine.registry.RegistryValue;
import com.playonlinux.wine.registry.StringValueType;

public class RegistryWinePrefixInputConfiguration implements WinePrefixInputConfiguration {
    private final RegistryKey userRegsitry;

    public RegistryWinePrefixInputConfiguration(RegistryKey userRegistry) {
        this.userRegsitry = userRegistry;
    }

    @Override
    public MouseWarpOverride getMouseWarpOverride() {
        final AbstractRegistryNode registryChild = this.userRegsitry.getChild("Software", "Wine", "DirectInput",
                "MouseWarpOverride");
        if (registryChild instanceof RegistryValue) {
            switch (((RegistryValue<StringValueType>) registryChild).getText()) {
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
        return MouseWarpOverride.DEFAULT;
    }
}
