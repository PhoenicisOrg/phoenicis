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

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.entities.ProgressEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

@Safe
public class Zip {
    private static final String ZIP_ERROR_MESSAGE = "Unable to open input stream";
    private final Logger LOGGER = LoggerFactory.getLogger(Zip.class);

    public List<File> uncompressZipFile(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback) {
        return uncompressZipFile(inputFile, outputDir, stateCallback, true);
    }

    List<File> uncompressZipFile(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback,
            boolean trackExtractedFiles) {
        LOGGER.info(String.format("Attempting to unzip file \"%s\" to directory \"%s\".",
                inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));

        final Path targetDirPath = outputDir.toPath();

        try (ZipFile zipFile = new ZipFile(inputFile)) {
            final long finalSize = FileUtils.sizeOf(inputFile);

            final AtomicLong extractedBytesCounter = new AtomicLong(0);
            final Consumer<ZipExtractionResult> unzipCallback = zipExtractionResult -> {
                final double currentExtractedBytes = extractedBytesCounter
                        .addAndGet(zipExtractionResult.getExtractedBytes());

                stateCallback
                        .accept(new ProgressEntity.Builder()
                                .withPercent(currentExtractedBytes / finalSize * 100.0D)
                                .withProgressText(tr("Extracted {0}", zipExtractionResult.getFileName())).build());
            };

            final List<File> extractedFiles = new ArrayList<>();

            for (ZipArchiveEntry entry : Collections.list(zipFile.getEntries())) {
                final File extractedFile = unzipEntry(zipFile, entry, targetDirPath, unzipCallback);

                if (trackExtractedFiles) {
                    extractedFiles.add(extractedFile);
                }
            }

            return extractedFiles;
        } catch (IOException e) {
            throw new ArchiveException(ZIP_ERROR_MESSAGE, e);
        }
    }

    private File unzipEntry(ZipFile zipFile, ZipArchiveEntry entry, Path targetDirectory,
            Consumer<ZipExtractionResult> unzipCallback) {
        final String fileName = entry.getName();
        final long compressedSize = entry.getCompressedSize();

        final Path targetPath = targetDirectory.resolve(fileName);

        if (!targetPath.normalize().startsWith(targetDirectory.normalize())) {
            throw new RuntimeException("Bad zip entry");
        }
        try {
            if (entry.isDirectory()) {
                LOGGER.info(String.format("Attempting to create output directory %s.", targetPath.toString()));

                Files.createDirectories(targetPath);
            } else {
                Files.createDirectories(targetPath.getParent());

                try (InputStream in = zipFile.getInputStream(entry)) {
                    LOGGER.info(String.format("Creating output file %s.", targetPath.toString()));

                    Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);

                    // update progress bar
                    unzipCallback.accept(new ZipExtractionResult(compressedSize, fileName));
                }
            }

            return targetPath.toFile();
        } catch (IOException e) {
            throw new ArchiveException(String.format("Unable to extract file \"%s\"", fileName), e);
        }
    }

    private static final class ZipExtractionResult {
        private final long extractedBytes;

        private final String fileName;

        private ZipExtractionResult(long extractedBytes, String fileName) {
            this.extractedBytes = extractedBytes;
            this.fileName = fileName;
        }

        private long getExtractedBytes() {
            return extractedBytes;
        }

        private String getFileName() {
            return fileName;
        }
    }
}
