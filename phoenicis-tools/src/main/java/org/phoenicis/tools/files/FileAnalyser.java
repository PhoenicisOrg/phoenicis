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

import net.sf.jmimemagic.*;
import org.apache.commons.io.FileUtils;
import org.phoenicis.configuration.security.Safe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Safe
public class FileAnalyser {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileAnalyser.class);

    /**
     * Identify which line delimiter is used in a file
     *
     * @param fileContent string to analyse
     * @return the line separator as a string. Null if the file has no line
     *         separator
     */
    public static String identifyLineDelimiter(String fileContent) {
        if (fileContent.matches("(?s).*(\\r\\n).*")) { // Windows //$NON-NLS-1$
            return "\r\n"; //$NON-NLS-1$
        } else if (fileContent.matches("(?s).*(\\n).*")) { // Unix/Linux //$NON-NLS-1$
            return "\n"; //$NON-NLS-1$
        } else if (fileContent.matches("(?s).*(\\r).*")) { // Legacy mac os 9. Newer OS X use \n //$NON-NLS-1$
            return "\r"; //$NON-NLS-1$
        } else {
            return "\n"; // fallback onto '\n' if nothing matches. //$NON-NLS-1$
        }
    }

    public static String identifyLineDelimiter(File fileToAnalyse) throws IOException {
        final String fileContent = FileUtils.readFileToString(fileToAnalyse);
        return identifyLineDelimiter(fileContent);
    }

    private MagicMatch getMatch(File inputFile) throws MagicMatchNotFoundException {
        final Path path = Paths.get(inputFile.getAbsolutePath());

        try {
            byte[] data = Files.readAllBytes(path);
            return Magic.getMagicMatch(data, true);
        } catch (MagicException | MagicParseException | IOException e) {
            throw new IllegalStateException("Unable to detect mimetype of the file", e);
        }
    }

    public String getDescription(File inputFile) {
        try {
            return getMatch(inputFile).getDescription();
        } catch (MagicMatchNotFoundException e) {
            throw new IllegalStateException("Unable to detect mimetype of the file", e);
        }
    }

    public String getMimetype(File inputFile) {
        try {
            final MagicMatch match = getMatch(inputFile);
            final String mimeType = match.getMimeType();
            if ("???".equals(mimeType)) {
                return guessMimeTypeFromDescription(match);
            }

            return mimeType;
        } catch (MagicMatchNotFoundException e) {
            LOGGER.debug("Failed to get Mime Type", e);
            return probeMimeType(inputFile);
        }
    }

    private String probeMimeType(File inputFile) {
        try {
            final String mimeType = Files.probeContentType(inputFile.toPath());
            if (mimeType != null) {
                return mimeType;
            }
        } catch (IOException ioException) {
            LOGGER.debug("Unable to probe Mime Type from file system metadata", ioException);
        }

        return "application/octet-stream";
    }

    private String guessMimeTypeFromDescription(MagicMatch match) {
        if ("MS-DOS executable (EXE)".equals(match.getDescription())) {
            return "application/x-dosexec";
        }

        return "???";
    }
}
