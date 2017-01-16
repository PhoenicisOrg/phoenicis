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

package com.playonlinux.tools.version;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents a version
 */
public class Version {
    private final int bigNumber;
    private final int intermediateNumber;
    private final int lowNumber;
    private final String customName;

    public Version(String completeVersionAsString) {
        final String[] splitCompleteVersion = completeVersionAsString.split("-");
        final String versionAsString = splitCompleteVersion[0];

        if(splitCompleteVersion.length > 1) {
            customName = splitCompleteVersion[1].trim();
        } else {
            customName = "";
        }

        final String[] splitVersion = versionAsString.split("\\.");
        bigNumber = readInteger(splitVersion[0]);

        if(splitVersion.length <= 1) {
            intermediateNumber = 0;
        } else {
            intermediateNumber = readInteger(splitVersion[1]);
        }

        if(splitVersion.length <= 2) {
            lowNumber = 0;
        } else {
            lowNumber = readInteger(splitVersion[2]);
        }
    }

    private int readInteger(String number) {
        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException e) {
            return 0;
        }
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

    public String getCustomName() { return customName; }

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
                .append(customName, version.customName)
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

    @Override
    public String toString() {
        if(StringUtils.isBlank(customName)) {
            if (lowNumber == 0) {
                return String.format("%s.%s", bigNumber, intermediateNumber);
            }
            return String.format("%s.%s.%s", bigNumber, intermediateNumber, lowNumber);
        } else {
            if (lowNumber == 0) {
                return String.format("%s.%s-%s", bigNumber, intermediateNumber, customName);
            }
            return String.format("%s.%s.%s-%s", bigNumber, intermediateNumber, lowNumber, customName);
        }
    }
}
