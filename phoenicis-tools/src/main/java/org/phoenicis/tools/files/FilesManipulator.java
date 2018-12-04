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

package org.phoenicis.tools.files;

import org.phoenicis.configuration.security.Safe;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

import static java.lang.String.format;

@Safe
class FilesManipulator {
    @Value("${application.user.root}")
    private String userRoot;

    private boolean isInSubDirectory(File directory, File fileIside) {
        return fileIside != null
                && (fileIside.equals(directory) || isInSubDirectory(directory, fileIside.getParentFile()));
    }

    void assertInDirectory(File file) {
        if (!isInSubDirectory(new File(userRoot), file)) {
            throw new IllegalArgumentException(
                    format("The file (%s) must be in the Phoenicis root directory (%s)", file, userRoot));
        }
    }

}
