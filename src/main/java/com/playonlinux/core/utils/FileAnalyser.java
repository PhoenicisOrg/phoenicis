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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.playonlinux.app.PlayOnLinuxException;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

public final class FileAnalyser {
    private static final Logger LOGGER = Logger.getLogger(FileAnalyser.class);

    private FileAnalyser() {
        // Utility class
    }

    private static MagicMatch getMatch(File inputFile) throws PlayOnLinuxException, MagicMatchNotFoundException {
        final Path path = Paths.get(inputFile.getAbsolutePath());

        try {
            byte[] data = Files.readAllBytes(path);
            return Magic.getMagicMatch(data);
        } catch (MagicException | MagicParseException | IOException e) {
            throw new PlayOnLinuxException("Unable to detect mimetype of the file", e);
        }
    }

    public static String getDescription(File inputFile) throws PlayOnLinuxException {
        try {
            return getMatch(inputFile).getDescription();
        } catch (MagicMatchNotFoundException e) {
            throw new PlayOnLinuxException("Unable to detect mimetype of the file", e);
        }
    }

    public static String getMimetype(File inputFile) throws PlayOnLinuxException {
        try {
            return getMatch(inputFile).getMimeType();
        } catch (MagicMatchNotFoundException e) {
            LOGGER.debug(e);
            final MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
            return mimeTypesMap.getContentType(inputFile);
        }
    }


    /**
     * Identify which line delimiter is used in a file
     * @param fileContent string to analyse
     * @return the line separator as a string. Null if the file has no line separator
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
