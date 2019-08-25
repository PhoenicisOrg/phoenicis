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
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.phoenicis.configuration.security.Safe;
import org.phoenicis.entities.ProgressEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Safe
public class Zip {
    private static final String ZIP_ERROR_MESSAGE = "Unable to open input stream";
    private final Logger LOGGER = LoggerFactory.getLogger(Zip.class);

    public List<File> uncompressZipFile(File inputFile, File outputDir, Consumer<ProgressEntity> stateCallback) {
        final long finalSize = FileUtils.sizeOf(inputFile);
        final AtomicLong counter = new AtomicLong(0);

        final BlockingDeque<ZipExtractionResult> queue = new LinkedBlockingDeque<>();
        final ExecutorService uiExecutor = Executors.newSingleThreadExecutor();

        LOGGER.info(String.format("Attempting to unzip file \"%s\" to directory \"%s\".",
                inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));

        final Path targetDirPath = outputDir.toPath();

        try (ZipFile zipFile = new ZipFile(inputFile)) {
            final Future<List<File>> result = uiExecutor
                    .submit(() -> Collections.list(zipFile.getEntries()).parallelStream()
                            .map(entry -> unzipEntry(zipFile, entry, targetDirPath, queue))
                            .collect(Collectors.toList()));

            double maxTotalExtractedBytes = 0;
            List<ZipExtractionResult> fetched = new ArrayList<>();
            while (!result.isDone() || !queue.isEmpty()) {
                queue.drainTo(fetched);

                for (ZipExtractionResult zipExtractionResult : fetched) {
                    double totalExtractedBytes = counter.getAndAdd(zipExtractionResult.getExtractedBytes());

                    // ensure that the progressbar only increases and never decreases
                    if (totalExtractedBytes > maxTotalExtractedBytes) {
                        maxTotalExtractedBytes = totalExtractedBytes;

                        stateCallback
                                .accept(new ProgressEntity.Builder()
                                        .withPercent(totalExtractedBytes / finalSize * 100.0D)
                                        .withProgressText("Extracting " + zipExtractionResult.getFileName()).build());
                    }
                }

                Thread.sleep(25);
            }

            return result.get();
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new ArchiveException(ZIP_ERROR_MESSAGE, e);
        }
    }

    private File unzipEntry(ZipFile zipFile, ZipArchiveEntry entry, Path targetDir,
            BlockingDeque<ZipExtractionResult> queue) {
        try {
            Path targetPath = targetDir.resolve(Paths.get(entry.getName()));

            if (entry.isDirectory()) {
                LOGGER.info(String.format("Attempting to create output directory %s.", targetPath.toString()));

                Files.createDirectories(targetPath);
            } else {
                Files.createDirectories(targetPath.getParent());

                try (CountingInputStream in = new CountingInputStream(zipFile.getInputStream(entry))) {
                    LOGGER.info(String.format("Creating output file %s.", targetPath.toString()));

                    Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);

                    // enqueue the extraction information
                    queue.add(new ZipExtractionResult(in.getCount(), entry.getName()));
                }
            }

            return targetPath.toFile();
        } catch (IOException e) {
            throw new ArchiveException(String.format("Unable to extract file \"%s\"", entry.getName()), e);
        }
    }

    private class ZipExtractionResult {
        private final long extractedBytes;

        private final String fileName;

        public ZipExtractionResult(long extractedBytes, String fileName) {
            this.extractedBytes = extractedBytes;
            this.fileName = fileName;
        }

        public long getExtractedBytes() {
            return extractedBytes;
        }

        public String getFileName() {
            return fileName;
        }
    }
}
