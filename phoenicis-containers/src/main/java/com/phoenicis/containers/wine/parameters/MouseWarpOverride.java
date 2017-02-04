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

import static com.phoenicis.configuration.localisation.Localisation.translate;

public enum MouseWarpOverride implements RegistryParameter {
    DEFAULT(translate("Default"), ""),
    DISABLED(translate("Disabled"), "disabled"),
    ENABLED(translate("Enabled"), "enabled"),
    FORCE(translate("Force"), "force");

    private final String translatedName;
    private final String registryValue;

    MouseWarpOverride(String translatedName, String registryValue) {
        this.translatedName = translatedName;
        this.registryValue = registryValue;
    }

    @Override
    public String toString() {
        return translatedName;
    }

    @Override
    public AbstractRegistryNode toRegistryPatch() {
        final RegistryKey registryNode
                = new RegistryKey("HKEY_CURRENT_USER")
                .addDeepChildren("Software", "Wine", "DirectInput");

        switch (this) {
            case DEFAULT:
                registryNode.addChild(new RegistryValue<>("MouseWarpOverride", new RemoveValueType()));
                break;
            case ENABLED:
            case DISABLED:
            case FORCE:
                registryNode.addChild(new RegistryValue<>("MouseWarpOverride", new StringValueType(registryValue)));
                break;
        }

        return registryNode;
    }
}
