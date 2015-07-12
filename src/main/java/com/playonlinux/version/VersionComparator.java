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

package com.playonlinux.version;

import java.util.Comparator;

/**
 * Implements a comparator for the version object
 */
public class VersionComparator implements Comparator<Version> {

    @Override
    public int compare(Version version1, Version version2) {
        if(version1.equals(version2)) {
            return 0;
        }
        if(version1.getBigNumber() > version2.getBigNumber()) {
            return 1;
        } else if(version1.getBigNumber() == version2.getBigNumber()) {
            if(version1.getIntermediateNumber() > version2.getIntermediateNumber()) {
                return 1;
            } else if(version1.getIntermediateNumber() == version2.getIntermediateNumber()) {
                if(version1.getLowNumber() > version2.getLowNumber()) {
                    return 1;
                } else if (version1.getLowNumber() == version2.getLowNumber()) {
                    return 0;
                } else {
                    return 2;
                }
            } else {
                return 2;
            }
        } else {
            return 2;
        }
    }
}