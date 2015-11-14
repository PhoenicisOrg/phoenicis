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

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Sets;
import com.playonlinux.app.PlayOnLinuxContext;
import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;

@Scan
public final class Files {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    private Files() {
        // Utility class
    }

    /**
     * Delete a file only if it is inside PlayOnLinux root
     * 
     * @param fileToDelete
     *            fileOrDirectoryToDelete
     */
    public static void remove(File fileToDelete) throws IOException {
        final File userRoot = playOnLinuxContext.makeUserRootPath();

        if (!isInSubDirectory(userRoot, fileToDelete)) {
            throw new IllegalArgumentException(
                    format("The file (%s) must be in a the PlayOnLinux root directory (%s)", fileToDelete, userRoot));
        }

        FileUtils.deleteDirectory(fileToDelete);
    }

    public static boolean isInSubDirectory(File directory, File fileIside) {
        return fileIside != null
                && (fileIside.equals(directory) || isInSubDirectory(directory, fileIside.getParentFile()));
    }

    private static Set<PosixFilePermission> singleIntToFilePermission(Integer mode, String groupType) {
        Set<PosixFilePermission> permissions = new HashSet<>(9);

        if (Arrays.asList(new Integer[] { 1, 3, 5, 7 }).contains(mode)) {
            permissions.add(PosixFilePermission.valueOf(groupType + "_EXECUTE"));
        }

        if (Arrays.asList(new Integer[] { 2, 3, 6, 7 }).contains(mode)) {
            permissions.add(PosixFilePermission.valueOf(groupType + "_WRITE"));
        }

        if (Arrays.asList(new Integer[] { 4, 5, 6, 7 }).contains(mode)) {
            permissions.add(PosixFilePermission.valueOf(groupType + "_READ"));
        }

        return permissions;
    }

    public static Set<PosixFilePermission> intToPosixFilePermission(int mode) {
        if (mode >= 1000 || mode < 0) {
            throw new IllegalArgumentException("Invalid mode " + mode);
        }

        final int owner = mode / 100;
        final int group = (mode - owner * 100) / 10;
        final int others = mode - owner * 100 - group * 10;

        if (owner > 7 || group > 7 || others > 7) {
            throw new IllegalArgumentException("Invalid mode " + mode);
        }

        return Sets.union(
                Sets.union(singleIntToFilePermission(owner, "OWNER"), singleIntToFilePermission(group, "GROUP")),
                singleIntToFilePermission(others, "OTHERS"));
    }

    public static Set<PosixFilePermission> octToPosixFilePermission(int modeOct) {
        // TODO: optimize this method and make it cleaner
        int modeInt = Integer.parseInt(Integer.toString(modeOct, 8));

        return intToPosixFilePermission(modeInt);
    }
}
