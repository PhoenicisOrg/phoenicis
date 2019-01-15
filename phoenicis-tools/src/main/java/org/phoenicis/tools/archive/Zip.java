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

package org.phoenicis.tools.archive;

import com.google.common.io.CountingInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.entities.ProgressEntity;
import org.phoenicis.tools.stream.CursorFinderInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@Safe
public class Zip {
    private static final String ZIP_ERROR_MESSAGE = "Unable to open input stream";
    private static final byte[] ZIP_MAGICK_BYTE = new byte[] { 0x50, 0x4B, 0x03, 0x04 };
    private final Logger LOGGER = LoggerFactory.getLogger(Zip.class);

    List<File> uncompressZipFile(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback) {
        try (CountingInputStream inputStream = new CountingInputStream(new FileInputStream(inputFile))) {
            final long finalSize = FileUtils.sizeOf(inputFile);
            List<File> files = uncompress(inputStream, inputStream, outputDir, finalSize, stateCallback);

            return files;
        } catch (IOException e) {
            throw new ArchiveException(ZIP_ERROR_MESSAGE, e);
        }
    }

    /**
     * Uncompress a tar
     *
     * @param countingInputStream to count the number of byte extracted
     * @param outputDir The directory where files should be extracted
     * @return A list of extracted files
     * @throws ArchiveException if the process fails
     */
    private List<File> uncompress(final InputStream inputStream, CountingInputStream countingInputStream,
            final File outputDir, long finalSize, Consumer<ProgressEntity> stateCallback) {
        final List<File> uncompressedFiles = new LinkedList<>();
        try (InputStream cursorInputStream = new CursorFinderInputStream(inputStream, ZIP_MAGICK_BYTE);
                ArchiveInputStream debInputStream = new ArchiveStreamFactory().createArchiveInputStream("zip",
                        cursorInputStream)) {
            ZipArchiveEntry entry;
            while ((entry = (ZipArchiveEntry) debInputStream.getNextEntry()) != null) {
                final File outputFile = new File(outputDir, entry.getName());
                if (entry.isDirectory()) {
                    LOGGER.info(
                            String.format("Attempting to write output directory %s.", outputFile.getAbsolutePath()));

                    if (!outputFile.exists()) {
                        LOGGER.info(String.format("Attempting to createPrefix output directory %s.",
                                outputFile.getAbsolutePath()));
                        Files.createDirectories(outputFile.toPath());
                    }
                } else {
                    LOGGER.info(String.format("Creating output file %s.", outputFile.getAbsolutePath()));
                    outputFile.getParentFile().mkdirs();
                    try (final OutputStream outputFileStream = new FileOutputStream(outputFile)) {
                        IOUtils.copy(debInputStream, outputFileStream);
                    }

                }
                uncompressedFiles.add(outputFile);

                stateCallback
                        .accept(new ProgressEntity.Builder()
                                .withPercent(
                                        (double) countingInputStream.getCount() / (double) finalSize * (double) 100)
                                .withProgressText("Extracting " + outputFile.getName()).build());

            }
            return uncompressedFiles;
        } catch (IOException | org.apache.commons.compress.archivers.ArchiveException e) {
            throw new ArchiveException("Unable to extract the file", e);
        }
    }
}
