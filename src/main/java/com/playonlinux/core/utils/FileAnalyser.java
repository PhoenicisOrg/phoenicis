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

import com.playonlinux.app.PlayOnLinuxException;
import net.sf.jmimemagic.*;
import org.apache.commons.io.FileUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileAnalyser {
    private FileAnalyser() {
        // Utility class
    }

    public static String getMimetype(File inputFile) throws PlayOnLinuxException {
        final Path path = Paths.get(inputFile.getAbsolutePath());

        try {
            byte[] data = Files.readAllBytes(path);
            MagicMatch match = Magic.getMagicMatch(data);
            return match.getMimeType();
        } catch (MagicMatchNotFoundException e) {
            final MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
            return mimeTypesMap.getContentType(inputFile);
        } catch (MagicException | MagicParseException | IOException e) {
            throw new PlayOnLinuxException("Unable to detect mimetype of the file", e);
        }
    }


    /**
     * Identify which line delimiter is used in a file
     * @param fileContent string to analyse
     * @return the line separator as a string. Null if the file has no line separator
     * @throws IOException if the file cannot be read
     */
    public static String identifyLineDelimiter(String fileContent) {
        if (fileContent.matches("(?s).*(\\r\\n).*")) {     //Windows //$NON-NLS-1$
            return "\r\n"; //$NON-NLS-1$
        } else if (fileContent.matches("(?s).*(\\n).*")) { //Unix/Linux //$NON-NLS-1$
            return "\n"; //$NON-NLS-1$
        } else if (fileContent.matches("(?s).*(\\r).*")) { //Legacy mac os 9. Newer OS X use \n //$NON-NLS-1$
            return "\r"; //$NON-NLS-1$
        } else {
            return "\n";  //fallback onto '\n' if nothing matches. //$NON-NLS-1$
        }
    }

    public static String identifyLineDelimiter(File fileToAnalyse) throws IOException {
        final String fileContent = FileUtils.readFileToString(fileToAnalyse);
        return identifyLineDelimiter(fileContent);
    }
}
