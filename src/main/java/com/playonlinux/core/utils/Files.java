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

package com.playonlinux.core.utils;

import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static java.lang.String.format;

@Scan
public final class Files {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    private Files() {
        // Utility class
    }

    /**
     * Delete a file only if it is inside PlayOnLinux root
     * @param fileToDelete fileOrDirectoryToDelete
     */
    public static void remove(File fileToDelete) throws IOException {
        final File userRoot = playOnLinuxContext.makeUserRootPath();

        if(!isInSubDirectory(userRoot, fileToDelete)) {
            throw new IllegalArgumentException(format("The file (%s) must be in a the PlayOnLinux root directory (%s)",
                    fileToDelete, userRoot));
        }

        FileUtils.deleteDirectory(fileToDelete);
    }

    public static boolean isInSubDirectory(File directory, File fileIside) {
        return fileIside != null && (fileIside.equals(directory) || isInSubDirectory(directory, fileIside.getParentFile()));
    }

}
