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

package com.playonlinux.engines.wine;

import com.playonlinux.utils.Architecture;
import com.playonlinux.utils.OperatingSystem;
import com.playonlinux.version.Version;
import org.apache.commons.lang.builder.ToStringBuilder;

import static java.lang.String.format;

public class WineVersionCoordinates {
    public Version getVersion() {
        return version;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public String getDistribution() {
        return distribution;
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    private final Version version;
    private final String distribution;
    private final Architecture architecture;
    private final OperatingSystem operatingSystem;

    public WineVersionCoordinates(Version version, String distribution, Architecture architecture, OperatingSystem operatingSystem) {
        this.version = version;
        this.distribution = distribution;
        this.architecture = architecture;
        this.operatingSystem = operatingSystem;
    }

    public String asName() {
        return format("%s-%s-%s", distribution, operatingSystem.getNameForWinePackages(), architecture.getNameForWinePackages());
    }

    public String toString() {
        return new ToStringBuilder(WineVersionCoordinates.class)
                .append(version)
                .append(distribution)
                .append(architecture)
                .append(operatingSystem)
                .toString();
    }
}
