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

package com.playonlinux.core.version;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents a version
 */
public class Version {

    final int bigNumber;
    final int intermediateNumber;
    final int lowNumber;

    public Version(String versionAsString) {
        String[] splitVersionAsString = versionAsString.split("\\.");
        bigNumber = Integer.valueOf(splitVersionAsString[0]);
        intermediateNumber = Integer.valueOf(splitVersionAsString[1]);
        lowNumber = Integer.valueOf(splitVersionAsString[2]);
    }

    public int getBigNumber() {
        return bigNumber;
    }

    public int getIntermediateNumber() {
        return intermediateNumber;
    }

    public int getLowNumber() {
        return lowNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Version version = (Version) o;

        return new EqualsBuilder()
                .append(bigNumber, version.bigNumber)
                .append(intermediateNumber, version.intermediateNumber)
                .append(lowNumber, version.lowNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(bigNumber)
                .append(intermediateNumber)
                .append(lowNumber)
                .toHashCode();
    }

    public String toString() {
        return String.format("%s.%s.%s", bigNumber, intermediateNumber, lowNumber);
    }
}
